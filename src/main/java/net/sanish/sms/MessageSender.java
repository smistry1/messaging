package net.sanish.sms;

import android.content.Context;
import android.telephony.SmsManager;

/**
 * Class To Send Messages
 */
public class MessageSender {

    private Context c;
    private SmsManager manager;

    public MessageSender(Context c) {
        this.c = c;
        manager = SmsManager.getDefault();
    }

    /**
     * Sends a SMS message
     * @param messageText - the text of the message
     * @param to - the number of the recipient.
     * @throws IllegalArgumentException if messageText or number is empty
     */
    public void send(String messageText, String to) {
        manager.sendTextMessage(to, null, messageText, null, null) ;
        // send the message using default service center and no callback functions.

    }

}