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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import static practicaiufragments.dam.com.netbeast.R.id.tv_dashboardip;

public class MainActivity extends AppCompatActivity {


    private String IP;
    private String port;
    private String urlGetAllApps;

    private static String TAG = MainActivity.class.getSimpleName();

    // Progress dialog
    private ProgressDialog pDialog;

    private NavigationViewListener navigationViewListener;

    private TextView tv_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Let's create/get global params
        Global g = Global.getInstance();
        IP = g.getIP();

        Intent intent = new Intent(Intent.ACTION_SYNC, null, this, NotificationsService.class);
        startService(intent);

        ImageButton exploreBt = (ImageButton) findViewById(R.id.imageButton);
        ImageButton installBt = (ImageButton) findViewById(R.id.imageButton2);

        exploreBt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        ((ImageButton)v.findViewById(R.id.imageButton)).setColorFilter(Color.argb(100, 255, 255, 255));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        exploreApps(v);

                        ((ImageButton)v.findViewById(R.id.imageButton)).clearColorFilter();
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        installBt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        ((ImageButton)v.findViewById(R.id.imageButton2)).setColorFilter(Color.argb(100, 255, 255, 255));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        installApps(v);

                        ((ImageButton)v.findViewById(R.id.imageButton2)).clearColorFilter();
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });


        navigationViewListener = new NavigationViewListener(this);

        tv_ip = (TextView)findViewById(tv_dashboardip);

        // If IP has a value, use this value in the text view
        if(IP!=null) {
            // if we have connected to the cloud dashboard, show "Cloud" instead of an IP
            if (IP.equals(this.getResources().getString(R.string.dashboard_cloud)))
                tv_ip.setText("Cloud");
            // if it's not the cloud dashboard, show the IP
            else
                tv_ip.setText(IP);
            // In any case, if we have a dashboard connected, change color
            tv_ip.setTextColor(Color.parseColor("#33cc33"));
        }
        // If it doesn't have a value, it will show the default text NONE

        // If we push the "choose a dashboard manually" button,
        // we should launch the dialog to change ip
        Bundle b = getIntent().getExtras();
        if (b != null) {
            if (b.getBoolean("manual")) {
                DialogFragment newFragment = new ChangeIpDialog();
                newFragment.show(getFragmentManager(), "ChangeIp");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void exploreApps(View v) {
        Intent intent = new Intent(this, ExploreActivity.class);

        Global g = Global.getInstance();
        IP = g.getIP();
        port = g.getPort();
        urlGetAllApps = "http://" + IP + ":" + port + "/api/modules";
        Bundle b = new Bundle();
        b.putString("url", urlGetAllApps);
        b.putString("title", "Explore");
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

