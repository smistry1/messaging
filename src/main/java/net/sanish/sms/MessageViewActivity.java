package net.sanish.sms;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import net.sanish.sms.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Activity for viewing messages
 */

public class MessageViewActivity extends SubActivity {


    /**
     * Called by system when activity is created, intent should contain a message object to display
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view);  // Set XML Layout

        Message m = (Message) getIntent().getExtras().get("message");  // Get Message from extras
        TextView sender = (TextView) findViewById(R.id.sender);        // Get Sender field
        TextView message = (TextView) findViewById(R.id.message);      // Get Message field
        TextView date = (TextView) findViewById(R.id.date);            // Get Date Field

        if(sender.equals(Global.getPhoneNumber(getApplicationContext()))) {
            sender.setText(m.getRecipientDisplayName());
            // if the sender of the message is the user, show the recipient
        } else {
            sender.setText(m.getSenderDisplayName());
            // otherwise show the sender
        }

        message.setText(m.getMessageBody());                      // Set data for message field
        date.setText(m.getDate());                                // Set data for date field

    }





}
