package net.sanish.sms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.sanish.sms.R;

/**
 * Activity for Composing/Replying to Messages.
 */

public class ComposeMessageActivity extends SubActivity implements View.OnClickListener {

    private Button sendButton;
    private EditText recipient;
    private EditText messageText;
    private Button contactLookupButton;
    private MessageDatabase db;
    private String key;
    private MessageEncryptor encryptor;

    /**
     *    Called when the activity is created, initializes UI
     *    Bundle should contain Intent with key to encrypt/ decrypt messages.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        db = new MessageDatabase(getApplicationContext());
        key = b.getString("key");
        encryptor = new MessageEncryptor(key);
        setContentView(R.layout.activity_compose_message);                  // Set XML Layout
        sendButton = (Button) (findViewById(R.id.sendButton));              // Get Button From XML LAyout
        contactLookupButton = (Button) (findViewById(R.id.contactButton));
        messageText = (EditText) (findViewById(R.id.messageText));
        recipient = (EditText) (findViewById(R.id.recipient));
        sendButton.setOnClickListener(this);
        contactLookupButton.setOnClickListener(this);

        if(b.get("recipient") != null) {
            recipient.setText((String) b.get("recipient")); // set the recipient if it was passed in.
        }

    }

    /**
     * starts the contact app to pick one.
     */
    private void showContactApp() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // only show contacts with phone numbers
        startActivityForResult(intent, 1);
        this.openedBefore = false; // allow resume after contact app is closed.

    }

    /**
     * Will be called by system when user picks a contact
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data == null) return; // user did not pick a contact.
        Uri contactUri = data.getData();
        Cursor cursor = getContentResolver().query(contactUri,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
        cursor.moveToFirst();

        String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        String finalNumber = "";

        for (char x : phoneNumber.toCharArray()) {
            int charCode = (int) x;
            if(x >= 48 && x <= 57) {  // Only keep digits and not brackets, spaces and + from number string.
                finalNumber += x;
            }
        }

        recipient.setText(finalNumber);
        // Put number in recipient field
    }

    /**
     *    Called by system when the user clicks send, validates input and sends message.
     */
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.sendButton) {

            if(!recipient.getText().toString().matches("[0-9]+")) {
                Toast.makeText(getApplicationContext(), "Invalid Recipient.", Toast.LENGTH_SHORT).show();
                return;
            } else if(messageText.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Cannot send blank message", Toast.LENGTH_SHORT).show();
                return;
            }

            MessageSender m = new MessageSender(getApplicationContext());
            m.send(messageText.getText().toString(), recipient.getText().toString()); // send the message


            String sender = Global.getPhoneNumber(getApplicationContext()); // get the user's phonenumber

            db.insertMessage(encryptor.encrypt(messageText.getText().toString()), sender, recipient.getText().toString(), 1);
            // insert encrypted sent message, with 1 as encryptionState, meaning encrypted with user's key.
            MessageListActivity.refreshList();
            Toast.makeText(getApplicationContext(), "Message Sent!", Toast.LENGTH_SHORT).show();
            finish();
        } else if(view.getId() == R.id.contactButton) {
           showContactApp();
        }
    }
}
