package practicaiufragments.dam.com.netbeast;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
public class ExploreActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private String IP;
    private String port;
    private String urlGetAllApps;
    private String urlGetApps;
    private String urlGetPlugins;
    private String urlGetActivities;

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
        if (b != null) {
            url = b.getString("url");
            title = b.getString("title");
        } else
            Log.d(TAG, "Bundle is null");

        // Toolbar with the menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.mipmap.logo);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // onClick method to show the menu
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        appList = new ArrayList<>();

        textView = (TextView) findViewById(R.id.title_text);
        //textView.setText(title);
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, appList, title);
        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        exploreApps();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
                        }, 500);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Let's create/get global params
        Global g = Global.getInstance();
        IP = g.getIP();
        port = g.getPort();

        // The default activity to launch is explore
        Intent intent = new Intent(this, ExploreActivity.class);
        Bundle b = new Bundle();

        // Menu options
        switch (item.getItemId()) {
            // Case Apps
            case R.id.nav_apps:
                urlGetApps = "http://" + IP + ":" + port + "/api/apps";
                b.putString("url", urlGetApps);
                b.putString("title", "Apps");
                intent.putExtras(b);
                startActivity(intent);
                break;
            // Case Plugins
            case R.id.nav_plugins:
                urlGetPlugins = "http://" + IP + ":" + port + "/api/plugins";
                b.putString("url", urlGetPlugins);
                b.putString("title", "Plugins");
                intent.putExtras(b);
                startActivity(intent);
                break;
            // Case Activities
            case R.id.nav_activities:
                urlGetActivities = "http://" + IP + ":" + port + "/api/activities";
                b.putString("url", urlGetActivities);
                b.putString("title", "Activities");
                intent.putExtras(b);
                startActivity(intent);
                break;
            // Case Install
            case R.id.nav_install:
                // Launch install activity instead of explore
                Intent install_intent = new Intent(this, InstallActivity.class);
                startActivity(install_intent);
                break;
            // Case Remove
            case R.id.nav_remove:
                urlGetAllApps = "http://" + IP + ":" + port + "/api/modules";
                b.putString("url", urlGetAllApps);
                b.putString("title", "Remove");
                intent.putExtras(b);
                startActivity(intent);
                break;

            // Example options for other sections in the menu
            case R.id.nav_twitter:

                break;
            case R.id.nav_slack:

                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

