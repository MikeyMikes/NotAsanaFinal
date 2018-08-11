package com.example.brosh.notasana.Friends;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.brosh.notasana.R;
import com.example.brosh.notasana.User;

public class AddFriendActivity extends AppCompatActivity {

    TextView yourFriendIDTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        yourFriendIDTextView = findViewById(R.id.yourFriendIDTextView);
        yourFriendIDTextView.setText("Your friend ID is " + User.userID);

    }
}
