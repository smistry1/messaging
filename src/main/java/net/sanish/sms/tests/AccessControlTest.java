package net.sanish.sms.tests;


import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.sanish.sms.PassKeyActivity;
import net.sanish.sms.R;

public class AccessControlTest extends ActivityUnitTestCase<PassKeyActivity>{

    private Button okButton;
    private EditText keyField;


    public AccessControlTest() {
        super(PassKeyActivity.class);
    }

    private void setObjects() {
        this.keyField = (EditText) getActivity().findViewById(R.id.keyInput);
        this.okButton = (Button) getActivity().findViewById(R.id.passKeyOK);

    }

    protected void setUp() throws Exception {
        super.setUp();
        Intent launchIntent = new Intent(getInstrumentation().getTargetContext(), PassKeyActivity.class);
        startActivity(launchIntent, null, null); // start the passkey activity
    }



    @SmallTest
    public void testResponseWithValidKey() {
        this.setObjects();
        this.keyField.setText("abc123"); // set the key field to contain "12345"
        //assumes 'abc123' is currently the correct key.

        this.okButton.performClick(); // Press the OK Button

        assertNotNull("No launch intent fired", getStartedActivityIntent()); // App should fire an intent to launch another activity
        assertTrue(isFinishCalled()); // Succeeded if this Activity Finished
    }

    @SmallTest
    public void testResponseWithWrongKey() {
        this.setObjects();
        this.keyField.setText("abc"); // set the key field to contain "abc"
        this.okButton.performClick(); // Press the OK Button

        assertTrue(!isFinishCalled()); // The activity should not close
    }


    @SmallTest
    public void testResponseWithNoKey() {
        this.setObjects();
        this.okButton.performClick(); // Press the OK Button
        assertTrue(!isFinishCalled()); // The activity should not close
    }

}
