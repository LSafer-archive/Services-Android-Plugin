package lsafer.services.plugin.sc;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import lsafer.services.annotation.Controller;
import lsafer.services.annotation.Entry;
import lsafer.services.annotation.Invokable;
import lsafer.services.plugin.R;
import lsafer.services.util.Process;
import lsafer.services.util.Service;

/**
 * Vibrate on call.
 *
 * @author LSaferSE
 * @version 3 beta (07-Sep-2019)
 * @since 22-Jun-19
 * @see Manifest.permission#VIBRATE
 */
@Service.Defaults(process = Vibrate.VibrateProcess.class)
final public class Vibrate extends Service<Vibrate.VibrateProcess> {
    /**
     * Vibrate on call.
     */
    @Controller(description = R.string.Vibrate)
    final public static class VibrateProcess extends Process<Vibrate> {
        /**
         * Absolute value to use on calling {@link #start(Integer)}.
         */
        @Entry(description = R.string.Vibrate_absolute,
                values_description = R.array.Vibrate_absolute,
                value = {"null"})
        public Integer absolute = null;

        /**
         * Vibrate with the passed value as a duration.
         *
         * @param integer vibrate length
         */
        @Invokable(description = R.string.Vibrate_start,
                params_description = R.array.Vibrate_start,
                value = {"integer"})
        public void start(Integer integer) {
            if (this.absolute != null)
                integer = this.absolute;

            Vibrator v = (Vibrator) this.service.getSystemService(Context.VIBRATOR_SERVICE);
            assert v != null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                v.vibrate(VibrationEffect.createOneShot(integer, 1));
            else v.vibrate(integer);
        }
    }
}
