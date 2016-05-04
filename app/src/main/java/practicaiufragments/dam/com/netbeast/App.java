package practicaiufragments.dam.com.netbeast;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Alejandro Rodríguez Calzado on 28/04/16.
 */
public class App {
    private String name;
    private String logoURL;
    private String full_name;
    private String logoPath;

    public App (String name) {
        this.name = name;
        this.logoURL = calculateLogoUrl();
        this.full_name = null;
    }

    public App (String name, String full_name) {
        this.name = name;
        this.full_name = full_name;
        this.logoURL = calculateLogoUrl();
    }

    public String getName() {
        return name;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public String getFull_name() { return this.full_name; }

    public void setFull_name(String full_name){ this.full_name = full_name; }

    public String getLogoPath() {  return this.logoPath; }

    public void setLogoPath(String logoPath) { this.logoPath = logoPath; }

    private String calculateLogoUrl () {
        String aux = null;
        if (full_name != null) {
            String url = "https://raw.githubusercontent.com/" + full_name + "/master/";
            this.askLogoPath(url + "package.json", this);
            Log.d("LOGO1", "Hello " + name + "|" + this.getLogoPath() + "|" + this.logoPath);
            if (this.logoPath != null) {
                aux = url + this.logoPath;
                Log.d("LOGO2", aux);
            }
            else
                aux = "http://" + Global.getInstance().getIP() + ":8000/api/apps/" + name + "/logo";
        }
        else {
            aux = "http://" + Global.getInstance().getIP() + ":8000/api/apps/" + name + "/logo";
            Log.d("test", aux);
        }

        return aux;
    }

    public void askLogoPath(String url, final App app) {

        JsonObjectRequest req = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.has("logo")) {
                                app.setLogoPath(response.getString("logo"));
                                Log.d("LOGO", app.getName() + " || " + app.getLogoPath());
                            }
                            else
                                logoPath = null;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Adding request to request queue
        QueueController.getInstance().addToRequestQueue(req);

    }

}
