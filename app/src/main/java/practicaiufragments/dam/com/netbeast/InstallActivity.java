package practicaiufragments.dam.com.netbeast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;


/**
 * Created by Cayetano Rodríguez Medina on 3/5/16.
 */
public class InstallActivity extends AppCompatActivity{

    private NavigationViewListener navigationViewListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.install_activity);

        navigationViewListener = new NavigationViewListener(this);
        getSupportActionBar().setTitle(R.string.install_activity_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void exploreInstallableApps(View v) {
        Intent intent = new Intent(this, ExploreInstallableAppsActivity.class);
        startActivity(intent);
    }

    public void gitInstallApps(View view){
        Intent intent = new Intent(this, GitInstallActivity.class);
        startActivity(intent);
    }

}
