package net.sanish.sms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

// TODO implement Message sending service for other apps (Required by android system)
public class RespondWithSMSService extends Service {
    public RespondWithSMSService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
