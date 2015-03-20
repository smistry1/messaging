package net.sanish.sms.tests;

import android.database.Cursor;
import net.sanish.sms.Message;
import android.test.AndroidTestCase;

import net.sanish.sms.MessageDatabase;

import java.util.ArrayList;


public class MessageDatabaseTest extends AndroidTestCase {

    public void clearDatabase() {
        getContext().deleteDatabase("messages.db");
    }

    public void testUpdate() {
        clearDatabase();

        MessageDatabase db = new MessageDatabase(getContext());

        db.insertMessage("test", "0123", "5432", 0);

        db.updateMessage("hello", 1, 1);

        ArrayList<Message> a = db.getMessagesList(0);


        assertTrue(
                (a.get(0).getMessageBody().equals("hello")) &&
                (a.get(0).getEncryptionState() == 1)
        );

        clearDatabase();
    }

    public void testInsertAndSelectAndDelete() {

        clearDatabase();
        MessageDatabase db = new MessageDatabase(getContext());
        db.insertMessage("sdgjsjgjs", "35346436", "5432", 0);
        Cursor c1 = db.getMessageById(1);
        c1.moveToFirst();
        ArrayList a1 = db.getMessagesList(0);
        db.insertMessage("lllff", "45346", "5432", 0);
        Cursor c2 = db.getMessages(0);
        c2.moveToFirst();
        ArrayList a2 = db.getMessagesList(0);

        db.deleteMessageById(1);
        Cursor c3 = db.getMessages(0);
        c3.moveToFirst();
        //System.out.println(c1.getCount());
        //System.out.println(a1.size());
        //System.out.println(c2.getCount());
        //System.out.println(((Message) a1.get(0)).getId());


        assertTrue((
                (c1.getCount() == 1) &&
                (a1.size() ==  1) &&
                (c2.getCount() == 2) &&
                (a2.size() == 2) &&
                (c3.getCount() == 1)
        ));

        clearDatabase(); // clear invalid data

    }


}
