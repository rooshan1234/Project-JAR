package com.example.rooshan.gcmimplementation;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

/**
 * Created by Rooshan on 1/30/2016.
 */
public class RegistrationIntentService extends IntentService {

    private static final String CLASSTAG = "RegistrationIntent";


    public RegistrationIntentService(){
        super(CLASSTAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        //load the deafult shared preferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try{
            //Generate registration token from sender ID
            InstanceID instanceID = InstanceID.getInstance(this);

            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

            Log.i(CLASSTAG, "GCM Registration Token: " + token);

        }catch (Exception e){
            Log.d(CLASSTAG, "Failed to complete token refresh", e);
        }
    }


}

