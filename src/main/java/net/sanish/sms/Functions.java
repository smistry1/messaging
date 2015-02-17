package net.sanish.sms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.Random;


public class Functions {

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
