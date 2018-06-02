package com.example.mhc.sensorapp;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
        import android.os.Handler;
        import android.os.Message;
        import android.provider.ContactsContract;
        import android.support.v7.app.ActionBarActivity;
        import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.telephony.SmsManager;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends Activity {

    private TextView textView;
    boolean flag = false;
    String number = "0";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (textView != null) {
                textView.setText(Integer.toString(msg.what));
                if ((msg.what > 80 || msg.what < 78) && !flag) {
                    flag = true;
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(number, null, "Alert! The owner of the watch might be having a heart attack! The heart beat rate is "+Integer.toString(msg.what), null, null);
                }
            }

        }
    };
    private GoogleApiClient client;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.heartbeat);
        Button dugme = (Button) findViewById(R.id.button);
        Button dugme2 = (Button) findViewById(R.id.button2);
        Button dugme3 = (Button) findViewById(R.id.button3);
        Button dugme4 = (Button) findViewById(R.id.button4);
        final EditText girdi = (EditText) findViewById(R.id.editText);
        dugme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, "Alert! The owner of the watch might need help!", null, null);
            }
        });
        dugme2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number = girdi.getText().toString();
                girdi.setEnabled(false);
            }
        });
        dugme3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                girdi.setEnabled(true);
            }
        });
        dugme4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = false;
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataLayerListenerService.setHandler(handler);
    }

    @Override
    protected void onPause() {
        DataLayerListenerService.setHandler(null);
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Main Page",
                Uri.parse("http://host/path"),
                Uri.parse("android-app://com.example.mhc.sensorapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        Action viewAction = Action.newAction(
                Action.TYPE_VIEW,
                "Main Page",
                Uri.parse("http://host/path"),

                Uri.parse("android-app://com.example.mhc.sensorapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}


