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

public class MessageViewActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view);  // Set XML Layout

        Message m = (Message) getIntent().getExtras().get("message");  // Get Message from extras
        TextView sender = (TextView) findViewById(R.id.sender);        // Get Sender field
        TextView message = (TextView) findViewById(R.id.message);      // Get Message field
        TextView date = (TextView) findViewById(R.id.date);            // Get Date Field
        sender.setText(m.getSender());                            // Set data for sender field
        message.setText(m.getMessageBody());                      // Set data for message field
        date.setText(m.getDate());                                // Set data for date field

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.message_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
