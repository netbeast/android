package practicaiufragments.dam.com.netbeast;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TextView;

import static practicaiufragments.dam.com.netbeast.R.id.tv_dashboardip;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private String IP;
    private String port;
    private String urlGetAllApps;
    private String urlGetApps;
    private String urlGetPlugins;
    private String urlGetActivities;

    private UDPMessenger udp;
    private static String TAG = MainActivity.class.getSimpleName();

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

        tv_ip = (TextView)findViewById(tv_dashboardip);
        // if we have connected to the cloud dashboard, show "Cloud" instead of an IP
        if(IP.equals("dashboard.827722d5.svc.dockerapp.io"))
            tv_ip.setText("Cloud");
        else
            tv_ip.setText(IP);
        tv_ip.setTextColor(Color.parseColor("#33cc33"));

        udp = new UDPMessenger(this);

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

    /* This is the settings button. If you want to display it uncomment this method and add
       the settings item in main_menu.xml
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
*/

    public void exploreApps(View v) {
        Intent intent = new Intent(this, ExploreActivity.class);

        Global g = Global.getInstance();
        IP = g.getIP();
        port = g.getPort();
        urlGetAllApps = "http://" + IP + ":" + port + "/api/modules";
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
        port = g.getPort();

        // The default activity to launch is explore
        Intent intent = new Intent(this, ExploreActivity.class);
        Bundle b = new Bundle();

        // Menu options
        switch(item.getItemId()) {
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

