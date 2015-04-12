package net.sanish.sms;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.sanish.sms.R;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLOutput;
import java.util.ArrayList;

/**
 * Activity for exporting messages to a CSV File
 */
public class ExportActivity extends SubActivity {

    private Button exportButton;
    private MessageDatabase db;
    private String key;
    private MessageEncryptor en;
    private EditText fileNameInput;

    /**
     * Called by system to create activity, initializes UI Components, Database and keys.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        exportButton = (Button) findViewById(R.id.exportButton);
        db = new MessageDatabase(this);
        key = getIntent().getExtras().getString("key");
        en = new MessageEncryptor(key);
        fileNameInput = (EditText) findViewById(R.id.fileName);

        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fileNameInput.getText().toString().equals("")) {
                    Toast.makeText(ExportActivity.this, "File name cannot be empty.", Toast.LENGTH_LONG).show();
                } else {
                    exportMessages(fileNameInput.getText().toString());
                }
            }
        });
    }

    /**
        Should be called when export message is complete
        Will show a message passed to it and closes the activity.
        @param message - the message to show to the user
    */
    private void onExportComplete(String message) {
        Toast.makeText(ExportActivity.this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * Exports the message to a file in CSV format
     * @param fileName - the name of the file to export to.
     */
    private void exportMessages(final String fileName) {

        new Thread(new Runnable() {
            public void run() {
                try {
                    File path = Environment.getExternalStoragePublicDirectory("exported_messages"); // Get the path to store the messages
                    path.mkdirs();   // Make the export_messages directory if it does not exist
                    final File exportFile = new File(path, fileName);   // open the file
                    PrintWriter pw = new PrintWriter(exportFile);
                    ArrayList<Message> messages = db.getAllMessages();

                    pw.println("sender,recipient,date,message");
                    for(Message m: messages) {
                       m.setDecryptedMessage(en.decrypt(m.getMessageBody()).replaceAll("'", "\\\\'"));
                        System.out.println(m.getMessageBody());
                       pw.println(
                               "'"+m.getSender()+"'," +
                               "'"+m.getRecipient()+"',"+
                               "'"+m.getDate()+"',"+
                               "'"+m.getMessageBody()+"'"
                       );

                    }

                    pw.close();  // Flush the printwriter buffer and close the file.

                    runOnUiThread(new Runnable() {
                        public void run() {
                            onExportComplete("Message successfully exported to " + exportFile.getAbsolutePath());
                        }
                    });

                } catch(IOException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            onExportComplete("Failed to export messages.");
                        }
                    });
                    //e.printStackTrace();
                }
            }
        }).start();

    }


}
