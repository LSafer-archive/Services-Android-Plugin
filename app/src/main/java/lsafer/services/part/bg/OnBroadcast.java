package lsafer.services.part.bg;

import java.util.function.Function;

import lsafer.services.annotation.Description;
import lsafer.services.annotation.Parameters;
import lsafer.services.annotation.Permissions;
import lsafer.services.annotation.Values;
import lsafer.services.io.Task;
import lsafer.services.support.BroadcastReceiver;
import lsafer.services.text.Run;
import lsafer.services.util.Arguments;

/**
 * Provides when the target action get broadcast
 * using {@link BroadcastReceiver}.
 *
 * @author LSaferSE
 * @version 2
 * @since 14-Jun-19
 */
@Permissions({})
@Description("Provides when the target action get broadcast.")
@SuppressWarnings("WeakerAccess")
final public class OnBroadcast extends Task.Part {

    /**
     * action to listen to.
     */
    @Values({
            "android.intent.action.ACTION_POWER_CONNECTED|when power cable plugged in",
            "android.intent.action.ACTION_POWER_DISCONNECTED|when power cable plugged out",
            "android.intent.action.ACTION_SHUTDOWN|when device powered off",
            "android.intent.action.AIRPLANE_MODE|when airplane mode changed",
            "android.intent.action.BATTERY_CHANGED|when battery changed",
            "android.intent.action.BATTERY_LOW|when battery low",
            "android.intent.action.BATTERY_OKAY|when battery ok",
            "android.intent.action.CAMERA_BUTTON|when camera button (if available) clicked",
            "android.intent.action.CONFIGURATION_CHANGED|when configuration changed (like screen rotation)",
            "android.intent.action.DATE_CHANGED|when date change (next day)",
            "android.intent.action.DEVICE_STORAGE_LOW|when storage get low",
            "android.intent.action.DEVICE_STORAGE_OK|when storage ok",
            "android.intent.action.DOCK_EVENT|when dock plugged/unplugged",
            "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE|when application installed",
            "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE|when application uninstalled",
            "android.intent.action.HEADSET_PLUG|when headset plugged/unplugged",
            "android.intent.action.INPUT_METHOD_CHANGED|when input method changed",
            "android.intent.action.MEDIA_BUTTON|when median   button clicked",
            "android.intent.action.SCREEN_OFF|when screen turned off",
            "android.intent.action.SCREEN_ON|when screen turned on",
    })
    @Description("Action to listen to.")
    public String action = "";

    /**
     * to update the provider when called.
     */
    private Function<Arguments, ?> $listener;

    /**
     * register the receiver.
     *
     * @param arguments context
     */
    @Parameters(input = {"context"}, output = {})
    @Description("register the receiver.")
    public void start(Arguments arguments) {
        BroadcastReceiver.getInstance(arguments.context, this.action)
                .addFunctions(this.$listener = (args) -> next(Run.start, null, args));
    }

    /**
     * unregister the receiver.
     *
     * @param arguments context
     */
    @Parameters(input = {"context"}, output = {""})
    @Description("unregister the receiver.")
    public void stop(Arguments arguments) {
        BroadcastReceiver.getInstance(arguments.context, this.action).removeFunctions(this.$listener);
        this.$listener = null;
    }

}
