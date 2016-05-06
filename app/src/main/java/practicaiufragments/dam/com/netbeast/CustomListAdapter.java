package practicaiufragments.dam.com.netbeast;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Alejandro Rodríguez Calzado on 28/04/16.
 */
public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<App> appItems;

    public CustomListAdapter(Activity activity, List<App> appItems) {
        this.activity = activity;
        this.appItems = appItems;
    }

    @Override
    public int getCount() {
        return appItems.size();
    }

    @Override
    public Object getItem(int location) {
        return appItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.custom_row, null);

        ImageButton imgBt = (ImageButton) convertView.findViewById(R.id.button);
        TextView txt = (TextView) convertView.findViewById(R.id.text);

        // getting app data for the row
        App app = appItems.get(position);

        if (app.getLogoURL() != null)
            if (app.getLogoBitmap() == null)
                // If the logo has not been downloaded yet...
                // download app logo and set it to image button
                new DownloadImageTask(imgBt, app).execute(app.getLogoURL());
            else
                // If the logo has already been downloaded...
                // set it to image button
                imgBt.setImageBitmap(app.getLogoBitmap());
        else
            imgBt.setImageResource(R.drawable.dflt);

        // set app name to textView
        txt.setText(app.getName());

        return convertView;
    }
}