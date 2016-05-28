package dam.com.netbeast;

import android.graphics.Bitmap;
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
    private Boolean installed;

    private String aux;
    private String url;


    public App (String name) {
        this.name = name;
        installed = true;
        logoBitmap = null;
        calculateLogoUrl();
        this.full_name = null;
    }

    public App (String name, Boolean installed, String full_name) {
        this.name = name;
        this.installed = installed;
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

    public Boolean getInstalled() { return installed; }

    public void setInstalled(Boolean installed) { this.installed = installed; }

    private String calculateLogoUrl () {
        aux = null;
        final Global g = Global.getInstance();
        if (full_name != null) {
            url = "https://raw.githubusercontent.com/" + full_name + "/master/";
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
                            aux = "http://" + g.getIP() + ":" + g.getPort() + "/api/apps/" + name + "/logo";
                            logoURL = aux;
                        }
                    } catch (JSONException e) {
                        Log.e("ERROR", e.getMessage(), e);
                    }
                }
            });
        }
        else {
            aux = "http://" + g.getIP() + ":" + g.getPort() + "/api/apps/" + name + "/logo";
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
