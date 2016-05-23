package practicaiufragments.dam.com.netbeast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Cayetano Rodríguez Medina on 21/5/16.
 */
public class DiscoveringDashboardsSplash extends Activity {

    // Duration of wait
    private final int SPLASH_DISPLAY_LENGTH = 2500;

    UDPMessenger udp;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.discovering_dashboards_splash);

        udp = new UDPMessenger(this);
        // Send multicast message
        if (udp.sendMessage("hi")) {
            // If there isn't any problem, wait for responses
            udp.startMessageReceiver();

        }
        // New Handler to start the Select Dashboard Activity
        // and close this Splash-Screen after some seconds.
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                udp.stopMessageReceiver();
                // Create an Intent that will start the Select Dashboard Activity.
                Intent intent = new Intent(DiscoveringDashboardsSplash.this, SelectDashboardActivity.class);
                DiscoveringDashboardsSplash.this.startActivity(intent);
                DiscoveringDashboardsSplash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
