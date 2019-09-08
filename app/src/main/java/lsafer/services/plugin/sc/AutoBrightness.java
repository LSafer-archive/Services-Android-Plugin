package lsafer.services.plugin.sc;

import android.Manifest;
import android.provider.Settings;
import lsafer.services.annotation.Controller;
import lsafer.services.annotation.Entry;
import lsafer.services.annotation.Invokable;
import lsafer.services.plugin.R;
import lsafer.services.util.Process;
import lsafer.services.util.Service;

/**
 * Turn on/off auto brightness when called.
 *
 * @author LSafer
 * @version 4 beta (07-Sep-2019)
 * @since 10 Jun 2019
 * @see Manifest.permission#WRITE_SETTINGS
 */
@Service.Defaults(process = AutoBrightness.AutoBrightnessProcess.class)
final public class AutoBrightness extends Service<AutoBrightness.AutoBrightnessProcess> {
    /**
     * Turn on/off auto brightness when called.
     */
    @Controller(description = R.string.AutoBrightness)
    final public static class AutoBrightnessProcess extends Process<AutoBrightness> {
        /**
         * Absolute value to use on calling {@link #start(Boolean)}.
         */
        @Entry(description = R.string.AutoBrightness_absolute,
                values_description = R.array.AutoBrightness_absolute,
                value = {"true", "false", "null"})
        public Boolean absolute = null;

        /**
         * Get whether the auto brightness turned on or not.
         *
         * @throws Settings.SettingNotFoundException system don't support this
         */
        @Invokable(description = R.string.AutoBrightness_get)
        public void get() throws Settings.SettingNotFoundException {
            this.callPrevious(Service.ACTION_INVOKE, Invokable.results, Settings.System.getInt(this.service.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        }

        /**
         * Turn on/off auto brightness depending on the passed boolean.
         *
         * @param bool whether the auto-brightness shall be turned on or off
         *             (will be ignored if {@link #absolute} is not null)
         */
        @Invokable(description = R.string.AutoBrightness_start,
                params_description = R.array.AutoBrightness_start,
                value = {"boolean"})
        public void start(Boolean bool) {
            if (this.absolute != null)
                bool = this.absolute;

            Settings.System.putInt(this.service.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    bool ? Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC :
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        }
    }
}
