package lsafer.services.part.xy;

import lsafer.services.annotation.Description;
import lsafer.services.annotation.Parameters;
import lsafer.services.annotation.Permissions;
import lsafer.services.annotation.Values;
import lsafer.services.io.Task;
import lsafer.services.text.Run;
import lsafer.services.util.Arguments;

/**
 * run when swipe detected.
 *
 * @author LSaferSE
 * @version 2
 * @since 22-Jun-19
 */
@Description("run when swipe detected.")
@Permissions({})
@SuppressWarnings("WeakerAccess")
final public class Swipe extends Task.Part {

    /**
     * smallest length to run when swiped.
     */
    @Values({
            "50|mother swipe",
            "100|child swipe",
            "200|super swipe",
            "300|father swipe",
            "%integer|custom"
    })
    @Description("smallest length to run when swiped.")
    public Integer length = 100;

    /**
     * center to count length from.
     */
    private Integer $center = 0;

    /**
     * set the center value to the passed value.
     *
     * @param arguments integer
     */
    @Parameters(input = {"integer"}, output = {})
    @Description("set the center value to the passed value.")
    public void start(Arguments arguments) {
        this.$center = arguments.integer;
    }

    /**
     * reset center value.
     *
     * @param arguments no-use
     */
    @Parameters(input = {}, output = {})
    @Description("reset center value.")
    public void stop(Arguments arguments) {
        this.$center = 0;
    }

    /**
     * run the task if the user had swiped the length specified.
     *
     * @param arguments integer
     */
    @Parameters(input = {"integer"}, output = {})
    @Description("run the task if the user had swiped the length specified.")
    public void update(Arguments arguments) {
        if (this.length > 0 ?
                this.$center - arguments.integer >= this.length :
                this.$center - arguments.integer <= this.length)
            this.next(Run.update, null);
    }

}
