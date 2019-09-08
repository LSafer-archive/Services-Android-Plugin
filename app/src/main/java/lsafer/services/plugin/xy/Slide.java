package lsafer.services.plugin.xy;

import lsafer.services.annotation.Controller;
import lsafer.services.annotation.Entry;
import lsafer.services.annotation.Invokable;
import lsafer.services.plugin.R;
import lsafer.services.util.Process;
import lsafer.services.util.Service;

/**
 * Transform X/Y changes into a value. Then pass it to the next process.
 *
 * @author LSafer
 * @version 4 beta (07-Sep-2019)
 * @since 10 Jun 2019
 */
@Service.Defaults(process = Slide.SlideProcess.class)
final public class Slide extends Service<Slide.SlideProcess> {
    /**
     * Transform X/Y changes into a value. Then pass it to the next process.
     */
    @Controller(description = R.string.Slide)
    final public static class SlideProcess extends Process<Slide> {
        /**
         * Sensitivity while changing value.
         */
        @Entry(description = R.string.Slide_sensitivity,
                values_description = R.array.Slide_sensitivity,
                value = {"0.08f", "0.01f"})
        public Float sensitivity = 0.08f;

        /**
         * Center X/Y location.
         */
        private transient Integer center = 0;

        /**
         * Set the center to the passed value and set the center value from task.
         *
         * @param x the center value
         */
        @Invokable(description = R.string.Slide_start,
                params_description = R.array.Slide_start,
                value = {"x"})
        public void start(Integer x) {
            this.center = x;
        }

        /**
         * Reset center and center value.
         */
        @Invokable(description = R.string.Slide_stop)
        public void stop() {
            this.center = null;
        }

        /**
         * Resolve the slide value then pass it to the task.
         *
         * @param x value to resolve
         */
        @Invokable(description = R.string.Slide_update,
                params_description = R.array.Slide_update,
                value = {"x"})
        public void update(Integer x) {
            x = (int) ((this.center - x) * this.sensitivity);
            this.callNext(Service.ACTION_INVOKE, Invokable.update, x);
        }
    }
}
