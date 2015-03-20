package net.sanish.sms.tests;

import android.test.AndroidTestCase;

import net.sanish.sms.Message;


public class MessageTest extends AndroidTestCase {

    public void test() {
        Message m = new Message("testmessage", "3543", 0, 1 , "2015-01-01 00:00", "123"); // create a message object
        assertEquals("2015-01-01 00:00", m.getDate());
        assertEquals(true, m.isEncrypted());
        assertEquals(0, m.getEncryptionState());
        assertEquals(1, m.getId());
        assertEquals("testmessage", m.getMessageBody());
        assertEquals("3543", m.getSender());
        m.setDecryptedMessage("hello");
        assertEquals("hello" , m.getMessageBody());
        assertEquals(false, m.isEncrypted());

    }
}
