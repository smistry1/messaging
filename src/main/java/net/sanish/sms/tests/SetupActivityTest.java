package net.sanish.sms.tests;

import android.content.Context;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.widget.Button;
import android.widget.EditText;

import net.sanish.sms.Message;
import net.sanish.sms.MessageEncryptor;
import net.sanish.sms.PassKeyActivity;
import net.sanish.sms.R;
import net.sanish.sms.SetupActivity;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;

/**
 * Created by sanish on 15/02/2015.
 */
public class SetupActivityTest extends ActivityUnitTestCase<SetupActivity> {

    private EditText key;               // Key Field
    private EditText keyConfirmation;   // Key Confirmation Field
    private Button okButton;

    public SetupActivityTest() {
        super(SetupActivity.class);
    }

    public void setUp() throws Exception {

        super.setUp();
        Intent launchIntent = new Intent(getInstrumentation().getTargetContext(), SetupActivity.class);
        startActivity(launchIntent, null, null);

        key = (EditText) getActivity().findViewById(R.id.key);
        keyConfirmation = (EditText) getActivity().findViewById(R.id.confirmKey);
        okButton = (Button) getActivity().findViewById(R.id.keySetUpOk);

    }

    public void testWithShortKey() {
        key.setText("abc");
        keyConfirmation.setText("abc");
        okButton.performClick();
        assertEquals(false, isFinishCalled());
    }

    public void testWithMismatch() {
        key.setText("123poi");
        keyConfirmation.setText("12r3poi");
        okButton.performClick();
        assertEquals(false ,isFinishCalled());
    }

    public void testWithValidKey() {
        key.setText("123poi");
        keyConfirmation.setText("123poi");
        okButton.performClick();
        assertEquals(true, isFinishCalled());
        String phrase = getActivity().getSharedPreferences("key_infos", Context.MODE_PRIVATE).getString("keyCheckPhrase", null);
        assertEquals(true, phrase != null);         // A phrase should now exist
        MessageEncryptor e = new MessageEncryptor("123poi");

        try {
            e.decrypt(phrase); // should be able to decrypt phrase using key
        } catch(EncryptionOperationNotPossibleException ex) {
            fail();
        }

    }

}
