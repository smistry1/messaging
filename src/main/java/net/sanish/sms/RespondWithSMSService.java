package net.sanish.sms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class RespondWithSMSService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException();
    }
}
