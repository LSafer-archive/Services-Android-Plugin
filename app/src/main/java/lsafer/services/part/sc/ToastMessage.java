package lsafer.services.part.sc;

import android.widget.Toast;

import lsafer.services.annotation.Description;
import lsafer.services.annotation.Parameters;
import lsafer.services.annotation.Permissions;
import lsafer.services.io.Task;
import lsafer.services.util.Arguments;

/**
 * task toast the passed value when called.
 *
 * @author LSaferSE
 * @version 2
 * @since 15-Jun-19
 */
@Permissions({})
@Description("task toast the passed value when called.")
final public class ToastMessage extends Task.Part {

    /**
     * toast the passed text.
     *
     * @param arguments context, string
     */
    @Parameters(input = {"context", "string"}, output = {})
    @Description("toast the passed text.")
    public void start(Arguments arguments) {
        Toast.makeText(arguments.context, arguments.string, Toast.LENGTH_LONG).show();

    }

}
