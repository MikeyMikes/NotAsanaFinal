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

        addFriendButton = findViewById(R.id.findFriendButton);
        addFriendButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                friendIDEditText = findViewById(R.id.friendIDEditText);
                final String friendID = friendIDEditText.getText().toString();
                friendNameEditText = findViewById(R.id.friendNameEditText);
                final String friendName = friendNameEditText.getText().toString();

                if(friendID != "") {
                    if(friendName != "") {
                        findFriend(friendName, friendID);
                    }
                    else{
                        friendNameEditText.setError("This field can't be blank");
                        friendNameEditText.setText("");
                        friendNameEditText.requestFocus();
                    }
                }
                else{
                    friendIDEditText.setError("This field can't be blank");
                    friendIDEditText.setText("");
                    friendIDEditText.requestFocus();
                }
            }
        });

    }

    public void findFriend(final String friendName, final String friendID){

        DatabaseReference mRef;

        try {
            mRef = FirebaseDatabase.getInstance().getReferenceFromUrl
                    ("https://notasana-57563.firebaseio.com/Users/Info/" +
                            friendName + "/");
        }
        catch(Exception e){
            toast("That user wasn't found first");
            return;
        }

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                boolean found = false;

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        try {
                            String correctID = "";
                            if(dsp.getKey().toString().equals("UserID")){
                                correctID = dsp.getValue().toString();
                            }
                            if (correctID.equals(friendID)) {
                                found = true;
                            } else {
                                found = false;
                            }
                        }catch(Exception e){
                            toast("whoops: " + e.getMessage());
                        }
                    }
                }
                if(found) {
                    FriendsTab.friendAdapter.add(friendName);
                    addFriendToDatabase(friendName, friendID);
                    toast("Friend added successfully!");
                    finish();
                }
                else{
                    toast("User not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mRef.addListenerForSingleValueEvent(eventListener);

    }

    private void addFriendToDatabase(String friendName, String friendID) {

        DatabaseReference mRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://notasana-57563.firebaseio.com/Users/Info/" +
                        User.username + "/Friends/");

        mRef.child(friendName).setValue(friendID);

    }

    public void toast(String msg){
        Toast.makeText(AddFriendActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
