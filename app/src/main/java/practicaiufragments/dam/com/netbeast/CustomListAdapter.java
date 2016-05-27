package practicaiufragments.dam.com.netbeast;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Alejandro Rodríguez Calzado on 28/04/16.
 */
public class CustomListAdapter extends BaseAdapter {
    final private Activity activity;
    private LayoutInflater inflater;
    private List<App> appItems;
    private String title;
    private String IP;
    private String port;
    private String url;
    private List<String> items;


    private HashMap<String, String> mRequestParams;

    private static String TAG = CustomListAdapter.class.getSimpleName();

    private static final int SOCKET_TIMEOUT_MS = 30000;

    public CustomListAdapter(Activity activity, List<App> appItems, String title) {
        this.activity = activity;
        this.appItems = appItems;
        this.title = title;
    }

    public CustomListAdapter(Activity activity, List<String> items) {
        this.activity = activity;
        this.appItems = null;
        this.items = items;
    }

    @Override
    public int getCount() {
        if (appItems!=null)
            return appItems.size();
        else
            return items.size();
    }

    @Override
    public Object getItem(int location) {
        if (appItems!=null)
            return appItems.get(location);
        else
            return items.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Apps list view
        if (appItems!=null) {
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
            port = g.getPort();

            mRequestParams = new HashMap<>();

            // Set the button over the apps, to INSTALL, LAUNCH, REMOVE or STOP
            switch (title) {

                case "Install":
                    bt.setVisibility(View.VISIBLE);
                    if (app.getInstalled()) {
                        bt.setImageResource(R.drawable.launch);
                        bt.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                switch(event.getAction()) {
                                    case MotionEvent.ACTION_DOWN:
                                        // PRESSED
                                        ((ImageButton)v.findViewById(R.id.button)).setColorFilter(Color.argb(100, 255, 255, 255));
                                        return true; // if you want to handle the touch event
                                    case MotionEvent.ACTION_UP:
                                        // RELEASED
                                        url = "http://" + IP + ":" + port + "/api/activities/" + app.getName();
                                        // Use this url to post params
                                        mRequestParams.put("app", app.getName());
                                        // Make post request
                                        sendPostRequest();
                                        launchWebActivity(v, app.getName());

                                        ((ImageButton)v.findViewById(R.id.button)).clearColorFilter();
                                        return true; // if you want to handle the touch event
                                }
                                return false;
                            }
                        });
                    } else {
                        bt.setImageResource(R.drawable.install);
                        bt.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                switch(event.getAction()) {
                                    case MotionEvent.ACTION_DOWN:
                                        // PRESSED
                                        ((ImageButton)v.findViewById(R.id.button)).setColorFilter(Color.argb(100, 255, 255, 255));
                                        return true; // if you want to handle the touch event
                                    case MotionEvent.ACTION_UP:
                                        // RELEASED
                                        url = "http://" + IP + ":" + port + "/api/apps";
                                        String gitUrl = "https://github.com/" + app.getFull_name();
                                        // Use this url to post params
                                        mRequestParams.put("url", gitUrl);
                                        // Make post request
                                        sendPostRequest();

                                        ((ImageButton)v.findViewById(R.id.button)).clearColorFilter();
                                        return true; // if you want to handle the touch event
                                }
                                return false;
                            }
                        });
                    }
                    break;

                case "Activities":
                    bt.setVisibility(View.VISIBLE);
                    bt.setImageResource(R.drawable.stop);
                    bt.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch(event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    // PRESSED
                                    ((ImageButton)v.findViewById(R.id.button)).setColorFilter(Color.argb(100, 255, 255, 255));
                                    return true; // if you want to handle the touch event
                                case MotionEvent.ACTION_UP:
                                    // RELEASED
                                    url = "http://" + IP + ":" + port + "/api/activities/" + app.getName();
                                    // Use this url to post params
                                    mRequestParams.put("url", url);
                                    // Make delete request
                                    sendDeleteRequest(new DataCallback() {
                                        @Override
                                        public void onSuccess(JSONObject result) {
                                            ((ExploreActivity) activity).exploreApps();
                                        }
                                    });
                                    ((ImageButton)v.findViewById(R.id.button)).clearColorFilter();
                                    return true; // if you want to handle the touch event
                            }
                            return false;
                        }
                    });
                    break;

                case "Remove":
                    bt.setVisibility(View.VISIBLE);
                    bt.setImageResource(R.drawable.remove);
                    bt.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            switch(event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    // PRESSED
                                    ((ImageButton)v.findViewById(R.id.button)).setColorFilter(Color.argb(100, 255, 255, 255));
                                    return true; // if you want to handle the touch event
                                case MotionEvent.ACTION_UP:
                                    // RELEASED
                                    url = "http://" + IP + ":" + port + "/api/apps/" + app.getName();
                                    // Make delete request
                                    sendDeleteRequest(new DataCallback() {
                                        @Override
                                        public void onSuccess(JSONObject result) {
                                            ((ExploreActivity) activity).exploreApps();
                                        }
                                    });

                                    ((ImageButton)v.findViewById(R.id.button)).clearColorFilter();
                                    return true; // if you want to handle the touch event
                            }
                            return false;
                        }
                    });
                    break;
            }

            // If you click on an app, it launches
            imgBt.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            // PRESSED
                            ((ImageButton)v.findViewById(R.id.imbutton)).setColorFilter(Color.argb(100, 255, 255, 255));
                            return true; // if you want to handle the touch event
                        case MotionEvent.ACTION_UP:
                            // RELEASED
                            url = "http://" + IP + ":" + port + "/api/activities/" + app.getName();
                            // Use the app name to post params
                            mRequestParams.put("app", app.getName());
                            // Make post request
                            sendPostRequest();
                            launchWebActivity(v, app.getName());

                            ((ImageButton)v.findViewById(R.id.imbutton)).clearColorFilter();
                            return true; // if you want to handle the touch event
                    }
                    return false;
                }
            });
        }

        // Dashboards list view
        else {

            if (inflater == null)
                inflater = (LayoutInflater) activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.dashboard_row, null);

            TextView txt = (TextView) convertView.findViewById(R.id.dashboard_text);
            txt.setText(items.get(position));
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Global g = Global.getInstance();
                    JSONArray dashboards = g.getDashboards();
                    try {
                        JSONObject dash = (JSONObject) dashboards.get(position);
                        g.setIP(dash.getString("ip"));
                        g.setPort(dash.getString("port"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(v.getContext(), MainActivity.class);
                    v.getContext().startActivity(intent);
                }
            });
        }
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

        req.setRetryPolicy(new DefaultRetryPolicy(
                SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        QueueController.getInstance().addToRequestQueue(req);
    }


    // Generic method to make a DELETE request
    public void sendDeleteRequest(final DataCallback callback) {

        StringRequest req = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "DELETE request has been made");
                        try {
                            callback.onSuccess(new JSONObject().put("response", response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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

    // Method to launch the web activity with the app name in title
    public void launchWebActivity (View view, String name)
    {
        Intent intent = new Intent(view.getContext(), WebActivity.class);
        Bundle b = new Bundle();
        b.putString("title", name);
        intent.putExtras(b);
        view.getContext().startActivity(intent);
    }
}