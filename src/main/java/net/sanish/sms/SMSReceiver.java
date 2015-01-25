package net.sanish.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

      Bundle b = intent.getExtras();
      Object[] messages = (Object[]) b.get("pdus"); // get array of messages in Protocol Data Units

      for(Object rawMessage : messages) {
          SmsMessage message = SmsMessage.createFromPdu((byte[])rawMessage);
          System.out.println(message.getMessageBody());
          System.out.println(message.getOriginatingAddress());

      }



    }
}
