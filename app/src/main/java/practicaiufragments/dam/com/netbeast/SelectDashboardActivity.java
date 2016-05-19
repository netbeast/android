package practicaiufragments.dam.com.netbeast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Cayetano Rodríguez Medina on 19/5/16.
 */
public class SelectDashboardActivity extends Activity {

    private String IP;
    private String port;
    private UDPMessenger udp;

    TextView tv;

    // Progress dialog
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_dashboard_activity);

        udp = new UDPMessenger(this);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Looking for dashboards...");
        pDialog.setCancelable(false);

        // Show dialog "Looking for dashboards"
        showpDialog();
        // Send multicast message
        //if (udp.sendMessage("hi"))
            // Wait for responses
            //udp.startMessageReceiver();
        //else
        // If wifi is not connected show toast
            //Toast.makeText(getApplicationContext(),
            //        "Sorry! You need to be in a WiFi network", Toast.LENGTH_SHORT).show();
        udp.startMessageReceiver();
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        hidepDialog();
                    }
                }, 1500);

        // Let's create/get global params
        Global g = Global.getInstance();
        tv = (TextView)findViewById(R.id.dashboard_tv);
        tv.setText(g.getIP()+":"+g.getPort());
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void dashboardSelected(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void cloudDashboard(View v){
        Global g = Global.getInstance();
        // dashboard cloud address
        g.setIP("dashboard.827722d5.svc.dockerapp.io");
        g.setPort("");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
