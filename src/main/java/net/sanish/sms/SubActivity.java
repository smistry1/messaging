package net.sanish.sms;

import android.app.Activity;

/**
 * This class is to be used as a base class for all activities that are opened from the message list
 * to prevent key less resume access after pausing the sub activity.
 */
abstract class SubActivity extends Activity {

    protected boolean openedBefore = false;
    public void onResume () {
        super.onResume();
        if(openedBefore) {
            finish();
            MessageListActivity.allowResumeAccess = false;

            // If this activity is being resumed close it, and prevent ListActivity from being
            // resumed also, which forces user to renter key.

        } else {
            openedBefore = true;
            // next time onResume is called, it will be openedBefore.
        }
    }
}
