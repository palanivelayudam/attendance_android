package com.acceedo.attendancesystem.FireBaseActivity;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.acceedo.attendancesystem.common.CommonFunction;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "TestFCMMsg";
    CommonFunction cf;

    // f3o-QpPWZag:APA91bGrBkwIuoLlQsU7YYEXIAn57tr0_WDyU-V8BXlpgeXWp7dcGD3yERX7MbxGNL6L92nUehzdSxSp2lF1wsPCct0FnPTegPemMSyrMM_nei4fJQL9IynRUwVMKWhe9xLosFtZD8ZA

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();


        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);
        //Toast.makeText(this,refreshedToken,Toast.LENGTH_SHORT).show();

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        //Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        //registrationComplete.putExtra("token", refreshedToken);
        //LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
        //cf.show_toast(token);
        //Toast.makeText(getApplicationContext(),token,Toast.LENGTH_LONG).show();
    }


}
