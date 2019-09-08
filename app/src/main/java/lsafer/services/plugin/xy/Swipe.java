package lsafer.services.plugin.xy;

import lsafer.services.annotation.Controller;
import lsafer.services.annotation.Entry;
import lsafer.services.annotation.Invokable;
import lsafer.services.plugin.R;
import lsafer.services.plugin.pr.Switch;
import lsafer.services.util.Process;
import lsafer.services.util.Service;

/**
 * Call the next process when swipe detected.
 *
 * @author LSaferSE
 * @version 3 beta (07-Sep-2019)
 * @since 22-Jun-19
 */
@Service.Defaults(process = Switch.SwitchProcess.class)
final public class Swipe extends Service<Swipe.SwipeProcess> {
    /**
     * Call the next process when swipe detected.
     */
    @Controller(description = R.string.Swipe)
    final public static class SwipeProcess extends Process<Swipe> {
        /**
         * Smallest length to trigger when swiped.
         */
        @Entry(description = R.string.Swipe_length,
                values_description = R.array.Swipe_length,
                value = {"50", "100", "200", "300"})
        public Integer length = 100;

        /**
         * Center to count length from.
         */
        private transient Integer center = 0;

        /**
         * Set the center value to the passed value.
         *
         * @param x value to resolve
         */
        @Invokable(description = R.string.Swipe_start,
                params_description = R.array.Swipe_start,
                value = {"x"})
        public void start(Integer x) {
            this.center = x;
        }

        /**
         * Reset center value.
         */
        @Invokable(description = R.string.Swipe_stop)
        public void stop() {
            this.center = 0;
        }

        /**
         * Call the next process. If the targeted swipe length have been detected.
         *
         * @param x value to resolve
         */
        @Invokable(description = R.string.Swipe_update,
                params_description = R.array.Swipe_update,
                value = {"x"})
        public void update(Integer x) {
            if (this.length > 0 ?
                    this.center - x >= this.length :
                    this.center - x <= this.length)
                this.callNext(Service.ACTION_INVOKE, Invokable.update);
        }
    }
}
