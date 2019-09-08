package lsafer.services.plugin.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;
import lsafer.services.plugin.R;
import lsafer.services.util.Service;
import lsafer.view.Refreshable;

/**
 * Activity to manage application's permissions.
 *
 * @author LSaferSE
 * @version 2 beta (08-Sep-2019)
 * @since 11 Jun 2019
 */
public class PermissionsActivity extends AppCompatActivity implements Refreshable {
    /**
     * Setup views with data.
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public void refresh() {
        //Plugin
        this.<CheckBox>findViewById(R.id.ignoreBatteryOptimization).setChecked(
                this.getSystemService(PowerManager.class).isIgnoringBatteryOptimizations(this.getPackageName())
        );
        this.<CheckBox>findViewById(R.id.foregroundService).setChecked(
                Build.VERSION.SDK_INT < Build.VERSION_CODES.P ||
                this.checkSelfPermission(Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED
        );
        this.<CheckBox>findViewById(R.id.invoker).setChecked(
                this.checkSelfPermission(Service.PERMISSION_INVOKER) == PackageManager.PERMISSION_GRANTED
        );
        //Processes
        this.<CheckBox>findViewById(R.id.displayOverOtherApps).setChecked(
                this.checkSelfPermission(Manifest.permission.SYSTEM_ALERT_WINDOW) == PackageManager.PERMISSION_DENIED
        );
        this.<CheckBox>findViewById(R.id.writeSystemSettings).setChecked(
                this.checkSelfPermission(Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_DENIED
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_permissions);
        this.refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.refresh();
    }

    /**
     * ask system for a permission depending on the
     * view's id that have called this method.
     *
     * @param view that have called this method
     */
    @SuppressLint("BatteryLife")
    public void _request(View view) {
        this.refresh();
        switch (view.getId()) {
            //Plugin
            case R.id.ignoreBatteryOptimization:
                this.startActivity(new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).setData(Uri.parse("package:" + getPackageName())));
                break;
            case R.id.foregroundService:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    this.requestPermissions(new String[]{Manifest.permission.FOREGROUND_SERVICE}, 611561551);
                break;
            case R.id.invoker:
                this.requestPermissions(new String[]{Service.PERMISSION_INVOKER}, 64198161);
                break;
            //Processes
            case R.id.displayOverOtherApps:
                this.startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).setData(Uri.parse("package:" + getPackageName())));
                break;
            case R.id.writeSystemSettings:
                this.startActivity(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).setData(Uri.parse("package:" + getPackageName())));
                break;
        }
    }
}
