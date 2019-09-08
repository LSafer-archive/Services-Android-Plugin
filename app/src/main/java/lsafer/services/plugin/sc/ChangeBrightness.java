package lsafer.services.plugin.sc;

import android.Manifest;
import android.content.ContentResolver;
import android.provider.Settings;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import lsafer.services.annotation.Controller;
import lsafer.services.annotation.Entry;
import lsafer.services.annotation.Invokable;
import lsafer.services.plugin.R;
import lsafer.services.util.Process;
import lsafer.services.util.Service;

/**
 * Change brightness volume on called.
 *
 * @author LSafer
 * @version 5 beta (07-Sep-2019)
 * @since 10 Jun 2019
 * @see Manifest.permission#WRITE_SETTINGS
 */
@Service.Defaults(process = ChangeBrightness.ChangeBrightnessProcess.class)
final public class ChangeBrightness extends Service<ChangeBrightness.ChangeBrightnessProcess> {
    /**
     * Change brightness volume on called.
     */
    @Controller(description = R.string.ChangeBrightness)
    final public static class ChangeBrightnessProcess extends Process<ChangeBrightness> {
        /**
         * Absolute value to use on calling {@link #start(Integer)}.
         */
        @Entry(description = R.string.ChangeBrightness_absolute,
                values_description = R.array.ChangeBrightness_absolute,
                value = {"null"})
        public Integer absolute = null;

        /**
         * Is it allowed to display a message on change.
         */
        @Entry(description = R.string.ChangeBrightness_show_message,
                values_description = R.array.ChangeBrightness_show_message,
                value = {"true", "false"})
        public Boolean show_message = true;

        /**
         * Is it allowed to Toast on change.
         */
        @Entry(description = R.string.ChangeBrightness_show_toast,
                values_description = R.array.ChangeBrightness_show_toast,
                value = {"true", "false"})
        public Boolean show_toast = false;

        /**
         * Maximum allowed value to be set.
         */
        @Entry(description = R.string.ChangeBrightness_maximum,
                values_description = R.array.ChangeBrightness_maximum,
                value = {"255"})
        public Integer maximum = 255;

        /**
         * Minimum allowed value to be set.
         */
        @Entry(description = R.string.ChangeBrightness_minimum,
                values_description = R.array.ChangeBrightness_minimum,
                value = {"0"})
        public Integer minimum = 0;

        /**
         * The current showing toast.
         */
        private transient Toast message;

        /**
         * Get the current brightness level.
         *
         * @throws Settings.SettingNotFoundException if the system don't support this
         */
        @Invokable(description = R.string.ChangeBrightness_get)
        public void get() throws Settings.SettingNotFoundException {
            this.callPrevious(Service.ACTION_INVOKE, Invokable.results, Settings.System.getInt(this.service.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS));
        }

        /**
         * Add the passed value to the current brightness level.
         *
         * @param integer to add
         */
        @Invokable(description = R.string.ChangeBrightness_update,
                params_description = R.array.ChangeBrightness_update,
                value = {"integer"})
        public void update(Integer integer) {
            try {
                this.start(integer + Settings.System.getInt(this.service.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS));
            } catch (Settings.SettingNotFoundException ignored) {
            }
        }

        /**
         * Set brightness to the passed value.
         *
         * @param integer to set brightness level to
         */
        @Invokable(description = R.string.ChangeBrightness_start,
                params_description = R.array.ChangeBrightness_start,
                value = {"integer"})
        public void start(Integer integer){
            if (this.absolute != null)
                integer = this.absolute;

            integer = integer > this.maximum ? this.maximum : integer < this.minimum ? this.minimum : integer;

            ContentResolver resolver = this.service.getContentResolver();
            Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            Settings.System.putInt(resolver, Settings.System.SCREEN_BRIGHTNESS, integer);

            if (this.show_toast)
                android.widget.Toast.makeText(this.service, String.valueOf(integer), android.widget.Toast.LENGTH_SHORT).show();

            if (this.show_message) {
                LinearLayout layout = new LinearLayout(this.service);
                TextView view = new TextView(this.service);

                layout.setBackgroundResource(android.R.drawable.toast_frame);

                view.setPadding(40, 20, 40, 20);
                view.setFontFeatureSettings("serif-monospace");
                view.setText(String.valueOf(integer));
                view.setTextSize(17);

                layout.addView(view);

                if (this.message != null) this.message.cancel();

                this.message = new Toast(this.service);
                this.message.setView(layout);
                this.message.show();
            }
        }
    }
}
