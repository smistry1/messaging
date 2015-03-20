package net.sanish.sms;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Handles database operations
 */
public class MessageDatabase extends SQLiteOpenHelper {

    public MessageDatabase(Context c) {
        super(c, "messages.db", null, 1);
        this.c = c;
    }

    private Context c;
    /**
     * Method automatically run on first access to database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE messages (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "message TEXT NOT NULL," +
            "sender TEXT NOT NULL," +
            "received_at DATETIME DEFAULT CURRENT_TIMESTAMP," +
            "encryption_state TINYINT," +
            "recipient TEXT NOT NULL" +
        ");");
    }

    /**
     * Inserts a new message into the database
     *
     * @param message           - the message payload (should be encyrypted)
     * @param sender            - the number of the sender of the message
     * @param encryptionState   - number representing encryption state 0 for default key, 1 for user key
     */
    public void insertMessage(String message, String sender, String recipient, int encryptionState) {

        this.getReadableDatabase().execSQL("INSERT INTO messages (message, sender, encryption_state, recipient) VALUES (?, ?, ?, ?)",
            new Object[]{message, sender, encryptionState, recipient}
        );
    }

    /**
     * Allows you to update the message payload and encryption state
     * @param messageText - payload of message (should be encrypted)
     * @param id          - id of the message to update
     * @param encState    - number representing encryption state 0 for default key, 1 for user key
     */
    public void updateMessage(String messageText, int id, int encState) {
        this.getWritableDatabase().execSQL("UPDATE messages SET message = ?, encryption_state = ? WHERE _id = ?",
                new Object[]{messageText, encState, id}
        );
    }

    /**
     * Gets a message by ID
     * @param id - the id of the message
     * @return a Cursor containing a message
     */
    public Cursor getMessageById(int id) {
        return this.getReadableDatabase().rawQuery("SELECT * FROM messages WHERE _id = ?",
                new String[] {Integer.toString(id)}
        );
    }

    /**
     * Deletes a message
     * @param id - the id of the message to delete
     */
    public void deleteMessageById(int id) {
        this.getWritableDatabase().execSQL("DELETE FROM messages WHERE _id = ?",
                new String[]{Integer.toString(id)}
        );
    }


    /**
     * Get 10 messages sorted by date received descending
     * @param offset - the starting offset
     * @param messageType the type of message, 0 for received, 1 for sent
     * @return - A Cursor containing the messages
     */
    public Cursor getMessages(int offset, int messageType) {
        String limitString = offset + ", 10";

        String number = Global.getPhoneNumber(c);
        String whereClause = "WHERE sender ";

        if(messageType == 0) {
            whereClause += "!= ?"; // for received messages (sender is not equal to users phone number)
        } else {
            whereClause += "= ?"; // for sent messages (sender is equal to users phone number)
        }

        String sql = "SElECT * FROM messages " + whereClause  + " ORDER BY received_at DESC LIMIT "+limitString;
        //System.out.println(sql);
        return this.getWritableDatabase().rawQuery(sql, new String[]{number});
    }

    /**
     * Get 10 received messages sorted by date received descending
     * @param offset - the starting offset
     * @return - A Cursor containing the messages
     */
    public Cursor getMessages(int offset) {
        return getMessages(offset, 0);
    }

    /**
     * Get 10 sent messages sorted by date received descending
     * @param offset - the starting offset
     * @return - A Cursor containing the messages
     */
    public Cursor getSentMessages(int offset) {
        return getMessages(offset, 1);
    }

    /**
     * Get 10 received messages sorted by date received descending
     * @param offset - the starting offset
     * @return - An ArrayList containing the messages
     */
    public ArrayList<Message> getMessagesList(int offset) {
        return getMessagesList(offset, 0);
    }

    public ArrayList<Message> getSentMessagesList(int offset) {
        return getMessagesList(offset, 1);
    }

    /**
     * Get 10 messages sorted by date received descending
     * @param offset - the starting offset
     * @param messageType the type of message, 0 for received, 1 for sent
     * @return - An ArrayList containing the messages
     */
    public ArrayList<Message> getMessagesList(int offset, int messageType) {
        Cursor c = getMessages(offset, messageType);
        ArrayList<Message> messages = new ArrayList<Message>();
        c.moveToFirst();

        while(!c.isAfterLast()) {
            Message m = new Message(c.getString(1), c.getString(2), c.getInt(4), c.getInt(0), c.getString(3), c.getString(5));
            m.setSenderDisplayName(Global.numberToName(c.getString(2), this.c));
            m.setReceiverDisplayName(Global.numberToName(c.getString(5), this.c));
            messages.add(m);
            c.moveToNext();
        }
        c.close();
        return messages;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
