package practicaiufragments.dam.com.netbeast;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import static practicaiufragments.dam.com.netbeast.R.id.tv_dashboardip;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private String IP;
    private String urlGetOneApp;
    private String urlGetAllApps;
    private String urlGetApps;
    private String urlGetPlugins;
    private String urlGetActivities;

    private static String TAG = MainActivity.class.getSimpleName();
    private String jsonResponse = "";

    // Progress dialog
    private ProgressDialog pDialog;

    private TextView tv_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_main_activity);

        // Let's create/get global params
        Global g = Global.getInstance();
        IP = g.getIP();

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

        tv_ip = (TextView)findViewById(tv_dashboardip);
        tv_ip.setText(IP);
        tv_ip.setTextColor(Color.parseColor("#33cc33"));

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /*
        This is just a checking method that we can use to get responses if we don't know if
        it's working properly.
     */
    public void botonClickeado(View v) {
        final TextView mTextView = (TextView) findViewById(R.id.text);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://" + IP;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        mTextView.setText("Response is: " + response.substring(0, 500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void exploreApps(View v) {
        Intent intent = new Intent(this, ExploreActivity.class);

        Global g = Global.getInstance();
        IP = g.getIP();
        urlGetAllApps = "http://" + IP + ":8000/api/modules";
        Bundle b = new Bundle();
        b.putString("url", urlGetAllApps);
        b.putString("title", "Apps");
        intent.putExtras(b);

        startActivity(intent);
    }

    public void installApps(View view){
        Intent intent = new Intent(this, InstallActivity.class);
        startActivity(intent);
    }

    public void changeIp(View v) {
        DialogFragment newFragment = new ChangeIpDialog();
        newFragment.show(getFragmentManager(), "ChangeIp");
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Let's create/get global params
        Global g = Global.getInstance();
        IP = g.getIP();

        Intent intent = new Intent(this, ExploreActivity.class);
        Bundle b = new Bundle();

        switch(item.getItemId()) {

            case R.id.nav_apps:
                urlGetApps = "http://" + IP + ":8000/api/apps";
                b.putString("url", urlGetApps);
                b.putString("title", "Apps");
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.nav_plugins:
                urlGetPlugins = "http://" + IP + ":8000/api/plugins";
                b.putString("url", urlGetPlugins);
                b.putString("title", "Plugins");
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.nav_activities:
                urlGetActivities = "http://" + IP + ":8000/api/activities";
                b.putString("url", urlGetActivities);
                b.putString("title", "Activities");
                intent.putExtras(b);
                startActivity(intent);
                break;
            case R.id.nav_install:
                Intent install_intent = new Intent(this, InstallActivity.class);
                startActivity(install_intent);
                break;
            case R.id.nav_delete:
                urlGetAllApps = "http://" + IP + ":8000/api/modules";
                b.putString("url", urlGetAllApps);
                b.putString("title", "Delete");
                intent.putExtras(b);
                startActivity(intent);
                break;
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

