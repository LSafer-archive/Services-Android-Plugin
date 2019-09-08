package lsafer.services.plugin.sc;

import android.Manifest;
import android.media.AudioManager;
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
 * Change the Audio Volume when called.
 *
 * @author LSafer
 * @version 5 beta (07-Sep-2019)
 * @since 10 Jun 2019
 * @see Manifest.permission#WRITE_SETTINGS
 */
@Service.Defaults(process = ChangeAudio.ChangeAudioProcess.class)
final public class ChangeAudio extends Service<ChangeAudio.ChangeAudioProcess> {
    /**
     * Change the Audio Volume when called.
     */
    @Controller(description = R.string.ChangeAudio)
    final public static class ChangeAudioProcess extends Process<ChangeAudio> {
        /**
         * Absolute value to use on calling {@link #start(Integer)}.
         */
        @Entry(description = R.string.ChangeAudio_absolute,
                values_description = R.array.ChangeAudio_absolute,
                value = {"null"})
        public Integer absolute = null;

        /**
         * Maximum allowed value to be set.
         */
        @Entry(description = R.string.ChangeAudio_maximum,
                values_description = R.array.ChangeAudio_maximum,
                value = {"7", "15", "30"})
        public Integer maximum = 30;

        /**
         * Minimum allowed value to be set.
         */
        @Entry(description = R.string.ChangeAudio_minimum,
                values_description = R.array.ChangeAudio_minimum,
                value = {"0"})
        public Integer minimum = 0;

        /**
         * Is it allowed to display a message on change.
         */
        @Entry(description = R.string.ChangeAudio_show_message,
                values_description = R.array.ChangeAudio_show_message,
                value = {"true", "false"})
        public Boolean show_message = true;

        /**
         * Is it allowed to Toast on change.
         */
        @Entry(description = R.string.ChangeAudio_show_toast,
                values_description = R.array.ChangeAudio_show_toast,
                value = {"true", "false"})
        public Boolean show_toast = false;

        /**
         * Is it allowed to show Audio UI on change.
         */
        @Entry(description = R.string.ChangeAudio_show_ui,
                values_description = R.array.ChangeAudio_show_ui,
                value = {"true", "false"})
        public Boolean show_ui = false;

        /**
         * Stream to adjust.
         */
        @Entry(description = R.string.ChangeAudio_stream,
                values_description = R.array.ChangeAudio_stream,
                value = {"0", "2", "3", "4", "5", "8", "10"})
        public Integer stream = 3;

        /**
         * The current showing toast.
         */
        private transient Toast message;

        /**
         * Get the current targeted audio stream's level.
         */
        @Invokable(description = R.string.ChangeAudio_get)
        public void get() {
            //noinspection ConstantConditions
            this.callPrevious(Service.ACTION_INVOKE, Invokable.results, this.service.getSystemService(AudioManager.class).getStreamVolume(this.stream));
        }

        /**
         * Set the targeted audio stream's level to the passed value.
         *
         * @param integer to brightness to
         */
        @Invokable(description = R.string.ChangeAudio_start,
                params_description = R.array.ChangeAudio_start,
                value = {"integer"})
        public void start(Integer integer) {
            if (this.absolute != null)
                integer = this.absolute;

            integer = integer > this.maximum ? this.maximum : integer < this.minimum ? this.minimum : integer;

            //noinspection ConstantConditions
            this.service.getSystemService(AudioManager.class)
                    .setStreamVolume(this.stream, integer, this.show_ui ?
                            AudioManager.FLAG_SHOW_UI : AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);

            if (this.show_toast)
                android.widget.Toast.makeText(this.service,
                        String.valueOf(integer),
                        android.widget.Toast.LENGTH_SHORT).show();

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

        /**
         * Add the passed value to the targeted audio stream\'s level.
         *
         * @param integer to add
         */
        @Invokable(description = R.string.ChangeAudio_update,
                params_description = R.array.ChangeAudio_update,
                value = {"integer"})
        public void update(Integer integer) {
            //noinspection ConstantConditions
            this.start(integer + this.service.getSystemService(AudioManager.class).getStreamVolume(this.stream));
        }
    }
}
