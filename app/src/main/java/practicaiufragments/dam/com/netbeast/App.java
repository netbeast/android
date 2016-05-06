package practicaiufragments.dam.com.netbeast;

import android.graphics.Bitmap;
import android.support.v7.util.AsyncListUtil;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Alejandro Rodríguez Calzado on 28/04/16.
 */
public class App {
    private String name;
    private String logoURL;
    private String full_name;
    private String logoPath;
    private Bitmap logoBitmap;

    private String aux;
    private String url;

    public App (String name) {
        this.name = name;
        logoBitmap = null;
        calculateLogoUrl();
        this.full_name = null;
    }

    public App (String name, String full_name) {
        this.name = name;
        logoBitmap = null;
        this.full_name = full_name;
        calculateLogoUrl();
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

    public Bitmap getLogoBitmap() {
        return logoBitmap;
    }

    public void setLogoBitmap(Bitmap logoBitmap) {
        this.logoBitmap = logoBitmap;
    }

    private String calculateLogoUrl () {
        aux = null;
        if (full_name != null) {
            url = "https://raw.githubusercontent.com/" + full_name + "/master/";
            //this.askLogoPath(url + "package.json", this);
            this.askLogoPath(url + "package.json", new DataCallback() {
                @Override
                public void onSuccess(JSONObject result) {
                    try {
                        logoPath = result.getString("logo");
                        if (logoPath != null) {
                            aux = url + logoPath;
                            logoURL = aux;
                            Log.d("LOGO2", aux);
                        } else {
                            aux = "http://" + Global.getInstance().getIP() + ":8000/api/apps/" + name + "/logo";
                            logoURL = aux;
                        }
                    } catch (JSONException e) {
                        Log.e("ERROR", e.getMessage(), e);
                    }
                }
            });
            /*Log.d("LOGO1", "Hello " + name + "|" + this.getLogoPath() + "|" + this.logoPath);
            if (this.logoPath != null) {
                aux = url + this.logoPath;
                Log.d("LOGO2", aux);
            }
            else
                aux = "http://" + Global.getInstance().getIP() + ":8000/api/apps/" + name + "/logo";*/
        }
        else {
            aux = "http://" + Global.getInstance().getIP() + ":8000/api/apps/" + name + "/logo";
            logoURL = aux;
            Log.d("test", aux);
        }

        return aux;
    }

    public void askLogoPath(String url, final DataCallback callback) {

        JsonObjectRequest req = new JsonObjectRequest(url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.has("logo")) {
                                //app.setLogoPath(response.getString("logo"));
                                Log.d("LOGO", response.getString("name") + " || " + response.getString("logo"));
                                callback.onSuccess(response);
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
