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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import net.sanish.sms.R;

import java.util.ArrayList;

public class MessageListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);





        final ListView list = (ListView) findViewById(R.id.list);


        final ArrayList<String[]> data = new ArrayList<String[]>();
        for (int i = 0; i < 20; i ++) {
            data.add(new String[]{"HI"+i, "DATA" + i});
        }

        final ArrayAdapter<String[]> adapter = new ArrayAdapter<String[]>(this, android.R.layout.simple_list_item_2, android.R.id.text1, data){
            public View getView(int i, View v, ViewGroup vg) {
                View newView = super.getView(i, v, vg);
                TextView line1 = (TextView) newView.findViewById(android.R.id.text1);
                TextView line2 = (TextView) newView.findViewById(android.R.id.text2);
                line1.setText(data.get(i)[0]);
                line2.setText(data.get(i)[1]);//.substring(0, 50) + "...");
                return newView;
            }
        };

        list.setOnScrollListener(new AbsListView.OnScrollListener() {

            private boolean userScrolled;

            public void onScrollStateChanged(AbsListView view, int scrollState) {
                System.out.println(scrollState);
                if(scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

                    userScrolled = true; // scroll was initiated by user instead of list population
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if(userScrolled &&(firstVisibleItem + visibleItemCount) == totalItemCount){
                    System.out.println(firstVisibleItem + ", " + visibleItemCount + ", " + totalItemCount);
                    System.out.println("end of list");

                    adapter.add(new String[]{"another", "new thing"});

                    userScrolled = false;
                }
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MessageListActivity.this, MessageViewActivity.class);
                //intent.putExtra("sender", listItems[i]);
                //intent.putExtra("message",listItems2[i]);

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
