package practicaiufragments.dam.com.netbeast;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
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

public class MainActivity extends FragmentActivity {


    private String IP;
    private String urlGetOneApp;
    private String urlGetAllApps;

    private static String TAG = MainActivity.class.getSimpleName();
    private String jsonResponse = "";

    // Progress dialog
    private ProgressDialog pDialog;

    private TextView tv_ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Let's create/get global params
        Global g = Global.getInstance();
        IP = g.getIP();
        urlGetOneApp = "http://" + IP + ":8000/api/app/";
        urlGetAllApps = "http://" + IP + ":8000/api/apps";


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

    public void exploreOneApp (View v, String app) {
        showpDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                                                            urlGetOneApp + app,
                                                            (String)null,
                                                            new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG,response.toString());

                try {
                    String name = response.getString("name");

                    jsonResponse = "";
                    jsonResponse += "Name: " + name + "\n\n";


                    Log.d(TAG, jsonResponse);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
            }
        });

        // Adding request to request queue
        QueueController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public void exploreApps(View v) {
        Intent intent = new Intent(this, ExploreActivity.class);

        Global g = Global.getInstance();
        IP = g.getIP();
        urlGetAllApps = "http://" + IP + ":8000/api/modules";

        Bundle b = new Bundle();
        b.putString("url", urlGetAllApps);
        intent.putExtras(b);

        startActivity(intent);
    }

    public void installApps(View view){
        Intent intent = new Intent(this, InstallActivity.class);
        startActivity(intent);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void changeIp(View v) {
        DialogFragment newFragment = new ChangeIpDialog();
        newFragment.show(getFragmentManager(), "missiles");
    }
}
