package net.sanish.sms;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sanish on 19/01/2015.
 */
public class MessageDatabase extends SQLiteOpenHelper {

    public MessageDatabase(Context c) {
        super(c, "messages.db", null, 1);
    }

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

    public void insertMessage(String message, String sender, int encryptionState) {
        this.getReadableDatabase().execSQL("INSERT INTO messages (message, sender, encryption_state) VALUES (?, ?, ?)",
            new Object[]{message, sender, encryptionState}
        );
    }

    public Cursor getMessageById(int id) {
        return this.getReadableDatabase().rawQuery("SELECT * FROM messages WHERE _id = ?",
                new String[]{Integer.toString(id)}
        );
    }

    public void deleteMessageById(int id) {
        this.getWritableDatabase().execSQL("DELETE FROM messages WHERE _id = ?",
                new String[]{Integer.toString(id)}
        );
    }

    public Cursor getMessages(int offset) {
        String limitString = offset + ", 10";
        return this.getWritableDatabase().rawQuery("SElECT * FROM messages LIMIT "+limitString, null);
    }

  

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
