package net.sanish.sms;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.sanish.sms.R;

import java.io.Serializable;
import java.util.ArrayList;

/**
    Activity for viewing a list of messsages
*/
public class MessageListActivity extends Activity {

    private static MessageListActivity instance;
    // Make instance available to outside so that if the List is Open the MessageReceiver can notify
    // and the list can be refreshed.
    private int lastMessageOffset = 0;      // offset of the next message to be requested
    private ListView list;                  // ListView for messages
    private MessageDatabase db;             // Message Database
    private ArrayList<Message> listItems;   // Current List of messages
    private String defaultKey;              // The default key
    private ArrayAdapter adapter;           // List adapter for displaying messages
    private String key;                     // the users key
    public static boolean allowResumeAccess = true;
    public static boolean forceAllowResumeAccess = false;

    /**
     * @return returns an ArrayList of 10 messages starting from the lastMessageOffset
     * will increase lastMessageOffset by 10
     */
    private ArrayList<Message> getMoreMessages() {
        ArrayList<Message> messages =  db.getMessagesList(lastMessageOffset);
        lastMessageOffset += 10;
        return messages;
    }


    private void allowResumeAccess() {
        this.allowResumeAccess = true; // Allows Activity to be resumed.
        this.forceAllowResumeAccess = true; // Prevents allowResumeAccess being set to false onPause
    }
    /**
    * Will update the list of messages shown (to be called when new message has been delivered)
    */
    public static void refreshList() {
        if(instance != null) {              // Check if Message List is open or running in background.
            System.out.println("UPDATING");
            MessageListActivity inst = instance;
            instance.adapter.clear();
            instance.lastMessageOffset = 0;
            instance.updateList();
        }
    }

    /**
     * Will get more messages from the database (In a seperate thread to avoid blocking)
     * and them to the ListView.
     */
    private void updateList()  {
        new Thread(new Runnable() {   // Run in another thread to avoid blocking the UI
            @Override
            public void run() {
                final ArrayList messages = getMoreMessages();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addAll(messages);
                    }
                });
            }
        }).start();
    }

    public void onResume() {
        super.onResume();
        if(!allowResumeAccess) {
            startActivity(new Intent(this, PassKeyActivity.class));
            allowResumeAccess(); // Allow user to get back in later.
            finish();
        }
    }

    public void onPause() {
        super.onPause();
        if(!forceAllowResumeAccess)
            allowResumeAccess = false;
        forceAllowResumeAccess = false;
    }
    /**
     * Will reencrypt a message using the user's key and store in the database
     * (to be called when message is encrypted using the default key)
     * @param message - The Message to be encrypted
     */
    private void updateEncryptedMessage(final Message message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                MessageEncryptor e = new MessageEncryptor(key);
                String enc = e.encrypt(message.getMessageBody());
                db.updateMessage(enc, message.getId(), 1);
            }
        }).start();
    }


    /**
     * gets Default Key from private sharedPrefences file and sets defaultKey equal to this
     */
    protected void initDefaultKey() {
        defaultKey = getSharedPreferences("key_infos", Context.MODE_PRIVATE).getString("defaultKey", null);
    }
    @Override

    /**
     * Initialise View Components, Databases and Keys.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_message_list);     // Set XML Layout
        list = (ListView) findViewById(R.id.list);          // Get List from XML layout
        db = new MessageDatabase(this);                     // Get Message Database using activity as context
        listItems = getMoreMessages();                      // Initial List
        initDefaultKey();                                   // Initialise the default key
        key = (String) getIntent().getExtras().get("key");  // Initialise the user key passed from previous activity

        // create list adapter using built in simple_list_item_2 template
        adapter = new ArrayAdapter<Message>(this, android.R.layout.simple_list_item_2, android.R.id.text1, listItems){

            // Method is called whenever a new list item is computed.
            public View getView(int i, View v, ViewGroup vg) {
                View newView = super.getView(i, v, vg);
                TextView line1 = (TextView) newView.findViewById(android.R.id.text1);       // Get first line of text
                TextView line2 = (TextView) newView.findViewById(android.R.id.text2);       // Get second line of text
                Message m = listItems.get(i);             // Get Message Corresponding to list index
                String messageText;
                String messageHeader;
                messageHeader = m.getSender();
                if(m.isEncrypted() == true) {
                   switch (m.getEncryptionState()) {
                       case 0 :  // Message is encrypted with default key not user key.
                            if(defaultKey == null) // Message Arrived after first open of app, but same session.
                                initDefaultKey();
                            messageText = new MessageEncryptor(defaultKey).decrypt(m.getMessageBody());
                            // Decrypt the message using the default key
                            m.setDecryptedMessage(messageText);
                            // set the decrypted text so no need to decrypt again when item is scrolled to again.
                            updateEncryptedMessage(m); // reencrypt the message using the user key and store.
                       break;

                       case 1 : // Message is encrypted with user key.
                       default :
                           messageText = new MessageEncryptor(key).decrypt(m.getMessageBody());
                           m.setDecryptedMessage(messageText);
                           // set the decrypted text so no need to decrypt again when item is scrolled to again.
                       break;
                   }

                } else {
                    messageText = m.getMessageBody(); // Recycled list item, therefore not encrypted
                }

                if(messageText.length() > 50) {
                    messageText = messageText.substring(0, 50); // truncate message
                }
                line1.setText(messageHeader);
                line2.setText(messageText);
                return newView;
            }
        };

        list.setOnScrollListener(new AbsListView.OnScrollListener() {

            private boolean userScrolled;

            public void onScrollStateChanged(AbsListView view, int scrollState) {

                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

                    userScrolled = true; // scroll was initiated by user instead of list population
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                // check if the end of the list has been reached
                if(userScrolled &&(firstVisibleItem + visibleItemCount) == totalItemCount){
                    updateList();
                    userScrolled = false;
                }
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                // Make new Intent containing the message that was clicked
                Intent intent = new Intent(MessageListActivity.this, MessageViewActivity.class);
                intent.putExtra("message", listItems.get(i));


                startActivity(intent); // start  the message view activity using intent
                allowResumeAccess(); // Allow user to resume after finished reading message.

            }
        });
        list.setAdapter(adapter);

        registerForContextMenu(list);

    }



    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() == R.id.list) {
            menu.add(ContextMenu.NONE, 0, ContextMenu.NONE, "Delete");
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        System.out.println(item.getItemId());
        System.out.println(info.position);

        if(item.getItemId() == 0)  {  // Delete was clicked
            int listPosition = info.position; // the position of the item that was selected.
            Message m = listItems.get(listPosition); // get the message that has been selected.
            db.deleteMessageById(m.getId());        // Delete the message from DB.
            listItems.remove(listPosition);         // Remove from the list view.
            adapter.notifyDataSetChanged();         // Refresh the list
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.message_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_compose) {
            startActivity(new Intent(this, ComposeMessageActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
