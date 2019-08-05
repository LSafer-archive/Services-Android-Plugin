package lsafer.services.part.sc;

import lsafer.services.annotation.Description;
import lsafer.services.annotation.Permissions;
import lsafer.services.annotation.Values;
import lsafer.services.io.Profile;
import lsafer.services.io.Profiles;
import lsafer.services.io.Task;
import lsafer.services.util.Arguments;

/**
 * launch all providers when get called.
 *
 * @author LSafer
 * @version 3
 * @since 10 Jun 2019
 */
@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess"})
@Permissions({})
@Description("launch all providers when get called.")
final public class RunProfile extends Task.Part {

    /**
     * directory where the daemons have stored.
     */
    @Values({"%folder|profile directory"})
    @Description("name where the profile have stored.")
    public String profile = "";

    @Override
    public Arguments invoke(String name, String[] expected, Object... arguments) {
        Arguments passed = new Arguments(arguments);

        if (passed.context != null)
            Profiles.$.<Profile>putIfAbsent(this.profile, n ->
                    new Profile()
                            .<Profile>remote(Profiles.$.remote().child(this.profile))
                            .<Profile>load()
                            .initialize(passed.context))
                    .run(name, passed);

        return new Arguments();
    }

}
