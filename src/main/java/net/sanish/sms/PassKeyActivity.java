package net.sanish.sms;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class PassKeyActivity extends Activity {



    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_pass_key);

        Button submitButton = (Button) findViewById(R.id.passKeyOK);
        final EditText passKeyInput = (EditText) findViewById(R.id.keyInput);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(passKeyInput.getText().toString().equals("12345")) {
                    startActivity(new Intent(PassKeyActivity.this, MessageListActivity.class));
                    PassKeyActivity.this.finish();
                } else {


                    Toast.makeText(PassKeyActivity.this, "Incorrect Key!", Toast.LENGTH_SHORT).show();
                    passKeyInput.setText("");
                }


            }
        });



    }

}