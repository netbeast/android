package practicaiufragments.dam.com.netbeast;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by Cayetano Rodríguez Medina on 19/5/16.
 */
public class SelectDashboardActivity extends Activity {

    private String IP;
    private String port;
    private UDPMessenger udp;
    private Boolean dashboardsLoaded;

    ListView listView;
    CustomListAdapter adapter;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_dashboard_activity);

        udp = new UDPMessenger(this);


        listView = (ListView) findViewById(R.id.list);

        list = new ArrayList<>();
        adapter = new CustomListAdapter(this,list);

        listView.setAdapter(adapter);

        // Send multicast message
        if (udp.sendMessage("hi")) {
            // If there isn't any problem, wait for responses
            udp.startMessageReceiver();

            // After one second, stop waiting for responses
            new Timer().schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            udp.stopMessageReceiver(new DataCallback() {
                                @Override
                                public void onSuccess(JSONObject result) {
                                    fillList();
                                }
                            });
                        }
                    }, 500);
        }


    }

    public void fillList(){
        Global g = Global.getInstance();
        JSONArray dashboards = g.getDashboards();

        if (dashboards != null)
            // Go over the JSONArray dashboards that has all dashboards as JSON Objects
            for (int i = 0; i < dashboards.length(); i++) {
                // Create the list if it's not created
                if (list == null)
                    list = new ArrayList<>();

                try {
                    // Get the JSONObject for position i in the dashboards list
                    JSONObject dash = (JSONObject) dashboards.get(i);
                    IP = dash.getString("ip");
                    port = dash.getString("port");
                    // Add to the list in screen the dashboard in position i, as IP:port
                    list.add(IP + ":" + port);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
    }

    // Method that will be called in the onClick of the "Use dashboard in the cloud" button
    public void cloudDashboard(View v){
        Global g = Global.getInstance();
        // dashboard cloud address
        g.setIP("dashboard.827722d5.svc.dockerapp.io");
        g.setPort("");
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
