package practicaiufragments.dam.com.netbeast;

import android.app.IntentService;
import android.content.Intent;

import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * Created by fcojriosbello on 23/05/16.
 */
public class NotificationsService extends IntentService {

    public NotificationsService () {
        super(NotificationsService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // "ws://localhost:8000"
        final MqttAndroidClient mqttAndroidClient = new MqttAndroidClient(
                this.getApplicationContext(),
                this.getResources().getString(R.string.mqtt_uri),
                this.getResources().getString(R.string.clientId));

        mqttAndroidClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connection was lost!");

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                System.out.println("Message Arrived!: " + topic + ": " + new String(message.getPayload()));
                CharSequence text = new String(message.getPayload());
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                toast.show();

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                System.out.println("Delivery Complete!");
            }
        });

        try {
            mqttAndroidClient.connect(null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("Connection Success!");
                    try {
                        System.out.println("Subscribing to /notifications");
                        mqttAndroidClient.subscribe("notifications", 0);
                        System.out.println("Subscribed to /notifications");
                        //  System.out.println("Publishing message..");
                        //  mqttAndroidClient.publish("notifications", new MqttMessage("This is a notification!".getBytes()));
                    } catch (MqttException ex) {

                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("Connection Failure!");
                    System.out.println(exception);
                }
            });
        } catch (MqttException ex) {

        }

    }
}