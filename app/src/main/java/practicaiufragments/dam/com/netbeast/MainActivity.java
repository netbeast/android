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

public class MainActivity extends AppCompatActivity {


    private String IP;
    private String port;
    private String urlGetAllApps;

    private UDPMessenger udp;
    private static String TAG = MainActivity.class.getSimpleName();

    // Progress dialog
    private ProgressDialog pDialog;

    private TextView tv_ip;

    private NavigationViewListener navigationViewListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_main_activity);

        // Let's create/get global params
        Global g = Global.getInstance();
        IP = g.getIP();

        navigationViewListener = new NavigationViewListener(this);

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
}

