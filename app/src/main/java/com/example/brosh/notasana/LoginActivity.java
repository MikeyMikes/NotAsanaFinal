package com.example.brosh.notasana;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    TextView signUpTextView;
    Button loginButton;
    DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        signUpTextView = findViewById(R.id.signUpTextView);

        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                checkDatabaseForUser(new MyCallback() {
                    @Override
                    public void onCallback(Boolean isValid) {

                        if(isValid) {
                            //so the rest of the application knows the user's name
                            User.username = usernameEditText.getText().toString();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            toast("That combination of username/password is incorrect.");
                            usernameEditText.setText("");
                            passwordEditText.setText("");
                            usernameEditText.requestFocus();
                        }
                    }
                });

            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkDatabaseForUser(final MyCallback myCallback) {
        try {
            DatabaseReference mRef = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl("https://notasana-57563.firebaseio.com/Users/Info/");

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    String username = usernameEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    boolean isCorrect = false;

                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        if (dataSnapshot.exists()) {
                            String correctUsername = dsp.getKey();
                            String correctPassword = dsp.child("Password").getValue().toString();
                            if(correctUsername.equals(username)) {
                                if (correctPassword.equals(password)) {
                                    User.userID = dsp.child("UserID").getValue().toString();
                                    isCorrect = true;
                                }
                            }
                        }
                    }
                    myCallback.onCallback(isCorrect);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mRef.addListenerForSingleValueEvent(eventListener);

        }catch(Exception e){
            toast(e.getMessage());
        }
    }

    private void toast(String s) {
        Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
    }
}
