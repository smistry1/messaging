package net.sanish.sms;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.telephony.SmsMessage;

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
          defaultKey = Global.getRandomString(30);
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
          messageDatabase.insertMessage(encryptor.encrypt(messageBody), sender, Global.getPhoneNumber(context), 0);

          showNewMessageNotification(context, sender);
      }


       MessageListActivity.refreshList(); // Refreshes Message List if open.



    }

    private void showNewMessageNotification(Context c, String sender) {

        Notification.Builder  nb = new Notification.Builder(c);
        nb.setContentTitle("New SMS Message(s)");

        PendingIntent i = PendingIntent.getActivity(c, 0, new Intent(c, StartupActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        nb.setContentIntent(i);

        nb.setAutoCancel(true);
        nb.setSmallIcon(R.drawable.closed);
        nb.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        // vibrate, ring, and light the phone using phone settings

        NotificationManager m = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        m.notify(0, nb.build());


    }



}
