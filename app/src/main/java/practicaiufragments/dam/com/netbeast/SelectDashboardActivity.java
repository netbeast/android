package practicaiufragments.dam.com.netbeast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
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
    LinearLayout l;
    ListView listView;
    CustomListAdapter adapter;
    ArrayList<String> lista;

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
        final int N = 10;
        listView = (ListView) findViewById(R.id.list);

        lista = new ArrayList<>();
        adapter = new CustomListAdapter(this,lista);

        listView.setAdapter(adapter);
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        hidepDialog();
                    }
                }, 1500);

        for (int i = 0; i < N; i++) {
            if (lista == null)
                lista = new ArrayList<>();
            lista.add("Dashboard " + i);
            adapter.notifyDataSetChanged();
        }
    }



    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
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
