package lsafer.services.part.sc;

import android.content.ContentResolver;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

import lsafer.services.annotation.Description;
import lsafer.services.annotation.Parameters;
import lsafer.services.annotation.Permissions;
import lsafer.services.annotation.Values;
import lsafer.services.io.Task;
import lsafer.services.util.Arguments;

/**
 * change brightness volume on called.
 *
 * @author LSafer
 * @version 4
 * @since 10 Jun 2019
 */
@SuppressWarnings("WeakerAccess")
@Permissions({"android.permission.WRITE_SETTINGS"})
@Description("change brightness volume on called.")
final public class ChangeBrightness extends Task.Part {

    /**
     * is it allowed to display a message on change.
     */
    @Values({"true|display a message", "false|don't display a message"})
    @Description("is it allowed to display a message on change.")
    public Boolean show_message = true;

    /**
     * is it allowed to Toast on change.
     */
    @Values({"true|toast", "false|don't toast"})
    @Description("is it allowed to Toast on change.")
    public Boolean show_toast = false;

    /**
     * get the current brightness level.
     *
     * @param arguments context
     * @return integer
     * @throws Settings.SettingNotFoundException if the system don't support this
     */
    @Parameters(input = {"context"}, output = {"integer"})
    @Description("get the current brightness level.")
    public Integer get(Arguments arguments) throws Settings.SettingNotFoundException {
        return Settings.System.getInt(arguments.context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
    }

    /**
     * set brightness to the passed value.
     *
     * @param arguments context, integer
     */
    @Parameters(input = {"context", "integer"}, output = {})
    @Description("set brightness to the passed value.")
    public void update(Arguments arguments) {
        ContentResolver resolver = arguments.context.getContentResolver();
        Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, arguments.integer);

        if (this.show_toast)
            android.widget.Toast.makeText(arguments.context, String.valueOf(arguments.integer), android.widget.Toast.LENGTH_SHORT).show();

        if (this.show_message) {
            TextView tv = new TextView(arguments.context);
            android.widget.Toast t = new Toast(arguments.context);

            tv.setText(String.valueOf(arguments.integer));
            t.setView(tv);
            t.show();
        }
    }

}
