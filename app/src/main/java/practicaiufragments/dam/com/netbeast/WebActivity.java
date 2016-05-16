package practicaiufragments.dam.com.netbeast;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Cayetano Rodríguez Medina on 12/5/16.
 */

public class WebActivity extends Activity {
    private String title;
    private String IP;
    private String port;

    private static String TAG = WebActivity.class.getSimpleName();

    // Progress dialog
    private ProgressDialog pDialog;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.web_app);

        Bundle b = getIntent().getExtras();
        if (b!=null) {
            title = b.getString("title");
        }
        else
            Log.d(TAG, "Bundle is null");

        textView = (TextView) findViewById(R.id.title_text);
        textView.setText(title);

        // Let's create/get global params
        Global g = Global.getInstance();
        IP = g.getIP();
        port = g.getPort();

        WebView myWebView = (WebView) this.findViewById(R.id.webView);

        // Enable JavaScript
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        showpDialog();

        // Load proxy app url
        myWebView.loadUrl("http://" + IP + ":" + port + "/i/" + title);

        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        hidepDialog();
                    }
                }, 2000);


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
