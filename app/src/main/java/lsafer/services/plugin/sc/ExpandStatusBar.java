package lsafer.services.plugin.sc;

import android.Manifest;
import android.annotation.SuppressLint;
import lsafer.services.annotation.Controller;
import lsafer.services.annotation.Entry;
import lsafer.services.annotation.Invokable;
import lsafer.services.plugin.R;
import lsafer.services.util.Process;
import lsafer.services.util.Service;

import java.lang.reflect.InvocationTargetException;

/**
 * Expand/Collapse notification panel on call.
 *
 * @author LSaferSE
 * @version 4 beta (07-Sep-2019)
 * @since 22-Jun-19
 * @see Manifest.permission#EXPAND_STATUS_BAR
 */
@Service.Defaults(process = ExpandStatusBar.ExpandStatusBarProcess.class)
final public class ExpandStatusBar extends Service<ExpandStatusBar.ExpandStatusBarProcess> {
    /**
     * Expand/Collapse notification panel on call.
     */
    @Controller(description = R.string.ExpandStatusBar)
    final public static class ExpandStatusBarProcess extends Process<ExpandStatusBar> {
        /**
         * Absolute value to use on calling {@link #start(Boolean)}.
         */
        @Entry(description = R.string.ExpandStatusBar_absolute,
                values_description = R.array.ExpandStatusBar_absolute,
                value = {"null"})
        public Boolean absolute = null;

        /**
         * Expand the notification panel.
         *
         * @param bool whether the notification panel should be expanded or not
         */
        @SuppressLint("WrongConstant, PrivateApi")
        @Invokable(description = R.string.ExpandStatusBar_start,
                params_description = R.array.ExpandStatusBar_start,
                value = {"boolean"})
        public void start(Boolean bool) {
            if (this.absolute != null)
                bool = this.absolute;

            try {
                //noinspection SpellCheckingInspection,
                Class.forName("android.app.StatusBarManager")
                        .getMethod(bool ? "expandNotificationsPanel" : "collapseNotificationsPanel")
                        .invoke(this.service.getSystemService("statusbar"));
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
}
