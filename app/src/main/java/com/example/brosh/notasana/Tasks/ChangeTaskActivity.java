package com.example.brosh.notasana.Tasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.brosh.notasana.MainActivity;
import com.example.brosh.notasana.R;
import com.example.brosh.notasana.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangeTaskActivity extends AppCompatActivity {

    EditText nameEditText, taskEditText, deadlineEditText;
    Switch notificationSwitch;
    Button finishedButton;
    Task t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_task);

        nameEditText = findViewById(R.id.nameEditText);
        taskEditText = findViewById(R.id.taskEditText);
        deadlineEditText = findViewById(R.id.deadlineEditText);

        notificationSwitch = findViewById(R.id.notificationSwitch);

        finishedButton = findViewById(R.id.finishedButton);

        t = (Task) getIntent().getSerializableExtra("task");

        nameEditText.setText(t.getOwner());
        taskEditText.setText(t.getTask());
        deadlineEditText.setText(t.getDeadline());
        if (t.getSendNotification()) notificationSwitch.setChecked(true);

        //now we change stuff
        finishedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String name = nameEditText.getText().toString();
                String task = taskEditText.getText().toString();
                String deadline = deadlineEditText.getText().toString();
                boolean sendNotification = notificationSwitch.isChecked();

                SetupTaskActivity setupTaskActivity = new SetupTaskActivity();

                if (!name.equals("")) {
                    if (!task.equals("")) {
                        if (setupTaskActivity.isValidDate(deadline)) {
                            new SetupTaskActivity(t, name, task, deadline, t.getDateCreated(), sendNotification, t.id);

                            //write newly modified task to database
                            DatabaseReference mRef = FirebaseDatabase.getInstance()
                                    .getReferenceFromUrl("https://notasana-57563.firebaseio.com/Users/Info/" +
                                            User.username + "/Tasks/" + t.id + "/");

                            addChild(mRef, "Name", name);
                            addChild(mRef, "Task", task);
                            addChild(mRef, "Deadline", deadline);
                            addChild(mRef, "Date Created", t.getDateCreated());
                            addChild(mRef, "Notification", sendNotification + "");
                            addChild(mRef, "ID", t.id + "");

                            Intent intent = new Intent(ChangeTaskActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            deadlineEditText.setError("Date Not Valid.");
                            deadlineEditText.setText("");
                            deadlineEditText.requestFocus();
                        }
                    } else {
                        taskEditText.setError("Please Enter Task");
                        taskEditText.requestFocus();
                    }
                } else {
                    nameEditText.setError("Please Enter Name");
                    nameEditText.requestFocus();
                }
            }
        });
    }

    public static void addChild(DatabaseReference rootRef, String key, String value){

        DatabaseReference childRef = rootRef.child(key);
        childRef.setValue(value);

    }

    public void toast(String msg){
        Toast.makeText(ChangeTaskActivity.this, msg, Toast.LENGTH_SHORT).show();
    }
}
