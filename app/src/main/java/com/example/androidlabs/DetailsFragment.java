package com.example.androidlabs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    private Bundle dataFromActivity;
    private long id;
    private AppCompatActivity parentActivity;
    //  private long dbID;
//    public DetailFragment() {
//        // Required empty public constructor
//    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        dataFromActivity = getArguments();
        //  id = dataFromActivity.getLong(ChatRoomActivity.ITEM_ID );
        View result =  inflater.inflate(R.layout.fragment_details, container, false);

        TextView message = (TextView)result.findViewById(R.id.msg);
        message.setText(dataFromActivity.getString(ChatRoomActivity.ITEM_SELECTED));

        TextView id = (TextView)result.findViewById(R.id.idText);
        id.setText("ID= "+dataFromActivity.getLong(ChatRoomActivity.ITEM_ID));

        CheckBox isSend = (CheckBox)result.findViewById(R.id.fragChk);
        isSend.setChecked(dataFromActivity.getBoolean(ChatRoomActivity.IS_SEND));

        Button hideButton = (Button)result.findViewById(R.id.hideButton);
        hideButton.setOnClickListener( clk -> {

            //Tell the parent activity to remove
            //        ChatRoomActivity parent = (ChatRoomActivity) getActivity();
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }
}
