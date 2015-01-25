package net.sanish.sms;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

public class WAPPushReceiver extends BroadcastReceiver {
    public WAPPushReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        intent.setClass(context, SMSReceiver.class);
        context.sendBroadcast(intent); // Redirect to SMSReceiver
    }


}
