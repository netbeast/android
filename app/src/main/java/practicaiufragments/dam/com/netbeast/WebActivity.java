package practicaiufragments.dam.com.netbeast;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by Cayetano Rodríguez Medina on 12/5/16.
 */

public class WebActivity extends Activity {
    private String url;
    private String IP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.web_app);

        // Let's create/get global params
        Global g = Global.getInstance();
        IP = g.getIP();

        WebView myWebView = (WebView) this.findViewById(R.id.webView);
        // Enable JavaScript
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Try to access an app directly
        myWebView.loadUrl("http://" + IP + ":60653");

    }



}
