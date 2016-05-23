package practicaiufragments.dam.com.netbeast;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Alejandro Rodríguez Calzado on 23/05/16.
 */
public class NavigationViewListener implements NavigationView.OnNavigationItemSelectedListener {
    private AppCompatActivity activity;

    private String IP;
    private String port;
    private String urlGetAllApps;
    private String urlGetApps;
    private String urlGetPlugins;
    private String urlGetActivities;

    public NavigationViewListener (final AppCompatActivity activity) {
        this.activity = activity;

        // Toolbar with the menu
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);

        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.mipmap.logo);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // onClick method to show the menu
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        NavigationView navigationView = (NavigationView) activity.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Let's create/get global params
        Global g = Global.getInstance();
        IP = g.getIP();
        port = g.getPort();

        // The default activity to launch is explore
        Intent intent = new Intent(activity, ExploreActivity.class);
        Bundle b = new Bundle();

        // Menu options
        switch (item.getItemId()) {
            // Case Apps
            case R.id.nav_apps:
                urlGetApps = "http://" + IP + ":" + port + "/api/apps";
                b.putString("url", urlGetApps);
                b.putString("title", "Apps");
                intent.putExtras(b);
                activity.startActivity(intent);
                break;
            // Case Plugins
            case R.id.nav_plugins:
                urlGetPlugins = "http://" + IP + ":" + port + "/api/plugins";
                b.putString("url", urlGetPlugins);
                b.putString("title", "Plugins");
                intent.putExtras(b);
                activity.startActivity(intent);
                break;
            // Case Activities
            case R.id.nav_activities:
                urlGetActivities = "http://" + IP + ":" + port + "/api/activities";
                b.putString("url", urlGetActivities);
                b.putString("title", "Activities");
                intent.putExtras(b);
                activity.startActivity(intent);
                break;
            // Case Install
            case R.id.nav_install:
                // Launch install activity instead of explore
                Intent install_intent = new Intent(activity, InstallActivity.class);
                activity.startActivity(install_intent);
                break;
            // Case Remove
            case R.id.nav_remove:
                urlGetAllApps = "http://" + IP + ":" + port + "/api/modules";
                b.putString("url", urlGetAllApps);
                b.putString("title", "Remove");
                intent.putExtras(b);
                activity.startActivity(intent);
                break;

            // Example options for other sections in the menu
            case R.id.nav_twitter:

                break;
            case R.id.nav_slack:

                break;
        }

        DrawerLayout drawer = (DrawerLayout) activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

