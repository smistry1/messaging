package net.sanish.sms;

import android.content.Context;
import android.telephony.SmsManager;

/**
 * Created by sanish on 07/03/2015.
 */
public class MessageSender {

    private Context c;
    private SmsManager manager;

    public MessageSender(Context c) {
        this.c = c;
        manager = SmsManager.getDefault();
    }

    public void send(String messageText, String to) {
        manager.sendTextMessage(to, null, messageText, null, null) ;
        // send the message using default service center and no callback functions.

    }

}
