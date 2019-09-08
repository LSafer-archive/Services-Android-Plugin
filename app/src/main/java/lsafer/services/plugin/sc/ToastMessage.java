package lsafer.services.plugin.sc;

import android.widget.Toast;
import lsafer.services.annotation.Controller;
import lsafer.services.annotation.Entry;
import lsafer.services.annotation.Invokable;
import lsafer.services.plugin.R;
import lsafer.services.util.Process;
import lsafer.services.util.Service;

/**
 * Task toast the passed value when called.
 *
 * @author LSaferSE
 * @version 3 beta (07-Sep-2019)
 * @since 15-Jun-19
 */
@Service.Defaults(process = ToastMessage.ToastMessageProcess.class)
final public class ToastMessage extends Service<ToastMessage.ToastMessageProcess> {
    /**
     * Task toast the passed value when called.
     */
    @Controller(description = R.string.ToastMessage)
    final public static class ToastMessageProcess extends Process<ToastMessage> {
        /**
         * Absolute value to use on calling {@link #start(String)}.
         */
        @Entry(description = R.string.ToastMessage_absolute,
                values_description = R.array.ToastMessage_absolute,
                value = {"null"})
        public String absolute = null;

        /**
         * Toast the passed text.
         *
         * @param string to display
         */
        @Invokable(description = R.string.ToastMessage_start,
                params_description = R.array.ToastMessage_start,
                value = {"string"})
        public void start(String string) {
            if (this.absolute != null)
                string = this.absolute;

            Toast.makeText(this.service, string, Toast.LENGTH_LONG).show();
        }
    }
}
