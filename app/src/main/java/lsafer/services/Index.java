package lsafer.services;

import lsafer.services.io.Profile;
import lsafer.services.io.Task;
import lsafer.services.part.bg.OnBroadcast;
import lsafer.services.part.bg.OnEdgeGesture;
import lsafer.services.part.pr.Spam;
import lsafer.services.part.pr.Switch;
import lsafer.services.part.sc.AutoBrightness;
import lsafer.services.part.sc.ChangeAudio;
import lsafer.services.part.sc.ChangeBrightness;
import lsafer.services.part.sc.ExpandNotificationPanel;
import lsafer.services.part.sc.RunProfile;
import lsafer.services.part.sc.ToastMessage;
import lsafer.services.part.sc.Vibrate;
import lsafer.services.part.xy.Slide;
import lsafer.services.part.xy.Swipe;

/**
 * plugin configuration class
 * includes all indexing information
 * of what's the full name of all classes
 * included in this.
 *
 * @author LSaferSE
 * @version 1 alpha (24-Jul-19)
 * @since 24-Jul-19
 */
public class Index extends lsafer.services.util.Index {

    @Override
    public Task.Part[] getTaskParts() {
        return new Task.Part[]{
                //background
                new OnEdgeGesture(),
                new OnBroadcast(),
                //process
                new Switch(),
                new Spam(),
                //script
                new AutoBrightness(),
                new ChangeAudio(),
                new ChangeBrightness(),
                new ExpandNotificationPanel(),
                new RunProfile(),
                new ToastMessage(),
                new Vibrate(),
                //xy
                new Slide(),
                new Swipe()
        };
    }

    @Override
    public Task[] getTasks() {
        return new Task[]{
        };
    }

    @Override
    public Profile[] getProfiles() {
        return new Profile[]{

        };
    }

}
