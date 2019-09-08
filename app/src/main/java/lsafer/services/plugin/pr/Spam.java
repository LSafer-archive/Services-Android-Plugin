package lsafer.services.plugin.pr;

import lsafer.services.annotation.Controller;
import lsafer.services.annotation.Entry;
import lsafer.services.annotation.Invokable;
import lsafer.services.plugin.R;
import lsafer.services.util.Process;
import lsafer.services.util.Service;

/**
 * Call the next process when a spam invocation detected.
 *
 * @author LSaferSE
 * @version 3 beta (07-Sep-2019)
 * @since 22 Jun 2019
 */
@Service.Defaults(process = Spam.SpamProcess.class)
final public class Spam extends Service<Spam.SpamProcess> {
    /**
     * Call the next process when a spam invocation detected.
     */
    @Controller(description = R.string.Spam)
    final public static class SpamProcess extends Process<Spam> {
        /**
         * Duration to wait until next invocation happen. (measurement in milliseconds).
         */
        @Entry(description = R.string.Spam_duration)
        public Integer duration = 1000;

        /**
         * Required spams to trigger.
         */
        @Entry(description = R.string.Spam_spams)
        public Integer spams = 2;

        /**
         * Id for allowing the countdown to reset click counts. To avoid old counters to reset spams count.
         */
        private transient Integer id = 0;

        /**
         * Current spammed invokes count.
         */
        private transient Integer spammed = 0;

        /**
         * Check if the spams matches the spams target count.
         */
        @Invokable(description = R.string.Spam_start)
        public void start() {
            int id = ++this.id;
            this.spammed++;

            if (this.spammed.equals(this.spams)) {
                this.spammed = 0;
                this.callNext(Service.ACTION_INVOKE, Invokable.start);
            }

            new Thread(() -> {
                try {
                    Thread.sleep(this.duration);
                    if (id == this.id)
                        this.spammed = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
