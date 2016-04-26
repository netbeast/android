package practicaiufragments.dam.com.netbeast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
public class ExploreActivity extends Activity{
    private String IP;
    private String urlGetAllApps;

    private static String TAG = ExploreActivity.class.getSimpleName();

    // Progress dialog
    private ProgressDialog pDialog;

    private ArrayList<String> jsonResponse;

    private Context mContext;

    private LinearLayout slnLay;
    private ImageButton bt;
    private TextView tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scroll);

        // Let's create/get global params
        Global g = Global.getInstance();
        IP = g.getIP();
        urlGetAllApps = "http://" + IP + ":8000/api/apps";


        jsonResponse = new ArrayList<>();

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        exploreApps();

        fillRows();
    }

    public void fillRows() {
        mContext = ExploreActivity.this;
        LinearLayout childln;

        slnLay = (LinearLayout) findViewById(R.id.scrollLinearLayout);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View convertView = inflater.inflate(R.layout.custom_row, null);

        for (int i = 0; i < jsonResponse.size(); i++) {

            childln = (LinearLayout) convertView.findViewById(R.id.linearchild);
            bt = new ImageButton(this);
            bt = (ImageButton) convertView.findViewById(R.id.button);
            //im.setImageResource();

            childln.addView(bt);

            tv = new TextView(this);
            tv = (TextView) convertView.findViewById(R.id.text);
            tv.setText(jsonResponse.get(i));

            childln.addView(tv);

            /*bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                }
            });*/
            slnLay.addView(childln);
        }

    }

    public void exploreApps() {

        showpDialog();

        JsonArrayRequest req = new JsonArrayRequest(urlGetAllApps,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject app = (JSONObject) response.get(i);
                                String name = app.getString("name");
                                jsonResponse.add(name);
                                Log.d(TAG, name);
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
}

