package dam.com.netbeast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Cayetano Rodríguez Medina on 26/4/16.
 */

// Class that includes all global application parameters
public class Global {
    private static Global instance;

    // IP and port where the dashboard is running
    private String IP;
    private String port;
    private JSONArray dashboards;

    public void setIP (String ip) { this.IP = ip; }

    public String getIP () { return this.IP; }

    public void setPort (String port) { this.port = port; }

    public String getPort () { return this.port; }

    public void addDashboard (String ip, String port) {
        if (dashboards == null)
            dashboards = new JSONArray();

        JSONObject dash = new JSONObject();
        try {
            dash.put("ip", ip);
            dash.put("port", port);
        }catch (JSONException e){
            e.printStackTrace();
        }
        dashboards.put(dash);
    }

    public JSONArray getDashboards() {
        return this.dashboards;
    }

    public void clearDashboards() {
        dashboards = null;
    }

    // Singleton implementation
    public static synchronized Global getInstance() {
        if (instance == null) {
            instance = new Global();
        }
        return instance;
    }

    private Global() {
    }
}
