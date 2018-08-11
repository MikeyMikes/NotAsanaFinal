package com.example.brosh.notasana;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.brosh.notasana.Tasks.TaskTab;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SignUpActivity extends AppCompatActivity {

    EditText newUsernameEditText, newPasswordEditText, retypePasswordEditText;
    Button createAccountButton;

    DatabaseReference rootRef;

    List<Integer> existingIDs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        newUsernameEditText = findViewById(R.id.newUsernameEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        retypePasswordEditText = findViewById(R.id.retypePasswordEditText);

        createAccountButton = findViewById(R.id.createAccountButton);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!newUsernameEditText.getText().toString().equals("")){
                    if(!newPasswordEditText.getText().toString().equals("")){
                        if(!retypePasswordEditText.getText().toString().equals("")){
                            if(retypePasswordEditText.getText().toString().equals(newPasswordEditText.getText().toString())) {
                                try {
                                    //so the rest of the application knows the user's name
                                    User.username = newUsernameEditText.getText().toString();
                                    User.userID = getRandomID();

                                    //stuff for firebase
                                    DatabaseReference mRef = FirebaseDatabase.getInstance()
                                            .getReferenceFromUrl("https://notasana-57563.firebaseio.com/Users/Info/");

                                    rootRef = mRef.child(newUsernameEditText.getText().toString());
                                    ValueEventListener eventListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(!dataSnapshot.exists()) {
                                                addChild(rootRef, "Password", newPasswordEditText.getText().toString());
                                                addChild(rootRef, "UserID", User.userID);

                                                toast("Account created successfully!");
                                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                            else{
                                                toast("That account already exists.");
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {}
                                    };
                                    rootRef.addListenerForSingleValueEvent(eventListener);


                                }
                                catch(Exception e){
                                    toast(e.getMessage());
                                }
                            }
                            else{
                                toast("Those passwords don't match.");
                                newPasswordEditText.setText("");
                                retypePasswordEditText.setText("");
                                newPasswordEditText.requestFocus();
                            }
                        }
                        else{
                            retypePasswordEditText.setError("Please retype password.");
                            retypePasswordEditText.requestFocus();
                        }
                    }
                    else{
                        newPasswordEditText.setError("Password can't be blank.");
                        newPasswordEditText.requestFocus();
                    }
                }
                else{
                    newUsernameEditText.setError("Username can't be blank.");
                    newUsernameEditText.requestFocus();
                }
            }
        });

    }

    private String getRandomID() {

        Random rand = new Random();
        int id;

        do {
            id = rand.nextInt(100000) + 1;
        } while (existingIDs.contains(id));

        existingIDs.add(id);

        return id + "";
    }

    public static void addChild(DatabaseReference rootRef, String key, String value){

        DatabaseReference childRef = rootRef.child(key);
        childRef.setValue(value);

    }

    public void toast(String msg){
        Toast.makeText(SignUpActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
