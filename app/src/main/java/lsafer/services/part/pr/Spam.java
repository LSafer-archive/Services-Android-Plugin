package lsafer.services.part.pr;

import lsafer.services.annotation.Description;
import lsafer.services.annotation.Parameters;
import lsafer.services.annotation.Permissions;
import lsafer.services.annotation.Values;
import lsafer.services.io.Task;
import lsafer.services.text.Run;
import lsafer.services.util.Arguments;

/**
 * do the task when double click detected.
 *
 * @author LSaferSE
 * @version 2
 * @since 22 Jun 2019
 */
@Description("do the task when double click detected.")
@Permissions({})
@SuppressWarnings("WeakerAccess")
final public class Spam extends Task.Part {

    /**
     * duration to wait until next click happen.
     * <p>
     * measurement in milliseconds
     */
    @Values("%integer|custom")
    @Description("duration to wait until next click happen (in milliseconds)")
    public Integer duration = 1000;

    /**
     * spams number to run.
     */
    @Values("%integer|custom")
    @Description("spams number to run.")
    public Integer spams = 2;

    /**
     * id for allowing the countdown to reset click counts.
     * <p>
     * to avoid old counters to reset
     */
    private Integer $id = 0;

    /**
     * spams count under the given duration.
     *
     * @see #duration
     */
    private Integer $spams = 0;

    /**
     * check if the spams matches the spams target count.
     *
     * @param arguments no-use
     */
    @Parameters(input = {}, output = {})
    @Description("check if the spams matches the spams target count.")
    public void start(Arguments arguments) {
        int id = ++this.$id;
        this.$spams++;

        if (this.$spams.equals(this.spams)) {
            this.$spams = 0;
            this.next(Run.start, null, arguments);
        }

        new Thread(() -> {
            try {
                Thread.sleep(this.duration);
                if (id == this.$id)
                    this.$spams = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
