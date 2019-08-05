package lsafer.services.part.sc;

import android.annotation.SuppressLint;

import java.lang.reflect.InvocationTargetException;

import lsafer.services.annotation.Description;
import lsafer.services.annotation.Parameters;
import lsafer.services.annotation.Permissions;
import lsafer.services.io.Task;
import lsafer.services.util.Arguments;

/**
 * expand notification panel.
 *
 * @author LSaferSE
 * @version 3
 * @since 22-Jun-19
 */
@Permissions({"android.permission.EXPAND_STATUS_BAR"})
@Description("expand notification panel.")
final public class ExpandNotificationPanel extends Task.Part {

    /**
     * expand the notification panel.
     *
     * @param arguments context
     */
    @Parameters(input = {"context"}, output = {})
    @Description("expand the notification panel.")
    @SuppressLint("WrongConstant, PrivateApi")
    @SuppressWarnings("JavaReflectionMemberAccess")
    public void start(Arguments arguments) {
        try {
            //noinspection SpellCheckingInspection,
            Class.forName("android.app.StatusBarManager")
                    .getMethod("expandNotificationsPanel")
                    .invoke(arguments.context.getSystemService("statusbar"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * collapse the notification panel.
     *
     * @param arguments context
     */
    @Parameters(input = {"context"}, output = {})
    @Description("collapse the notification panel.")
    @SuppressLint("WrongConstant, PrivateApi")
    @SuppressWarnings("JavaReflectionMemberAccess")
    public void stop(Arguments arguments) {
        try {
            //noinspection SpellCheckingInspection,
            Class.forName("android.app.StatusBarManager")
                    .getMethod("collapseNotificationsPanel")
                    .invoke(arguments.context.getSystemService("statusbar"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
