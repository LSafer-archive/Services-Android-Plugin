package lsafer.services.part.sc;

import android.media.AudioManager;
import android.widget.TextView;
import android.widget.Toast;

import lsafer.services.annotation.Description;
import lsafer.services.annotation.Parameters;
import lsafer.services.annotation.Permissions;
import lsafer.services.annotation.Values;
import lsafer.services.io.Task;
import lsafer.services.util.Arguments;

/**
 * change the Audio Volume when called.
 *
 * @author LSafer
 * @version 4
 * @since 10 Jun 2019
 */
@Description("change the Audio Volume when called.")
@Permissions({"android.permission.WRITE_SETTINGS"})
@SuppressWarnings("WeakerAccess")
final public class ChangeAudio extends Task.Part {

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
     * is it allowed to show Audio UI on change.
     */
    @Values({"true|show audio UI", "false|don't show audio UI"})
    @Description("is it allowed to show Audio UI on change.")
    public Boolean show_ui = false;

    /**
     * Stream to adjust.
     */
    @Values({
            "0|voice call",
            "2|ringtone",
            "3|media",
            "4|alarm",
            "5|notification",
            "8|DTMF",
            "10|Accessibility",
    })
    @Description("audio stream to adjust.")
    public Integer stream = 3;

    /**
     * get the current targeted audio stream's level.
     *
     * @param arguments context
     * @return integer
     */
    @Parameters(input = {"context"}, output = {"integer"})
    @Description("get the current targeted audio stream's level.")
    public Integer get(Arguments arguments) {
        //noinspection ConstantConditions
        return arguments.context.getSystemService(AudioManager.class).getStreamVolume(this.stream);
    }

    /**
     * set the targeted audio stream's level to the passed value.
     *
     * @param arguments context, integer
     */
    @Parameters(input = {"context", "integer"}, output = {})
    @Description("set the targeted audio stream's level to the passed value.")
    public void update(Arguments arguments) {
        //noinspection ConstantConditions
        arguments.context.getSystemService(AudioManager.class)
                .setStreamVolume(this.stream, arguments.integer, this.show_ui ?
                        AudioManager.FLAG_SHOW_UI : AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

        if (this.show_toast)
            android.widget.Toast.makeText(arguments.context,
                    String.valueOf(arguments.integer),
                    android.widget.Toast.LENGTH_SHORT).show();

        if (this.show_message) {
            TextView tv = new TextView(arguments.context);
            android.widget.Toast t = new Toast(arguments.context);

            tv.setText(String.valueOf(arguments.integer));
            t.setView(tv);
            t.show();
        }
    }

}
