package practicaiufragments.dam.com.netbeast;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Cayetano Rodríguez Medina on 12/5/16.
 */

public class WebActivity extends AppCompatActivity {
    private String app_name;
    private String IP;
    private String port;

    private static String TAG = WebActivity.class.getSimpleName();

    private NavigationViewListener navigationViewListener;

    // Progress dialog
    private ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.web_app);

        // Get the app name to include it as the title of the activity
        Bundle b = getIntent().getExtras();
        if (b!=null) {
            app_name = b.getString("title");
        }
        else
            Log.d(TAG, "Bundle is null");

        navigationViewListener = new NavigationViewListener(this);
        getSupportActionBar().setTitle(app_name);

        // Let's create/get global params
        Global g = Global.getInstance();
        IP = g.getIP();
        port = g.getPort();

        WebView myWebView = (WebView) this.findViewById(R.id.webView);

        // Enable JavaScript
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Provide a WebViewClient for your WebView
        myWebView.setWebViewClient(new WebViewClient());

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        showpDialog();

        // Load proxy app url
        myWebView.loadUrl("http://" + IP + ":" + port + "/i/" + app_name);

        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        hidepDialog();
                    }
                }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
