package practicaiufragments.dam.com.netbeast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

/**
 * Created by Cayetano Rodríguez Medina on 29/4/16.
 */
public class GitInstallActivity extends Activity{
    private String IP;
    private String urlPostApp;

    private static String TAG = GitInstallActivity.class.getSimpleName();

    private EditText editText;
    private Button installButton;

    // Progress dialog
    private ProgressDialog pDialog;

    private HashMap<String, String> mRequestParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.git_install_activity);

        // Let's create/get global params
        Global g = Global.getInstance();
        IP = g.getIP();
        urlPostApp = "http://" + IP + ":8000/api/apps";

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        editText = (EditText) findViewById(R.id.editText);

        mRequestParams = new HashMap<>();

        installButton = (Button) findViewById(R.id.installButton);
        installButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get url from text box
                String url = editText.getText().toString();
                // Use this url to post params
                mRequestParams.put("url", url);
                // Make post request
                installApp();
            }
        });

    }

    public void installApp() {

        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.POST, urlPostApp,
                new JSONObject(mRequestParams),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "ERROR:  " + response.toString());

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

