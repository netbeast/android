package practicaiufragments.dam.com.netbeast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Cayetano Rodríguez Medina on 4/5/16.
 */
public class ExploreInstallableAppsActivity extends Activity {
    private String url;

    private static String TAG = ExploreInstallableAppsActivity.class.getSimpleName();

    // Progress dialog
    private ProgressDialog pDialog;

    private ListView listView;
    private CustomListAdapter adapter;
    private ArrayList<App> appList;
    private String [] ignoreApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore_activity);

        url = "https://api.github.com/search/repositories?q=netbeast+language:javascript";

        appList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, appList);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        ignoreApps = new String[] {"dashboard", "api"};

        exploreApps();
    }

    public void exploreApps() {

        showpDialog();

        JsonObjectRequest req = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONArray("items");
                            Log.d(TAG, response.getJSONArray("items").toString());
                            for (int i = 0; i < items.length(); i++) {

                                JSONObject app = (JSONObject) items.get(i);
                                String name = app.getString("name");
                                String full_name = app.getString("full_name");

                                if (!contains(ignoreApps, name)) {
                                    appList.add(new App(name, full_name));
                                    Log.d(TAG, name + " || " + full_name);

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
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

    private static <String> boolean contains(final String[] array, final String v) {
        if (v == null) {
            for (final String e : array)
                if (e == null)
                    return true;
        } else {
            for (final String e : array)
                if (v.equals(e))
                    return true;
        }

        return false;
    }
}

