package lsafer.services.plugin.bg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.gesture.GestureOverlayView;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;
import lsafer.services.annotation.Controller;
import lsafer.services.annotation.Entry;
import lsafer.services.annotation.Invokable;
import lsafer.services.plugin.R;
import lsafer.services.util.Arguments;
import lsafer.services.util.Process;
import lsafer.services.util.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a Bar-like {@link GestureOverlayView} to the target position.
 * And calls a specific calls to the handler the time the bar get touched.
 *
 * @author LSafer
 * @version 5 beta (07-Sep-2019)
 * @since 10 Jun 2019
 */
@SuppressWarnings("WeakerAccess")
//android.permission.SYSTEM_ALERT_WINDOW
@Service.Defaults(process = OnEdgeGesture.OnEdgeGestureProcess.class)
final public class OnEdgeGesture extends Service<OnEdgeGesture.OnEdgeGestureProcess> {
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        this.processes.forEach((key, process)-> process.update());
    }

    /**
     * Overlay class. To simplify managing multi agents overlay.
     */
    @SuppressLint("ViewConstructor, RtlHardcoded")
    @SuppressWarnings("UnusedReturnValue")
    final public static class EdgeOverlay extends GestureOverlayView {
        /**
         * Positions overlays. To be able to apply more that one provider at the same position.
         */
        private volatile static EdgeOverlay[] Overlays = new EdgeOverlay[4];

        /**
         * Params to appear in the window with.
         */
        private WindowManager.LayoutParams params;

        /**
         * Index of the position of this.
         * <br>
         * 0:bottom
         * 1:left
         * 2:top
         * 3:right
         */
        private Integer position;

        /**
         * A list of all {@link OnGestureListener listeners} linked to this.
         */
        final private List<OnGestureListener> listeners = new ArrayList<>();

        /**
         * Initialize this.
         *
         * @param context  of application
         * @param position to display this at
         * @param params   to display this with
         */
        private EdgeOverlay(Context context, int position, WindowManager.LayoutParams params) {
            super(context);
            this.position = position;
            this.params = params;

            params.type = Build.VERSION.SDK_INT >= 26 ?
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY :
                    WindowManager.LayoutParams.TYPE_PHONE;
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;

            switch (position) {
                case 0:
                    params.gravity = Gravity.BOTTOM;
                    break;
                case 1:
                    params.gravity = Gravity.LEFT;
                    break;
                case 2:
                    params.gravity = Gravity.TOP;
                    break;
                case 3:
                    params.gravity = Gravity.RIGHT;
                    break;
            }
        }

        /**
         * Get an overlay instance that is in the passed position.
         *
         * @param context  of application
         * @param position to display overlay at
         * @param params   to display overlay with
         * @return the overlay that the provider have linked with
         */
        public static EdgeOverlay getInstance(Context context, int position, WindowManager.LayoutParams params) {
            return Overlays[position] = Overlays[position] == null ? new EdgeOverlay(context, position, params).show() : Overlays[position];
        }

        @Override
        public void addOnGestureListener(OnGestureListener listener) {
            super.addOnGestureListener(listener);
            this.listeners.add(listener);
        }

        @Override
        public void removeOnGestureListener(OnGestureListener listener) {
            super.removeOnGestureListener(listener);
            this.listeners.remove(listener);
            if (this.listeners.size() == 0) {
                this.dismiss();
                Overlays[this.position] = null;
            }
        }

        /**
         * Remove this overlay from window.
         *
         * @return this
         */
        public EdgeOverlay dismiss() {
            try {
                //noinspection ConstantConditions
                this.getContext().getSystemService(WindowManager.class).removeView(this);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return this;
        }

        /**
         * Add this overlay to the window.
         *
         * @return this
         */
        public EdgeOverlay show() {
            try {
                //noinspection ConstantConditions
                this.getContext().getSystemService(WindowManager.class).addView(this, this.params);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return this;
        }
    }

    /**
     * Creates a Bar-like {@link GestureOverlayView} to the target position.
     * And calls a specific calls to the next process the time the bar get touched.
     */
    @Controller(description = R.string.OnEdgeGesture)
    final public static class OnEdgeGestureProcess extends Process<OnEdgeGesture> {
        /**
         * Transparency of the view of this.
         */
        @Entry(description = R.string.OnEdgeGesture_alpha,
                values_description = R.array.OnEdgeGesture_alpha,
                value = {"0", "1"})
        public Integer alpha = 1;

        /**
         * Color of the view of this.
         */
        @Entry(description = R.string.OnEdgeGesture_color,
                values_description = R.array.OnEdgeGesture_color,
                value = {"#ffff0000", "#ff00ff00", "#ff0000ff"})
        public String color = "#ffff0000";

        /**
         * Position of where the view should be in display.
         */
        @Entry(description = R.string.OnEdgeGesture_position,
                values_description = R.array.OnEdgeGesture_position,
                value = {"0", "1", "2", "3"})
        public Integer position = 1;

        /**
         * Is allowed Rotate the views when the screen get rotated.
         */
        @Entry(description = R.string.OnEdgeGesture_rotate,
                values_description = R.array.OnEdgeGesture_rotate,
                value = {"true", "false"})
        public Boolean rotate = false;

        /**
         * Width of the view.
         */
        @Entry(description = R.string.OnEdgeGesture_width,
                values_description = R.array.OnEdgeGesture_width,
                value = {"20", "35", "70"})
        public Integer width = 20;

        /**
         * The listener when a gesture get detected inside the overlay.
         */
        private transient GestureOverlayView.OnGestureListener listener;

        /**
         * View to call the next process on touched.
         */
        private transient EdgeOverlay overlay;

        /**
         * Link this with the overlay that matches this position.
         */
        @Invokable(description = R.string.OnEdgeGesture_start)
        public void start() {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();

            //noinspection ConstantConditions
            int position = this.rotate ? this.position : Math.abs(this.service.getSystemService(WindowManager.class)
                    .getDefaultDisplay().getRotation() * 3 + this.position) % 4;

            boolean landscape = position == 0 || position == 2;

            params.alpha = this.alpha;
            params.width = landscape ? FrameLayout.LayoutParams.MATCH_PARENT : this.width;
            params.height = !landscape ? FrameLayout.LayoutParams.MATCH_PARENT : this.width;

            this.overlay = EdgeOverlay.getInstance(this.service, position, params);
            this.overlay.setBackgroundColor(android.graphics.Color.parseColor(this.color));
            this.overlay.addOnGestureListener(this.listener = new GestureOverlayView.OnGestureListener() {
                @Override
                public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
                    Arguments arguments = new Arguments();

                    arguments.put("x", landscape ? event.getX() : event.getY());
                    arguments.put("y", landscape ? event.getY() : event.getX());

                    OnEdgeGestureProcess.this.callNext(Service.ACTION_INVOKE, Invokable.start, arguments);
                }

                @Override
                public void onGesture(GestureOverlayView overlay, MotionEvent event) {
                    Arguments arguments = new Arguments();

                    arguments.put("x", landscape ? event.getX() : event.getY());
                    arguments.put("y", landscape ? event.getY() : event.getX());

                    OnEdgeGestureProcess.this.callNext(Service.ACTION_INVOKE, Invokable.update, arguments);
                    overlay.clear(false);
                }

                @Override
                public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
                    Arguments arguments = new Arguments();

                    arguments.put("x", landscape ? event.getX() : event.getY());
                    arguments.put("y", landscape ? event.getY() : event.getX());

                    OnEdgeGestureProcess.this.callNext(Service.ACTION_INVOKE, Invokable.stop, arguments);
                }

                @Override
                public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
                    Arguments arguments = new Arguments();

                    arguments.put("x", landscape ? event.getX() : event.getY());
                    arguments.put("y", landscape ? event.getY() : event.getX());

                    OnEdgeGestureProcess.this.callNext(Service.ACTION_INVOKE, Invokable.stop, arguments);
                }
            });
        }

        /**
         * Unlink this from the linked overlay.
         */
        @Invokable(description = R.string.OnEdgeGesture_stop)
        public void stop() {
            this.overlay.removeOnGestureListener(this.listener);
        }

        /**
         * Unlink this from it\'s overlay. And link it with the overlay that matches the new position.
         */
        @Invokable(description = R.string.OnEdgeGesture_update)
        public void update() {
            this.overlay.removeOnGestureListener(this.listener);
            this.start();
        }
    }
}
