package dam.com.netbeast;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;

import java.io.InputStream;

/**
 * Created by Alejandro Rodríguez Calzado on 28/04/16.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageButton bmImage;
    App app;

    public DownloadImageTask(ImageButton bmImage, App app) {
        this.bmImage = bmImage;
        this.app = app;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            Log.d("DownloadImageTask", "Downloading " + urldisplay);
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            //Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
        app.setLogoBitmap(result);
    }
}