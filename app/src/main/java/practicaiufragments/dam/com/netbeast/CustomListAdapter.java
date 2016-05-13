package practicaiufragments.dam.com.netbeast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alejandro Rodríguez Calzado on 28/04/16.
 */
public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<App> appItems;
    private String title;
    private String IP;
    private String url;


    private HashMap<String, String> mRequestParams;

    private static String TAG = CustomListAdapter.class.getSimpleName();

    public CustomListAdapter(Activity activity, List<App> appItems, String title) {
        this.activity = activity;
        this.appItems = appItems;
        this.title = title;
    }

    @Override
    public int getCount() {
        return appItems.size();
    }

    @Override
    public Object getItem(int location) {
        return appItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.custom_row, null);

        ImageButton imgBt = (ImageButton) convertView.findViewById(R.id.imbutton);
        TextView txt = (TextView) convertView.findViewById(R.id.text);
        ImageButton bt = (ImageButton) convertView.findViewById(R.id.button);

        // getting app data for the row
        final App app = appItems.get(position);

        if (app.getLogoURL() != null)
            if (app.getLogoBitmap() == null)
                // If the logo has not been downloaded yet...
                // download app logo and set it to image button
                new DownloadImageTask(imgBt, app).execute(app.getLogoURL());
            else
                // If the logo has already been downloaded...
                // set it to image button
                imgBt.setImageBitmap(app.getLogoBitmap());
        else
            imgBt.setImageResource(R.drawable.dflt);

        // set app name to textView
        txt.setText(app.getName());


        // Let's create/get global params
        Global g = Global.getInstance();
        IP = g.getIP();

        mRequestParams = new HashMap<>();

        // Set the button over the apps, to INSTALL, LAUNCH, REMOVE or STOP
        switch(title){
            case "Install":
                bt.setVisibility(View.VISIBLE);
                if (app.getInstalled()) {
                    bt.setImageResource(R.drawable.launch);
                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            url = "http://" + IP + ":8000/api/activities/" + app.getName();
                            //String gitUrl = "https://github.com/" + app.getFull_name();
                            // Use this url to post params
                            mRequestParams.put("app", app.getName());
                            // Make post request
                            sendPostRequest();
                            launchWebActivity(v, app.getName());
                        }
                    });
                }
                else {
                    bt.setImageResource(R.drawable.install);
                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            url = "http://" + IP + ":8000/api/apps";
                            String gitUrl = "https://github.com/" + app.getFull_name();
                            // Use this url to post params
                            mRequestParams.put("url", gitUrl);
                            // Make post request
                            sendPostRequest();

                        }
                    });
                }
                break;
            case "Activities":
                bt.setVisibility(View.VISIBLE);
                bt.setImageResource(R.drawable.stop);
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        url = "http://" + IP + ":8000/api/activities/" + app.getName();
                        // Use this url to post params
                        mRequestParams.put("url", url);
                        // Make post request
                        sendDeleteRequest();
                    }
                });
                imgBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                break;
            case "Remove":
                bt.setVisibility(View.VISIBLE);
                bt.setImageResource(R.drawable.remove);
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        url = "http://" + IP + ":8000/api/apps/" + app.getName();
                        // Use this url to post params
                        mRequestParams.put("url", url);
                        // Make post request
                        sendDeleteRequest();
                    }
                });
                break;
        }

        // If you click on an app, it launches
        imgBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://" + IP + ":8000/api/activities/" + app.getName();
                // Use the app name to post params
                mRequestParams.put("app", app.getName());
                // Make post request
                sendPostRequest();
                launchWebActivity(v, app.getName());
            }
        });

        return convertView;
    }


    // Generic method to make a POST request
    public void sendPostRequest() {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(mRequestParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "POST request has been made");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        QueueController.getInstance().addToRequestQueue(req);
    }


    // Generic method to make a DELETE request
    public void sendDeleteRequest() {

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE, url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "DELETE request has been made");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        QueueController.getInstance().addToRequestQueue(req);
    }
    public void launchWebActivity (View view, String name)
    {
        Intent intent = new Intent(view.getContext(), WebActivity.class);
        Bundle b = new Bundle();
        b.putString("title", name);
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}