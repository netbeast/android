package dam.com.netbeast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import dam.com.netbeast.netbeast.R;


/**
 * Created by Cayetano Rodríguez Medina on 3/5/16.
 */
public class InstallActivity extends AppCompatActivity{

    private NavigationViewListener navigationViewListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.install_activity);

        Button exploreBt = (Button) findViewById(R.id.installButton1);
        Button gitBt = (Button) findViewById(R.id.installButton2);

        exploreBt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        ((Button)v.findViewById(R.id.installButton1)).setBackgroundColor(getResources().getColor(R.color.pressedButton));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        exploreInstallableApps(v);

                        ((Button)v.findViewById(R.id.installButton1)).setBackgroundColor(getResources().getColor(R.color.text));
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        gitBt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        ((Button)v.findViewById(R.id.installButton2)).setBackgroundColor(getResources().getColor(R.color.pressedButton));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        gitInstallApps(v);

                        ((Button)v.findViewById(R.id.installButton2)).setBackgroundColor(getResources().getColor(R.color.text));
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

        navigationViewListener = new NavigationViewListener(this);
        getSupportActionBar().setTitle(R.string.install_activity_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void exploreInstallableApps(View v) {
        Intent intent = new Intent(this, ExploreInstallableAppsActivity.class);
        startActivity(intent);
    }

    public void gitInstallApps(View view){
        Intent intent = new Intent(this, GitInstallActivity.class);
        startActivity(intent);
    }

}
