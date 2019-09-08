package lsafer.services.plugin.bg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import lsafer.services.annotation.Controller;
import lsafer.services.annotation.Entry;
import lsafer.services.annotation.Invokable;
import lsafer.services.plugin.R;
import lsafer.services.util.Process;
import lsafer.services.util.Service;

import java.util.function.Consumer;

/**
 * Calls the next process when the target action detected by the linked {@link Receiver}.
 *
 * @author LSaferSE
 * @version 4 beta (07-Sep-2019)
 * @since 14-Jun-19
 */
@Service.Defaults(process = OnBroadcast.OnBroadcastProcess.class)
final public class OnBroadcast extends Service<OnBroadcast.OnBroadcastProcess> {
    /**
     * Calls the next process when the target action detected by the linked {@link Receiver}.
     */
    @Controller(description = R.string.OnBroadcast)
    final public static class OnBroadcastProcess extends Process<OnBroadcast> {
        /**
         * The action to listen to.
         */
        @Entry(description = R.string.OnBroadcast_action,
                values_description = R.array.OnBroadcast_action,
                value = {
                "android.intent.action.ACTION_POWER_CONNECTED",
                "android.intent.action.ACTION_POWER_DISCONNECTED",
                "android.intent.action.ACTION_SHUTDOWN",
                "android.intent.action.AIRPLANE_MODE",
                "android.intent.action.BATTERY_CHANGED",
                "android.intent.action.BATTERY_LOW",
                "android.intent.action.BATTERY_OKAY",
                "android.intent.action.CAMERA_BUTTON",
                "android.intent.action.CONFIGURATION_CHANGED",
                "android.intent.action.DATE_CHANGED",
                "android.intent.action.DEVICE_STORAGE_LOW",
                "android.intent.action.DEVICE_STORAGE_OK",
                "android.intent.action.DOCK_EVENT",
                "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE",
                "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE",
                "android.intent.action.HEADSET_PLUG",
                "android.intent.action.INPUT_METHOD_CHANGED",
                "android.intent.action.MEDIA_BUTTON",
                "android.intent.action.SCREEN_OFF",
                "android.intent.action.SCREEN_ON",
        })
        public String action = "";

        /**
         * The receiver that will notify this. When it receives the targeted action.
         */
        private transient Receiver receiver = new Receiver(this.action, intent -> this.callNext(Service.ACTION_INVOKE, Invokable.start, intent));

        /**
         * Register the receiver.
         *
         * @see lsafer.services.plugin.R.string#OnBroadcast_start
         */
        @Invokable(description = R.string.OnBroadcast_start)
        public void start() {
            this.service.registerReceiver(this.receiver, new IntentFilter(this.action));
        }

        /**
         * Unregister the receiver.
         */
        @Invokable(description = R.string.OnBroadcast_stop)
        public void stop() {
            this.service.unregisterReceiver(this.receiver);
        }
    }

    /**
     * The {@link BroadcastReceiver reciever} that is responsible on notifying the linked process. When it receives it's targeted action.
     */
    final public static class Receiver extends BroadcastReceiver {
        /**
         * The action to call the listener. When detected.
         */
        public String action = "";

        /**
         * The listener that will be called. When the targeted action get detected.
         */
        public Consumer<Intent> listener;

        /**
         * Default Constructor.
         */
        public Receiver() {

        }

        /**
         * Initialize this.
         *
         * @param action to wait for
         * @param listener to be called when the action get detected
         */
        public Receiver(String action, Consumer<Intent> listener) {
            this.listener = listener;
            this.action = action;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(this.action))
                this.listener.accept(intent);
        }
    }
}
