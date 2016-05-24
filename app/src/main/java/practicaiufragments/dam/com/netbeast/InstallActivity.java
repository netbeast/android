package practicaiufragments.dam.com.netbeast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


/**
 * Created by Cayetano Rodríguez Medina on 3/5/16.
 */
public class InstallActivity extends Activity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.install_activity);

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
