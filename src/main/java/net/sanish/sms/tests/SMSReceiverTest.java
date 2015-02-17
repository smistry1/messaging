package net.sanish.sms.tests;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.test.AndroidTestCase;
import com.android.internal.util.*;

import net.sanish.sms.Message;
import net.sanish.sms.MessageDatabase;
import net.sanish.sms.MessageEncryptor;
import net.sanish.sms.SMSReceiver;

import java.util.ArrayList;

/**
 * Created by sanish on 14/02/2015.
 */
public class SMSReceiverTest extends AndroidTestCase {

    public void testReceiveMessage() {
        byte[] pduMessage = new byte[]{
                7,-111,65,81,85,21,18,-14,4,11,-111,97,5,85,21,17,-15,0,0,96,96,96,81,48,48,-118,4,-44,-14,-100,14
        };
        // pdu in bytes, Sender is +16505551111, Message is 'Test'

        Intent  i = new Intent();
        i.setClass(getContext(), SMSReceiver.class);
        i.setAction("android.provider.Telephony.SMS_RECEIVED");     // Set Received Flag
        i.putExtra("pdus", new Object[]{pduMessage});


        SMSReceiver r = new SMSReceiver();
        r.onReceive(getContext(), i);  // called by system in practice

        String defaultKey = getContext().getSharedPreferences("key_infos", Context.MODE_PRIVATE).getString("defaultKey", null);

        assertFalse(defaultKey == null); //  this should not be null as at least 1 message has been received.
        MessageEncryptor m = new MessageEncryptor(defaultKey);
        MessageDatabase db = new MessageDatabase(getContext());

        ArrayList<Message> messages = db.getMessagesList(0);  // Most recent messages are first

        assertTrue(messages.get(0).getSender().equals("+16505551111"));
        assertTrue(m.decrypt(messages.get(0).getMessageBody()).equals("Test"));
        // Test passed if sender stored is correct and decrypted message is 'Test'


    }
}
