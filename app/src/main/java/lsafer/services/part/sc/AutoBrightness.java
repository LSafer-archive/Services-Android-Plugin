package lsafer.services.part.sc;

import android.provider.Settings;

import lsafer.services.annotation.Description;
import lsafer.services.annotation.Parameters;
import lsafer.services.annotation.Permissions;
import lsafer.services.io.Task;
import lsafer.services.util.Arguments;

/**
 * turn on/off auto brightness when called.
 *
 * @author LSafer
 * @version 3
 * @since 10 Jun 2019
 */
@Description("turn on/off auto brightness when called.")
@Permissions({"android.permission.WRITE_SETTINGS"})
final public class AutoBrightness extends Task.Part {

    /**
     * get whether the auto brightness turned on or not.
     *
     * @param arguments context
     * @return boolean
     * @throws Settings.SettingNotFoundException if the support this
     */
    @Parameters(input = {"context"}, output = {"boolean"})
    @Description("get whether the auto brightness turned on or not.")
    public Boolean get(Arguments arguments) throws Settings.SettingNotFoundException {
        return Settings.System.getInt(arguments.context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
    }

    /**
     * turn on auto brightness depending on the passed boolean.
     *
     * @param arguments context
     */
    @Parameters(input = {"context"}, output = {})
    @Description("turn on auto brightness depending on the passed status.")
    public void start(Arguments arguments) {
        Settings.System.putInt(arguments.context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
    }

    /**
     * turn off auto brightness depending on the passed boolean.
     *
     * @param arguments context
     */
    @Parameters(input = {"context"}, output = {})
    @Description("turn off auto brightness depending on the passed status.")
    public void stop(Arguments arguments) {
        Settings.System.putInt(arguments.context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

}
