package com.example.brosh.notasana.Friends;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.brosh.notasana.Feature;
import com.example.brosh.notasana.R;
import com.example.brosh.notasana.SignUpActivity;
import com.example.brosh.notasana.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FriendsTab extends Fragment implements Feature {

    String[] friendArray;
    public static ArrayAdapter friendAdapter;
    ListView friendView;
    FloatingActionButton addFriendButton, removeFriendButton;
    Button aboutFriendButton;

    private String title = "Friends";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_friends_tab, container, false);

        //addExistingFriends();

        friendArray = new String[]{"Example friend"};
        final List<String> friendList = new ArrayList<>(Arrays.asList(friendArray));
        friendAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, friendList);
        friendAdapter.remove("Example friend");

        friendView = rootView.findViewById(R.id.friendView);
        friendView.setAdapter(friendAdapter);

        addFriendButton = rootView.findViewById(R.id.addFriendButton);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddFriendActivity.class);
                startActivity(intent);
            }
        });

        removeFriendButton = rootView.findViewById(R.id.removeFriendButton);

        aboutFriendButton = rootView.findViewById(R.id.aboutFriendButton);

        friendView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // @RequiresApi(api = Build.VERSION_CODES.O)
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                /*
                itemID = (int) taskIDs.get(position);
                itemName = taskAdapter.getItem(position);
                itemPosition = position;

                setDaysUntilDue();
                */

            }

        });

        return rootView;

    }

    private void addExistingFriends() {

        DatabaseReference mRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://notasana-57563.firebaseio.com/Users/Info/" + User.username + "/Friends/");

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    try {
                        friendAdapter.add(dsp.getKey());
                    }
                    catch(Exception e){

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}
