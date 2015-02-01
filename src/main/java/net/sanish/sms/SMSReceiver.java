package net.sanish.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.Random;

public class SMSReceiver extends BroadcastReceiver {

    private SharedPreferences sharedPrefs;


    @Override
    public void onReceive(Context context, Intent intent) {
      SharedPreferences sharedPrefs = context.getSharedPreferences("key_infos", Context.MODE_PRIVATE);
      Bundle b = intent.getExtras();
      Object[] messages = (Object[]) b.get("pdus"); // get array of messages in Protocol Data Units
      String defaultKey = sharedPrefs.getString("defaultKey", null);
      MessageDatabase messageDatabase = new MessageDatabase(context);

      if(defaultKey == null) {
          defaultKey = generateDefaultKey();
          sharedPrefs.edit().putString("defaultKey", defaultKey);
      }

      MessageEncryptor encryptor = new MessageEncryptor(defaultKey);
      // Encrypt with defaultKey, re encrypt later when user opens app and enters their key

      for(Object rawMessage : messages) {
          SmsMessage message = SmsMessage.createFromPdu((byte[])rawMessage);
          String messageBody = message.getMessageBody();
          String sender = message.getOriginatingAddress();
          messageDatabase.insertMessage(encryptor.encrypt(messageBody), sender, 0);
      }

    }

    private String generateDefaultKey() {
        String key  = "";

        for (int i = 0; i < 30; i ++) // 30 char string
            key +=  (char) (new Random().nextInt((126 - 32) + 1) + 32); // random char between 32 and 126        }

        return key;
    }
}
