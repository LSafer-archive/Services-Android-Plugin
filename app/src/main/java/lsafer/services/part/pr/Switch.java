package lsafer.services.part.pr;

import lsafer.services.annotation.Description;
import lsafer.services.annotation.Permissions;
import lsafer.services.io.Task;
import lsafer.services.util.Arguments;
import lsafer.util.AbstractStructure;

/**
 * run on custom times and with custom arguments.
 *
 * @author LSaferSE
 * @version 2
 * @since 22-Jun-19
 */
@SuppressWarnings("WeakerAccess")
@Description("run on custom times and with custom arguments.")
@Permissions({})
final public class Switch extends Task.Part {

    @Override
    public Arguments invoke(String name, String[] expected, Object... arguments) {
        Configuration configuration = this.get(Configuration.class, name);

        if (configuration != null) {
            Arguments pass = new Arguments(configuration.pass_passed ? arguments : new Object[0]).putAll(configuration.arguments);

            for (String target : configuration.targets)
                configuration.putIfAbsent("returns", k -> this.next(target, expected, pass));

            return new Arguments(configuration.returns);
        }

        return new Arguments();
    }

    /**
     * switch configurations.
     * <p>
     * do json like :
     * <pre>
     *     {<I><font color="gray"><----- switch root</font></I>
     *          "{@link Task.Part#class_name class_name}":...,
     *          "{@link Task.Part#apk_path apk_path}":...,
     *          "{@link lsafer.services.text.Run#start start}":{<I><font color="gray"><----- configuration 0</font></I>
     *              "{@link Configuration#targets targets}":["{@link lsafer.services.text.Run#start start}", ...],
     *              "{@link Configuration#arguments arguments}":{"{@link Arguments#integer integer}":0, ...},
     *              "{@link Configuration#pass_passed pass_passed}":true
     *              "{@link Configuration#returns returns}:{"{@link Arguments#string string}":"blocked", ...}"
     *          },
     *          "{@link lsafer.services.text.Run#stop stop}":{<I><font color="gray"><----- configuration 1</font></I>
     *              "{@link Configuration#targets targets}":["{@link lsafer.services.text.Run#stop stop}", ...],
     *              "{@link Configuration#arguments arguments}":{"{@link Arguments#integer integer}":1, ...},
     *              "{@link Configuration#pass_passed pass_passed}":false
     *              "{@link Configuration#returns returns}:null"
     *          },
     *          ...
     *     }
     * </pre>
     */
    public static class Configuration extends AbstractStructure {
        /**
         * arguments to pass.
         */
        public Arguments arguments = new Arguments();
        /**
         * whether the switch shall pass the passed arguments or not.
         */
        public Boolean pass_passed = true;
        /**
         * arguments to return.
         * <p>
         * null if you want to return from first target
         */
        public Object returns;
        /**
         * targeted methods to invoke in {@link Task.Part#next(String, String[], Object...)}
         */
        public String[] targets = {};
    }

}