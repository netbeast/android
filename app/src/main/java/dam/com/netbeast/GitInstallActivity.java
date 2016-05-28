package dam.com.netbeast;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import dam.com.netbeast.netbeast.R;

/**
 * Created by Cayetano Rodríguez Medina on 29/4/16.
 */
public class GitInstallActivity extends AppCompatActivity{
    private String IP;
    private String port;
    private String urlPostApp;

    private static String TAG = GitInstallActivity.class.getSimpleName();

    private EditText editText;
    private Button installButton;

    // Progress dialog
    private ProgressDialog pDialog;

    private HashMap<String, String> mRequestParams;

    private NavigationViewListener navigationViewListener;

    private static final int SOCKET_TIMEOUT_MS = 30000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.git_install_activity);

        navigationViewListener = new NavigationViewListener(this);
        getSupportActionBar().setTitle(R.string.install_activity_title);

        // Let's create/get global params
        Global g = Global.getInstance();
        IP = g.getIP();
        port = g.getPort();
        urlPostApp = "http://" + IP + ":" + port + "/api/apps";

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        editText = (EditText) findViewById(R.id.editText);

        mRequestParams = new HashMap<>();

        installButton = (Button) findViewById(R.id.installButton);
        installButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        ((Button)v.findViewById(R.id.installButton)).setBackgroundColor(getResources().getColor(R.color.pressedButton));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        // Get url from text box
                        String url = editText.getText().toString();
                        // Use this url to post params
                        mRequestParams.put("url", url);
                        // Make post request
                        installApp();

                        ((Button)v.findViewById(R.id.installButton)).setBackgroundColor(getResources().getColor(R.color.text));
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void installApp() {

        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST, urlPostApp,
                new JSONObject(mRequestParams),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        new Timer().schedule(
                                new TimerTask() {
                                    @Override
                                    public void run() {
                                        hidepDialog();
                                    }
                                }, 500);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(),
                //        "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                new Timer().schedule(
                        new TimerTask() {
                            @Override
                            public void run() {
                                hidepDialog();
                            }
                        }, 500);
            }
        });

        req.setRetryPolicy(new DefaultRetryPolicy(
                SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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

