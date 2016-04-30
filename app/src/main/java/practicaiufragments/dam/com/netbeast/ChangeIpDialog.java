package practicaiufragments.dam.com.netbeast;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Alejandro Rodríguez Calzado on 28/04/16.
 */
public class ChangeIpDialog extends DialogFragment {
    View customView;
    private Pattern pattern;
    private Matcher matcher;

    private static final String IPADDRESS_PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        customView = inflater.inflate(R.layout.ip_dialog, null);
        builder.setView(customView)
                // Add action buttons
                .setPositiveButton(R.string.connect_dialog_button, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        EditText ip = (EditText) customView.findViewById(R.id.ipTextEdit);
                        String givenIP = ip.getText().toString();
                        // givenIP is the one that de user set so we need to verify the format

                        if (givenIP.isEmpty() || !validateIP(givenIP)) {
                            Toast.makeText(getContext(),
                                    R.string.ip_error_dialog,
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Global g = Global.getInstance();

                            g.setIP(ip.getText().toString());

                            TextView ip_tv = (TextView) getActivity().findViewById(R.id.tv_dashboardip);
                            ip_tv.setText(g.getIP());
                        }
                    }
                })
                .setNegativeButton(R.string.cancel_dialog_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ChangeIpDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

    @Override
    public void onStart()
    {
        super.onStart();
        ((AlertDialog) getDialog()).getWindow().setBackgroundDrawableResource(R.color.background);

    }

    public boolean validateIP(String ip){
        pattern = Pattern.compile(IPADDRESS_PATTERN);
        matcher = pattern.matcher(ip);
        Log.d("validator", "Value: " + matcher.matches());
        return matcher.matches();
    }
}
