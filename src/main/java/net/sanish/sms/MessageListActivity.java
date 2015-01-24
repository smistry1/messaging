package net.sanish.sms;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.sanish.sms.R;

public class MessageListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        MessageDatabase db = new MessageDatabase(this);



        getApplicationContext().deleteDatabase("messages.db");

        db.insertMessage("this is a test message", "012345");
        db.insertMessage("test message 2", "566556");

        Cursor x = db.getAllMessages();

        x.moveToFirst();

        while(!x.isAfterLast()) {
            System.out.println(x.getString(0) +"\t" + x.getString(1) + "\t" + x.getString(2));
            x.moveToNext();
        }

        db.deleteMessageById(1);

        x = db.getMessageById(2);
        x.moveToFirst();

        while(!x.isAfterLast()) {
            System.out.println(x.getString(0) +"\t" + x.getString(1) + "\t" + x.getString(2) + "\t" + x.getString(3));
            x.moveToNext();
        }



        ListView list = (ListView) findViewById(R.id.list);
        final String[] listItems = new String[]{
                "Somebody",
                "Somebody Else",
                "555-555-001",
                "A Person"
        };

        final String[] listItems2 = new String[]{
                "Dragée gummies sugar plum dragée cheesecake. Carrot cake wafer chocolate soufflé ice cream. Apple pie cupcake halvah gummi bears cheesecake marzipan.",
                "Croissant dessert jelly-o powder cupcake. Chocolate cake marshmallow marshmallow croissant chupa chups dessert cake. Pie pie candy canes. Dessert sweet brownie halvah gummi bears cookie tootsie roll.",
                "Chupa chups lemon drops dragée unerdwear.com powder croissant chocolate. Wafer chupa chups tiramisu jelly unerdwear.com gummies oat cake macaroon chocolate cake. Jelly fruitcake marzipan caramels candy canes gummi bears chupa chups wafer.",
                "Cheesecake jelly beans candy canes cookie chupa chups bear claw. Cheesecake unerdwear.com pudding marzipan. Chocolate bar candy chocolate bar sesame snaps. Fruitcake muffin cupcake."
        };


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, listItems){
            public View getView(int i, View v, ViewGroup vg) {
                View newView = super.getView(i, v, vg);
                TextView line1 = (TextView) newView.findViewById(android.R.id.text1);
                TextView line2 = (TextView) newView.findViewById(android.R.id.text2);
                line1.setText(listItems[i]);
                line2.setText(listItems2[i].substring(0, 50) + "...");
                return newView;
            }
        };

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MessageListActivity.this, MessageViewActivity.class);
                intent.putExtra("sender", listItems[i]);
                intent.putExtra("message",listItems2[i]);

                startActivity(intent);

            }
        });
        list.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.message_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_compose) {
            startActivity(new Intent(this, ComposeMessageActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
