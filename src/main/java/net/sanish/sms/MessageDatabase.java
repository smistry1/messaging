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
    }

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
            "encryption_state TINYINT" +
        ");");
    }

    /**
     * Inserts a new message into the database
     *
     * @param message           - the message payload (should be encyrypted)
     * @param sender            - the number of the sender of the message
     * @param encryptionState   - number representing encryption state 0 for default key, 1 for user key
     */
    public void insertMessage(String message, String sender, int encryptionState) {
        this.getReadableDatabase().execSQL("INSERT INTO messages (message, sender, encryption_state) VALUES (?, ?, ?)",
            new Object[]{message, sender, encryptionState}
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
     * @return - A Cursor containing the messages
     */
    public Cursor getMessages(int offset) {
        String limitString = offset + ", 10";
        String sql = "SElECT * FROM messages ORDER BY received_at DESC LIMIT "+limitString;
        //System.out.println(sql);
        return this.getWritableDatabase().rawQuery(sql, null);
    }

    /**
     * Get 10 messages sorted by date received descending
     * @param offset - the starting offset
     * @return - An ArrayList containing the messages
     */
    public ArrayList<Message> getMessagesList(int offset) {
        Cursor c = getMessages(offset);
        ArrayList<Message> messages = new ArrayList<Message>();
        c.moveToFirst();

        while(!c.isAfterLast()) {
            messages.add(new Message(c.getString(1), c.getString(2), c.getInt(4), c.getInt(0), c.getString(3)));

            c.moveToNext();
        }
        c.close();
        return messages;
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
