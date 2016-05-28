package dam.com.netbeast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import dam.com.netbeast.netbeast.R;

/**
 * Created by Cayetano Rodríguez Medina on 19/5/16.
 */

public class Splash extends Activity {

    // Duration of wait
    private final int SPLASH_DISPLAY_LENGTH = 4000;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_layout);

        // New Handler to start the other Splash
        // and close this Splash-Screen after some seconds.
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                // Create an Intent that will start the other Splash Activity.
                Intent intent = new Intent(Splash.this, DiscoveringDashboardsSplash.class);
                Splash.this.startActivity(intent);
                Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}