package com.example.androidlabs;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {

    ArrayList<Message> chatList = new ArrayList<>();

    MyListAdapter myAdapter;

    public static final String ACTIVITY_NAME = "activity_chatroom";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        EditText message = findViewById(R.id.chatText);
        Button sendButton = findViewById(R.id.sendBtn);
        Button receiveButton = findViewById(R.id.receiveBtn);
        ListView myList = findViewById(R.id.theListView);

        //send button action
        sendButton.setOnClickListener(click ->{
            String sendMessage = message.getText().toString();
            Message newMessage = new Message(sendMessage,true);

            //add new person to list
            chatList.add(newMessage);
            // update listView;
            myAdapter = new MyListAdapter();
            myList.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
            myList.setSelection(myAdapter.getCount()-1);
            message.setText("");
        });
        //receive button action
        receiveButton.setOnClickListener(click->{
            String receiveMessage = message.getText().toString();
            Message newMessage = new Message(receiveMessage, false);

            chatList.add(newMessage);

            myAdapter = new MyListAdapter();
            myList.setAdapter(myAdapter);
            myAdapter.notifyDataSetChanged();
            myList.setSelection(myAdapter.getCount()-1);
            message.setText("");
        });

        myList.setOnItemLongClickListener( (p, b, pos, id) -> {
            androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            final int positionToRemove = pos;
            alertDialogBuilder.setTitle(R.string.deletemsg)
                    //display messages
                    .setMessage(getString(R.string.delRow) +(pos+1)+ "\n"+ getString(R.string.delDBid) + id)
                    //The database ID is:"+id)

                    //action of Yes button
                    .setPositiveButton(R.string.delBtn, (click, arg) -> {
                        chatList.remove(pos);
                        myAdapter.notifyDataSetChanged();
                    })
                    //action of No button
                    .setNegativeButton(R.string.undoBtn, (click, arg) -> { })
                    .create().show();
            return true;
        });
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
            return position;
        }
    }

}

