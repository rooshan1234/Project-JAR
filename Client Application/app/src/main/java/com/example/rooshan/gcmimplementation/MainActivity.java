package com.example.rooshan.gcmimplementation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;


public class MainActivity extends AppCompatActivity {

    private static final String CLASSTAG = "MainActivity";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mMessageReciever;
    private boolean isReceiverRegistered;
    private AtomicInteger messageId = new AtomicInteger();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //all fields on the menu
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final Button sendMessageButton= (Button) (findViewById(R.id.sendMessageButton));
        final TextView messageRecieved = (TextView)(findViewById(R.id.messageReceivedContainer));


        setSupportActionBar(toolbar);

        //Receive messages from GCM listener service
        mMessageReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                messageRecieved.append(intent.getExtras().getString("Message"));
            }
        };

        //Send message by pressing the send message button
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText sendMessage = (EditText)(findViewById(R.id.messageSentContainer));
                final String messageToSend = sendMessage.getText().toString();

                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params){
                        if (!messageToSend.isEmpty()){

                            Bundle data = new Bundle();
                            //data.putString("data", "magnet:?xt=urn:btih:411ace6e776f8da73619a57f2bed1d084e973b8d&dn=debian-update-8.4.0-arm64-CD-1.iso");
                            data.putString("data",messageToSend);
                            //send the message to GCM server

                            try {
                                   gcm.send(getString(R.string.gcm_defaultSenderId) + "@gcm.googleapis.com", Integer.toString(messageId.incrementAndGet()), data);
                            }catch (IOException ex){
                                Log.i(CLASSTAG, "Message was not sent");
                                return "Error sending upstream message: " + ex.getMessage();
                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        if (result != null) {
                            Log.i(CLASSTAG, "send message failed: " + result);
                        }
                    }

                }.execute(null,null,null);
            }
        });


        registerReceiver();

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReciever);
        isReceiverRegistered = false;
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReciever,
                    new IntentFilter("MESSAGE_RECIEVED"));
            isReceiverRegistered = true;
        }
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(CLASSTAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
