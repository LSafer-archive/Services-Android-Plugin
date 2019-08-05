package lsafer.services.part.sc;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

import lsafer.services.annotation.Description;
import lsafer.services.annotation.Parameters;
import lsafer.services.annotation.Permissions;
import lsafer.services.io.Task;
import lsafer.services.util.Arguments;

/**
 * vibrate on call.
 *
 * @author LSaferSE
 * @version 2
 * @since 22-Jun-19
 */
@Description("vibrate on call.")
@Permissions({"android.permission.VIBRATE"})
final public class Vibrate extends Task.Part {

    /**
     * vibrate as long as the passed value.
     *
     * @param arguments context, integer
     */
    @Parameters(input = {"context", "integer"}, output = {})
    @Description("vibrate as long as the passed value.")
    public void start(Arguments arguments) {
        Vibrator v = (Vibrator) arguments.context.getSystemService(Context.VIBRATOR_SERVICE);
        assert v != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            v.vibrate(VibrationEffect.createOneShot(arguments.integer, 1));
        else v.vibrate(arguments.integer);
    }

}
