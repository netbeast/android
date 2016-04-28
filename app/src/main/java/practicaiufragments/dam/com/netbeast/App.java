package practicaiufragments.dam.com.netbeast;

import android.util.Log;

/**
 * Created by Alejandro Rodríguez Calzado on 28/04/16.
 */
public class App {
    private String name;
    private String gitHubUrl;
    private String logoPath;
    private String logoURL;

    public App (String name, String gitHubUrl, String logoPath) {
        this.name = name;
        this.gitHubUrl = gitHubUrl;
        this.logoPath = logoPath;
        if (logoPath != null)
            this.logoURL = calculateLogoUrl();
        else
            this.logoURL = null;
    }

    public String getName() {
        return name;
    }

    public String getGitHubUrl() {
        return gitHubUrl;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGitHubUrl(String gitHubUrl) {
        this.gitHubUrl = gitHubUrl;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    private String calculateLogoUrl () {
        String aux = gitHubUrl;
        aux = aux.replaceFirst("\\.git", "");
        aux = aux.replaceFirst(".*https://github.com/", "https://raw.githubusercontent.com/");
        aux += "/master/" + logoPath;
        //Log.d("test",aux);

        return aux;
    }
}
