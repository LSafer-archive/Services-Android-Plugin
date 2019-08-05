package lsafer.services.part.xy;

import lsafer.services.annotation.Description;
import lsafer.services.annotation.Parameters;
import lsafer.services.annotation.Permissions;
import lsafer.services.annotation.Values;
import lsafer.services.io.Task;
import lsafer.services.text.Run;
import lsafer.services.util.Arguments;

/**
 * transform X/Y changes into a value
 * that well added to the current task value.
 *
 * @author LSafer
 * @version 3
 * @since 10 Jun 2019
 */
@Description("transform X/Y changes into a value that well added to the current task value.")
@Permissions({})
@SuppressWarnings(value = "WeakerAccess")
final public class Slide extends Task.Part {

    /**
     * maximum value to send to task.
     */
    @Values({
            "7|small range",
            "30|medium range",
            "255|big range",
            "%integer|custom"
    })
    @Description("maximum value to send to task.")
    public Integer maximum = 100;

    /**
     * minimum value to send to task.
     */
    @Values({
            "0|default",
            "%integer|custom"
    })
    @Description("minimum value to send to task.")
    public Integer minimum = 0;

    /**
     * sensitivity while changing value.
     */
    @Values({
            "0.08f|sensitive",
            "0.01f|low sensitivity",
            "%float|custom"
    })
    @Description("sensitivity while changing value.")
    public Float sensitivity = 0.08f;

    /**
     * center X/Y location.
     */
    private Integer $center = 0;

    /**
     * center value to calculate changes from.
     */
    private Integer $center_val = 0;

    /**
     * set the center to the passed value
     * and set the center value from task.
     *
     * @param arguments integer
     */
    @Parameters(input = {"integer"}, output = {})
    @Description("set the center to the passed value and set the center value from task.")
    public void start(Arguments arguments) {
        this.$center = arguments.integer;
        this.$center_val = this.next(Run.get, new String[]{"integer"}, arguments).integer;
    }

    /**
     * reset center and center value.
     *
     * @param arguments no-use
     */
    @Parameters(input = {}, output = {})
    @Description("reset center and center value.")
    public void stop(Arguments arguments) {
        this.$center = this.$center_val = 0;
    }

    /**
     * resolve the slide value then pass it to the task.
     *
     * @param arguments integer
     */
    @Parameters(input = {"integer"}, output = {})
    @Description("resolve the slide value then pass it to the task.")
    public void update(Arguments arguments) {
        arguments.integer = (int) (this.$center_val + (this.$center - arguments.integer) * this.sensitivity);
        arguments.integer = arguments.integer < this.minimum ? this.minimum : arguments.integer > this.maximum ? this.maximum : arguments.integer;
        this.next(Run.update, null, arguments);
    }

}
