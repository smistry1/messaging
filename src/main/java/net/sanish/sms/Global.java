package net.sanish.sms;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import java.util.Random;

/**
 * Used for functions and variables used throughout app.
 */


public class Global {



    /**
     * Generate a random string of specified length
     * @param length - length of string
     * @return - a random string
     */
    public static String getRandomString (int length) {
        String s  = "";

        for (int i = 0; i < length; i ++) // 30 char string
            s +=  (char) (new Random().nextInt((126 - 32) + 1) + 32); // random char between codes 32 and 126        }

        return s;
    }

    public static void clearNotification(Context c) {
        // Clears Notification with ID of 0
        ((NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(0);
    }

    /**
     * Launches the message list activity with a specified key.
     *
     * @param key - the users key
     * @param c - the current context
     */
    public static void launchMessageList(String key, Context c) {
        Intent i = new Intent(c, MessageListActivity.class);
        i.putExtra("key", key);
        c.startActivity(i);
        ((Activity) c).finish();
    }


}
