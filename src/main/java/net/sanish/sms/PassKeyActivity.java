package net.sanish.sms;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;


public class PassKeyActivity extends Activity  implements View.OnClickListener{

    private Button submitButton;
    private EditText passKeyInput;          // The pass key field
    private String keyCheckPhrase;          // The phrase to check the key against

    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        setContentView(R.layout.activity_pass_key);

        keyCheckPhrase = getSharedPreferences("key_infos", Context.MODE_PRIVATE).getString("keyCheckPhrase", null);
        // set the phrase from the shared prefrences

        submitButton = (Button) findViewById(R.id.passKeyOK);
        passKeyInput = (EditText) findViewById(R.id.keyInput);

        submitButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        String key = passKeyInput.getText().toString();
        if(!key.equals("")) {
            MessageEncryptor e = new MessageEncryptor(key);
            try {
                e.decrypt(keyCheckPhrase);
                Global.launchMessageList(key, this);     // No exception decrypting key, key is correct
            } catch (EncryptionOperationNotPossibleException ex) {
                Toast.makeText(this, "Invalid Key.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Key cannot be empty.", Toast.LENGTH_LONG).show();
        }
    }
}