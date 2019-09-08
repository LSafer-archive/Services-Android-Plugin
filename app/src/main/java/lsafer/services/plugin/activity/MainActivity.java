package lsafer.services.plugin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import lsafer.services.plugin.R;

/**
 * The main menu activity.
 *
 * @author LSaferSE
 * @version 2 beta (08-Sep-19)
 * @since 28-Aug-19
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(R.style.BlackAppTheme);
        this.setContentView(R.layout.activity_main);
    }

    /**
     * Start {@link PermissionsActivity}.
     *
     * @param view no-use
     */
    public void _permissions(View view) {
        this.startActivity(new Intent(this, PermissionsActivity.class));
    }
}
