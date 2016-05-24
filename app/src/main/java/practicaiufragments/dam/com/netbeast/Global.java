package practicaiufragments.dam.com.netbeast;

/**
 * Created by Cayetano Rodríguez Medina on 26/4/16.
 */

// Class that includes all global application parameters
public class Global {
    private static Global instance;

    // IP where the dashboard is running
    private String IP = "192.168.1.138";
    private String port = "8000";

    public void setIP (String ip) { this.IP = ip; }

    public String getIP () { return this.IP; }

    public void setPort (String port) { this.port = port; }

    public String getPort () { return this.port; }

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
