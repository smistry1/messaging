package net.sanish.sms;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetupActivity extends Activity implements View.OnClickListener {

    private EditText key;               // Key Field
    private EditText keyConfirmation;   // Key Confirmation Field
    private Button okButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        key = (EditText) findViewById(R.id.key);
        keyConfirmation = (EditText) findViewById(R.id.confirmKey);
        okButton = (Button) findViewById(R.id.keySetUpOk);
        okButton.setOnClickListener((View.OnClickListener) this);
    }

    public void onClick(View v) {

        if(key.getText().toString().length() < 5) {
            Toast.makeText(this, "The key must be 5 characters or more.", Toast.LENGTH_LONG).show();
        } else if(!key.getText().toString().equals(keyConfirmation.getText().toString())) {
            Toast.makeText(this, "The key did not match the confirmation.", Toast.LENGTH_LONG).show();
        } else { // Key is valid
            SharedPreferences.Editor e = getSharedPreferences("key_infos", Context.MODE_PRIVATE).edit();

            MessageEncryptor encryptor = new MessageEncryptor(key.getText().toString());
            String checkPhrase = encryptor.encrypt(Global.getRandomString(30));
            //encrypt a random string using the user's key to check it later.

            e.putString("keyCheckPhrase", checkPhrase);
            e.commit();
            Global.launchMessageList(key.getText().toString(), this); // Launch this message list passing the key.

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setup, menu);
        return true;
    }

}
