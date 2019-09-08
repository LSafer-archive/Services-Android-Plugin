package lsafer.services.plugin.pr;

import android.annotation.SuppressLint;
import android.content.Context;
import lsafer.services.annotation.Controller;
import lsafer.services.annotation.Invokable;
import lsafer.services.plugin.R;
import lsafer.services.util.Arguments;
import lsafer.services.util.Process;
import lsafer.services.util.Service;
import lsafer.util.HashStructure;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom switch process. To allow the advanced user to switch values between processes.
 *
 * @author LSaferSE
 * @version 3 alpha (07-Sep-2019)
 * @since 22-Jun-19
 */
@SuppressWarnings("WeakerAccess")
@Service.Defaults(process = Switch.SwitchProcess.class)
final public class Switch extends Service<Switch.SwitchProcess> {
    /**
     * Switch configurations.
     *
     * <br><br><b>example:</b>
     * <pre>
     *     {<I><font color="gray"><----- switch root</font></I>
     *          "{@link lsafer.services.annotation.Invokable#start start}":{<I><font color="gray"><----- configuration 0</font></I>
     *              "{@link Configuration#invokes invokes}":{
     *                  1: {<I><font color="gray"><----- invoke 0</font></I>
     *                      "{@link Configuration.Invoke#method method}":"{@link lsafer.services.annotation.Invokable#start start}",
     *                      "{@link Configuration.Invoke#arguments arguments}":{"integer":0, ...},
     *                      "{@link Configuration.Invoke#pass_passed pass_passed}":true
     *                  }
     *              }
     *          },
     *          "{@link lsafer.services.annotation.Invokable#stop stop}":{<I><font color="gray"><----- configuration 1</font></I>
     *              "{@link Configuration#invokes invokes}":{
     *                  0: {<I><font color="gray"><----- invoke 0</font></I>
     *                      "{@link Configuration.Invoke#method method}":"{@link lsafer.services.annotation.Invokable#stop stop}",
     *                      "{@link Configuration.Invoke#action action}":"{@link lsafer.services.util.Service#ACTION_SHUTDOWN lsafer.services.intent.action.SHUTDOWN}",
     *                      "{@link Configuration.Invoke#arguments arguments}":{"integer":0, ...},
     *                      "{@link Configuration.Invoke#pass_passed pass_passed}":true
     *                  },
     *                  5: {<I><font color="gray"><----- invoke 1</font></I>
     *                      "{@link Configuration.Invoke#method method}":"{@link lsafer.services.annotation.Invokable#stop stop}",
     *                      "{@link Configuration.Invoke#action action}":"{@link lsafer.services.util.Service#ACTION_SHUTDOWN lsafer.services.intent.action.SHUTDOWN}",
     *                      "{@link Configuration.Invoke#arguments arguments}":{"integer":0, ...},
     *                      "{@link Configuration.Invoke#pass_passed pass_passed}":false
     *                  },
     *              }
     *          }
     *     }
     * </pre>
     */
    final public static class Configuration extends HashStructure {
        /**
         * Targeted methods to be invoked using {@link lsafer.services.io.Chain#call(Context, int, String, String, Arguments)}.
         */
        @SuppressLint("UseSparseArrays")
        public Map<Integer, Invoke> invokes = new HashMap<>();

        /**
         * A structure to manage targeted invoke's data.
         */
        public static class Invoke extends HashStructure {
            /**
             * Action to be passed to the service.
             */
            public String action = Service.ACTION_INVOKE;
            /**
             * Arguments to be passed to the targeted method.
             */
            public Arguments arguments = new Arguments();
            /**
             * Method to be invoked.
             */
            public String method = Invokable.start;
            /**
             * Whether the switch shall pass the passed arguments or not.
             */
            public Boolean pass_passed = false;
        }
    }

    /**
     * Custom switch process. To allow the advanced user to switch values between processes.
     */
    @Controller(description = R.string.Switch)
    final public static class SwitchProcess extends Process<Switch> {

        @Override
        public void invoke(String target, Arguments arguments) {
            Configuration configuration = this.get(Configuration.class, target, Configuration::new);

            for (Integer key : configuration.invokes.keySet()) {
                Configuration.Invoke invoke = configuration.get(Configuration.Invoke.class, key);

                this.chain.call(this.service, key, invoke.action, invoke.method, new Arguments(invoke.pass_passed ? arguments : new Object[]{}).putAll(invoke.arguments));
            }
        }
    }
}
