package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class ChatRoomActivity extends AppCompatActivity {

    ArrayList<Message> chatList = new ArrayList<>();

    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final String IS_SEND = "IS_SENT";
    public static final int EMPTY_ACTIVITY = 345;
    DetailsFragment dFragment ;

    MyListAdapter myAdapter;
    SQLiteDatabase db;

    public static final String ACTIVITY_NAME = "activity_chatroom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        boolean isTablet = findViewById(R.id.fragmentLocation) != null;
        EditText message = findViewById(R.id.chatText);
        Button sendButton = findViewById(R.id.sendBtn);
        Button receiveButton = findViewById(R.id.receiveBtn);
        ListView myList = findViewById(R.id.theListView);

        //create opener, get database
        MyOpener opener = new MyOpener(this);
        db = opener.getWritableDatabase();

        //query: select all results
        String[] columns = {MyOpener.COL_ID, MyOpener.COL_MESSAGE, MyOpener.COL_SEND_RECEIVE};
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        //find columns
        int messageColumnIdx = results.getColumnIndex(MyOpener.COL_MESSAGE);
        int idColumnIdx = results.getColumnIndex(MyOpener.COL_ID);
        int checkColumnIdx = results.getColumnIndex(MyOpener.COL_SEND_RECEIVE);

        while (results.moveToNext()) {
            String messageData = results.getString(messageColumnIdx);
            int checkData = results.getInt(checkColumnIdx);
            boolean isSend = true;

            if (checkData == 0) {
                isSend = false;
            }
            long idData = results.getLong(idColumnIdx);
            chatList.add(new Message(messageData, idData, isSend));
        }

        myAdapter = new MyListAdapter();
        myList.setAdapter(myAdapter);
        myList.setSelection(myAdapter.getCount() - 1);


        //send button action
        sendButton.setOnClickListener(click ->{
            String sendMessage = message.getText().toString();
            //Message newMessage = new Message(sendMessage,true);
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(MyOpener.COL_MESSAGE, sendMessage);
            newRowValues.put(MyOpener.COL_SEND_RECEIVE, 1);
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);
            Message messages = new Message(sendMessage, newId, true);

            //add new person to list
            chatList.add(messages);
            // update listView;
            // myAdapter = new MyListAdapter();
            //myList.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
            myList.setSelection(myAdapter.getCount()-1);
            message.setText("");
        });
        //receive button action
        receiveButton.setOnClickListener(click->{
            String receiveMessage = message.getText().toString();
            //Message newMessage = new Message(receiveMessage, false);
            ContentValues newRowValues = new ContentValues();

            newRowValues.put(MyOpener.COL_MESSAGE, receiveMessage);
            newRowValues.put(MyOpener.COL_SEND_RECEIVE, 0);
            long newId = db.insert(MyOpener.TABLE_NAME, null, newRowValues);
            Message messages = new Message(receiveMessage, newId, false);
            //add new person to list
            chatList.add(messages);
            //update listview
            //myAdapter = new MyListAdapter();
            //myList.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
            myList.setSelection(myAdapter.getCount()-1);
            message.setText("");
        });

        //delete from database function
        myList.setOnItemLongClickListener( (p, b, pos, id) -> {
            androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            //final int positionToRemove = pos;
            Message selectedMessage = chatList.get(pos);
            alertDialogBuilder.setTitle(R.string.deletemsg)
                    //display messages
                    .setMessage(getString(R.string.delRow) +(pos+1)+ "\n"+ getString(R.string.delDBid) + id)
                    //The database ID is:"+id)

                    //action of Yes button
                    .setPositiveButton(R.string.delBtn, (click, arg) -> {
                        deleteMessage(selectedMessage);
                        Log.e("delete:",""+ selectedMessage.getId() ); //add
                        chatList.remove(pos);
                        myAdapter.notifyDataSetChanged();
                        //  ChatRoomActivity parent = (ChatRoomActivity) getActivity();
                        this.getSupportFragmentManager()
                                .beginTransaction()
                                .remove(dFragment)
                                .commit();
                    })
                    //action of No button
                    .setNegativeButton(R.string.undoBtn, (click, arg) -> { })
                    .create().show();
            return false; //change from true to false
        });

        myList.setOnItemClickListener((list, item, position, id) -> {
            //Create a bundle to pass data to the new fragment
            dFragment = new DetailsFragment();
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_SELECTED, chatList.get(position).getMessage());
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putBoolean(IS_SEND, chatList.get(position).isSend);
            dataToPass.putLong(ITEM_ID, chatList.get(position).getId());

            if(isTablet)
            {
                //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                //  dFragment.setTablet(true);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        //   .addToBackStack("AnyName")
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });

        results.moveToFirst();
        printCursor(results);
        Log.e(ACTIVITY_NAME, "In function: OnCreate()");
    }

    //delete from Database function
    protected void deleteMessage(Message i){
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(i.getId())});
    }

    //printCursor function
    protected  void printCursor(Cursor c ){

        Log.i( "Cursor Object","Database version number : "+ db.getVersion());
        Log.i(String.valueOf(c.getColumnCount()), "Number of columns : "+c.getColumnCount());

        String[] columns = c.getColumnNames();
        for (String name:columns){
            Log.i(name, "The column of the cursor in the cursor: " + name);
        }
        Log.i(String.valueOf(c.getCount()),	"The number of results in the cursor : "+c.getCount());

        for (int i=0;i<c.getCount();i++){
            Log.i(c.getString(c.getColumnIndex(MyOpener.COL_MESSAGE)), "PrintCursor: Message : "+c.getString(c.getColumnIndex(MyOpener.COL_MESSAGE)));
            Log.i(String.valueOf(c.getInt(c.getColumnIndex(MyOpener.COL_ID))), "printCursor: ID : "+c.getInt(c.getColumnIndex(MyOpener.COL_ID)));
            Log.i(String.valueOf(c.getInt(c.getColumnIndex(MyOpener.COL_SEND_RECEIVE))), "printCursor: Send(1) or Receive(0) : "+c.getInt(c.getColumnIndex(MyOpener.COL_SEND_RECEIVE)));
            c.moveToNext();
        }

        c.moveToFirst();
    }


    protected class MyListAdapter extends BaseAdapter{
        @Override
        public int getCount(){
            return chatList.size();
        }
        @Override
        public Message getItem(int position){
            return  chatList.get(position);
        }
        @Override
        public View getView (int position, View old, ViewGroup parent){
            LayoutInflater inflater = getLayoutInflater();
            View rowView;
            TextView rowMessage;

            Message thisRow = getItem(position);

            if (getItem(position).isSend()){
                rowView = inflater.inflate(R.layout.left_row, parent, false);
                rowMessage = (TextView) rowView.findViewById(R.id.send_msg);
                ImageView send = (ImageView) rowView.findViewById(R.id.left_image);
                send.setImageResource(R.drawable.row_send);
                rowMessage.setText(thisRow.getMessage());
            } else {
                rowView = inflater.inflate(R.layout.right_row, parent, false);
                rowMessage = (TextView) rowView.findViewById(R.id.receive_msg);
                ImageView receive = (ImageView) rowView.findViewById(R.id.right_image);
                receive.setImageResource(R.drawable.row_receive);
                rowMessage.setText(thisRow.getMessage());
            }

            rowMessage.setText((thisRow.getMessage()));
            return rowView;
        }

        @Override
        public long getItemId(int position){
            return getItem(position).getId();
        }
    }

}
