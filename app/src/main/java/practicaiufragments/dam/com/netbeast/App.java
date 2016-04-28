package practicaiufragments.dam.com.netbeast;

import android.util.Log;

/**
 * Created by Alejandro Rodríguez Calzado on 28/04/16.
 */
public class App {
    private String name;
    private String logoURL;

    public App (String name) {
        this.name = name;
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

    private String calculateLogoUrl () {
        String aux = "http://" + Global.getInstance().getIP() + ":8000/api/apps/" + name + "/logo";
        Log.d("test",aux);

        return aux;
    }
}
