package lsafer.services.part.bg;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.gesture.GestureOverlayView;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import lsafer.services.annotation.Description;
import lsafer.services.annotation.Parameters;
import lsafer.services.annotation.Permissions;
import lsafer.services.annotation.Values;
import lsafer.services.io.Task;
import lsafer.services.support.ForegroundService;
import lsafer.services.text.Run;
import lsafer.services.util.Arguments;

/**
 * Creates a Bar-like {@link GestureOverlayView} to the target position
 * and calls a specific calls to the handler the time the bar get touched.
 *
 * @author LSafer
 * @version 4
 * @since 10 Jun 2019
 */
@SuppressWarnings("WeakerAccess")
@Description("Creates a Bar to the target edge and calls a specific calls to the handler the time the bar get touched.")
@Permissions({"android.permission.SYSTEM_ALERT_WINDOW"})
final public class OnEdgeGesture extends Task.Part {

    /**
     * alpha/transparency of the view of this.
     */
    @Values({"0|transparent", "1|visible"})
    @Description("transparency of the overlay")
    public Integer alpha = 1;

    /**
     * Color of the view of this.
     */
    @Values({
            "#ffff0000|red",
            "#ff00ff00|green",
            "#ff0000ff|blue",
            "%color|custom color"
    })
    @Description("Color of the overlay.")
    public String color = "#ffff0000";

    /**
     * Position of where the view should be in display.
     */
    @Values({
            "0|bottom",
            "1|left",
            "2|top",
            "3|right"
    })
    @Description("Position of where the overlay should be displayed.")
    public Integer position = 1;

    /**
     * is allowed Rotate the views when the screen get rotated.
     */
    @Values({"true|rotate the overlay with screen", "false|keep at the same physical position"})
    @Description("is allowed rotate the overlay when the screen get rotated.")
    public Boolean rotate = false;

    /**
     * Width of the view.
     */
    @Values("%integer|custom")
    @Description("Width of the overlay.")
    public Integer width = 20;

    /**
     * the listener when a gesture get detected inside the overlay.
     */
    private GestureOverlayView.OnGestureListener $listener;

    /**
     * on configuration change listener.
     */
    private Runnable $conf_listener;

    /**
     * view to call the handler on touched.
     */
    private EdgeOverlay $overlay;

    /**
     * link this with the overlay that matches this position.
     *
     * @param arguments context
     */
    @Parameters(input = {"context"}, output = {})
    @Description("link this with the overlay that matches this position.")
    public void start(Arguments arguments) {
        ForegroundService.runFunctions(arguments.context, "OnEdgeGesture", service -> {
            WindowManager.LayoutParams params = new WindowManager.LayoutParams();

            //noinspection ConstantConditions
            int position = this.rotate ? this.position :
                    Math.abs(service.context.getSystemService(WindowManager.class)
                    .getDefaultDisplay().getRotation() * 3 + this.position) % 4;

            boolean landscape = position == 0 || position == 2;

            params.alpha = this.alpha;
            params.width = landscape ? FrameLayout.LayoutParams.MATCH_PARENT : this.width;
            params.height = !landscape ? FrameLayout.LayoutParams.MATCH_PARENT : this.width;

            this.$overlay = EdgeOverlay.getInstance(service.context, position, params);
            this.$overlay.setBackgroundColor(android.graphics.Color.parseColor(this.color));
            this.$overlay.addOnGestureListener(this.$listener = new GestureOverlayView.OnGestureListener() {

                @Override
                public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
                    OnEdgeGesture.this.next(Run.start, null, overlay.getContext(), (int) (landscape ? event.getX() : event.getY()));
                }

                @Override
                public void onGesture(GestureOverlayView overlay, MotionEvent event) {
                    OnEdgeGesture.this.next(Run.update, null, overlay.getContext(), (int) (landscape ? event.getX() : event.getY()));
                    overlay.clear(false);
                }

                @Override
                public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
                    OnEdgeGesture.this.next(Run.stop, null, overlay.getContext(), (int) (landscape ? event.getX() : event.getY()));
                }

                @Override
                public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
                    OnEdgeGesture.this.next(Run.stop, null, overlay.getContext(), (int) (landscape ? event.getX() : event.getY()));
                }

            });
            this.$overlay.addOnConfigurationChangeListener(this.$conf_listener = ()-> this.update(service));

            return null;
        });
    }

    /**
     * unlink this from the linked overlay.
     *
     * @param arguments no-use
     */
    @Parameters(input = {}, output = {})
    @Description("unlink this from the linked overlay.")
    public void stop(Arguments arguments) {
        this.$overlay.removeOnConfigurationChangeListener(this.$conf_listener);
        this.$overlay.removeOnGestureListener(this.$listener);
    }

    /**
     * unlink this from it's overlay and link
     * it with the overlay that matches
     * thi new position.
     *
     * @param arguments context
     */
    @Parameters(input = {"context"}, output = {})
    @Description("unlink this from it's overlay and link it with the overlay that matches thi new position.")
    public void update(Arguments arguments) {
        this.$overlay.removeOnConfigurationChangeListener(this.$conf_listener);
        this.$overlay.removeOnGestureListener(this.$listener);
        this.start(arguments);
    }

    /**
     * Overlay class to simplify managing multi agents overlay.
     */
    @SuppressLint("ViewConstructor, RtlHardcoded")
    @SuppressWarnings("UnusedReturnValue")
    public static class EdgeOverlay extends GestureOverlayView {

        /**
         * Overlays to be able to apply more that one provider at the same position.
         */
        private volatile static EdgeOverlay[] Overlays = new EdgeOverlay[4];

        /**
         * added listeners count.
         */
        private Integer listeners = 0;

        /**
         * params to appear in window with.
         */
        private WindowManager.LayoutParams params;

        /**
         * index of the position of this.
         * <p>
         * 0:bottom
         * 1:left
         * 2:top
         * 3:right
         */
        private Integer position;

        /**
         * on configurations change listeners.
         */
        private volatile List<Runnable> con = new ArrayList<>();

        /**
         * to init this.
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
         * link the given provider with the target position layout.
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
            this.listeners++;
        }

        @Override
        public void removeOnGestureListener(OnGestureListener listener) {
            super.removeOnGestureListener(listener);
            this.listeners--;
            if (this.listeners == 0) {
                this.dismiss();
                Overlays[this.position] = null;
            }
        }

        /**
         * add an element to {@link #con}.
         *
         * @param runnable to add
         */
        public void addOnConfigurationChangeListener(Runnable runnable){
            this.con.add(runnable);
        }

        /**
         * remove an element to {@link #con}.
         *
         * @param runnable to remove
         */
        public void removeOnConfigurationChangeListener(Runnable runnable){
            this.con.remove(runnable);
        }

        @Override
        protected void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            new ArrayList<>(con).forEach(Runnable::run);
        }

        /**
         * remove this overlay from window.
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
         * add this overlay to the window.
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

}
