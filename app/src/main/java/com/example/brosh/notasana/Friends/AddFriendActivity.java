package com.example.brosh.notasana.Friends;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brosh.notasana.R;
import com.example.brosh.notasana.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;

public class AddFriendActivity extends AppCompatActivity {

    TextView yourFriendIDTextView;
    EditText friendIDEditText, friendNameEditText;
    Button addFriendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        yourFriendIDTextView = findViewById(R.id.yourFriendIDTextView);
        yourFriendIDTextView.setText("Your friend ID is " + User.userID);

        friendIDEditText = findViewById(R.id.friendIDEditText);
        friendNameEditText = findViewById(R.id.friendNameEditText);

        addFriendButton = findViewById(R.id.findFriendButton);
        addFriendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO
                if(friendIDEditText != null && friendNameEditText != null) {
                    findFriend();
                }
            }
        });

    }

    public void findFriend(){

        DatabaseReference mRef;

        try {
            mRef = FirebaseDatabase.getInstance().getReferenceFromUrl
                    ("https://notasana-57563.firebaseio.com/Users/Info/" +
                            friendNameEditText + "/");
        }
        catch(Exception e){
            toast("That user wasn't found");
            return;
        }

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        String correctID = dsp.child("UserID").getValue().toString();
                        if(correctID.equals(friendIDEditText.getText().toString())) {
                            //TODO
                            toast("Worked");
                        }
                        else{
                            toast("That user wasn't found");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mRef.addListenerForSingleValueEvent(eventListener);

    }

    public void toast(String msg){
        Toast.makeText(AddFriendActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
