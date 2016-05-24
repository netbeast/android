package practicaiufragments.dam.com.netbeast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alejandro Rodríguez Calzado on 24/04/16.
 */
public class ExploreActivity extends Activity{
    private String url;
    private String title;

    private static String TAG = ExploreActivity.class.getSimpleName();

    // Progress dialog
    private ProgressDialog pDialog;

    private TextView textView;
    private ListView listView;
    private CustomListAdapter adapter;
    private ArrayList<App> appList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore_activity);

        Bundle b = getIntent().getExtras();
        if (b!=null) {
            url = b.getString("url");
            title = b.getString("title");
        }
        else
            Log.d(TAG, "Bundle is null");

        appList = new ArrayList<>();

        textView = (TextView) findViewById(R.id.title_text);
        textView.setText(title);
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, appList, title);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        exploreApps();
    }

    public void exploreApps() {

        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject app = (JSONObject) response.get(i);
                                    String name = app.getString("name");
                                    appList.add(new App(name));
                                    Log.d(TAG, name);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(),
                                            "Error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                        new Timer().schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        hidepDialog();
                                    }
                                }, 500);

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                new Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                hidepDialog();
                            }
                        },500);
            }
        });

        // Adding request to request queue
        QueueController.getInstance().addToRequestQueue(req);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}

