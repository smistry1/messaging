package net.sanish.sms.tests;

import android.test.AndroidTestCase;

import net.sanish.sms.MessageSender;

/**
 * Created by sanish on 13/03/2015.
 */
public class MessageSenderTest extends AndroidTestCase {

    private MessageSender ms;

    public MessageSenderTest() {
        ms = new MessageSender(getContext());
    }

    public void testInvalidNumber() {
        try {
            ms.send("fdfdfd", "ihiuhi");
            assertFalse(true); // Test Failed if went through
        } catch(Exception e) {
            assertTrue(true); // Test passed if exception
        }
    }

    public void testSendInvalidMessage() {
        try {
            ms.send("", "345435");
            assertFalse(true); // Test Failed if went through
        } catch(Exception e) {
            assertTrue(true); // Test passed if exception
        }
    }

    public void testSendValidMessage() {
        try {
            ms.send("test message", "012345");
            assertTrue(true);
        } catch (Exception e) {
            assertFalse(true); // Test failed if it did not go through
        }

    }
}
