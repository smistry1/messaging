package net.sanish.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.Random;

public class SMSReceiver extends BroadcastReceiver {


    /**
     * Method is called by system when a new SMS Message Comes In
     */
    @Override
    public void onReceive(Context context, Intent intent) {
      SharedPreferences sharedPrefs = context.getSharedPreferences("key_infos", Context.MODE_PRIVATE);
      Bundle b = intent.getExtras();
      Object[] messages = (Object[]) b.get("pdus"); // get array of messages in Protocol Data Units
      String defaultKey = sharedPrefs.getString("defaultKey", null);  // get the default key from sps
      MessageDatabase messageDatabase = new MessageDatabase(context);

      if(defaultKey == null) {
          defaultKey = Functions.getRandomString(30);
          SharedPreferences.Editor e = sharedPrefs.edit();
          e.putString("defaultKey", defaultKey);
          e.commit();

          // If the default key does not exist yet, generate and store it.
      }

      MessageEncryptor encryptor = new MessageEncryptor(defaultKey);
      // Encrypt with defaultKey, re encrypt later when user opens app and enters their key

      for(Object rawMessage : messages) {
          SmsMessage message = SmsMessage.createFromPdu((byte[])rawMessage);
          String messageBody = message.getMessageBody();
          String sender = message.getOriginatingAddress();
          messageDatabase.insertMessage(encryptor.encrypt(messageBody), sender, 0);
      }


       MessageListActivity.refreshList(); // Refreshes Message List if open.

    }



}
