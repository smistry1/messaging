package net.sanish.sms.tests;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.TextView;

import net.sanish.sms.Message;
import net.sanish.sms.MessageViewActivity;
import net.sanish.sms.PassKeyActivity;
import net.sanish.sms.R;


/**
 * Tests if the MessageView view displays message correctly when passed a Message
 */
public class MessageViewTest  extends ActivityUnitTestCase<MessageViewActivity> {


    public MessageViewTest() {
        super(MessageViewActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        Intent launchIntent = new Intent(getInstrumentation().getTargetContext(), PassKeyActivity.class);
        launchIntent.putExtra("message", new Message("test message", "0123", 0, 0, "2015-01-01 00:00"));
        startActivity(launchIntent, null, null);
    }

    public void test() {
        TextView sender = (TextView)  getActivity().findViewById(R.id.sender);        // Get Sender field
        TextView message = (TextView)  getActivity().findViewById(R.id.message);      // Get Message field
        TextView date = (TextView)  getActivity().findViewById(R.id.date);            // Get Date Field

        assertEquals("0123", sender.getText());
        assertEquals("test message", message.getText());
        assertEquals("2015-01-01 00:00", date.getText());
    }
}
