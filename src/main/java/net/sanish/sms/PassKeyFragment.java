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
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


public class PassKeyFragment extends Activity {

   public void onCreate(Bundle savedInstance) {
       /*
       AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
       dialogBuilder.setTitle("Enter Your Key");
       dialogBuilder.setView(this.getLayoutInflater().inflate(R.layout.activity_pass_key, null));

       dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface d, int i) {
               String input =  ((EditText) findViewById(R.id.keyInput)).getText().toString();

               if(input.equals("12345")) {
                  startActivity(new Intent(PassKeyActivity.this, MessageListActivity.class));
               } else {
                  Log.d("PASSKEY", "incorrect");
               }
           }
       });

*/
   }

}
