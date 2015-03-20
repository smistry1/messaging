package net.sanish.sms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Activity for determining which screen to present on start up.
 */
public class StartupActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MessageListActivity.instance != null) {
            MessageListActivity.instance.finish();
        }
        MessageListActivity.allowResumeAccess = true;
        Intent i;
        Global.clearNotification(this);
        SharedPreferences sp = getSharedPreferences("key_infos", Context.MODE_PRIVATE);
        if(sp.getString("keyCheckPhrase", null) == null) {
            // key does not exist, ask user to set up key.
            i = new Intent(this, SetupActivity.class);
        } else {
            i = new Intent(this, PassKeyActivity.class);
            // Key Exists, ask user for it.
        }

        startActivity(i);
        finish();
    }

}
