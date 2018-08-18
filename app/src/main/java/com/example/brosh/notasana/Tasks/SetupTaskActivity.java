package com.example.brosh.notasana.Tasks;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.brosh.notasana.LoginActivity;
import com.example.brosh.notasana.MainActivity;
import com.example.brosh.notasana.R;
import com.example.brosh.notasana.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetupTaskActivity extends AppCompatActivity {

    Button submitButton;
    EditText nameEditText, taskEditText, deadlineEditText;
    Switch notificationSwitch;
    Boolean sendNotification = false;

    public SetupTaskActivity(){}

    public SetupTaskActivity(Task old, String name, String task, String deadline, String currentDate, Boolean sendNotification, int id){

        //nameEditText.setText(User.username);
        //taskEditText.requestFocus();

        Task tsk = new Task(name, task, deadline, currentDate, sendNotification, id);
        tsk.allTasks.add(tsk);
        TaskTab.taskAdapter.add(tsk.task);
        tsk.allTasks.remove(old);
        TaskTab.taskIDs.remove(TaskTab.taskIDs.indexOf(old.id));
        TaskTab.taskAdapter.remove(old.task);

        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_task);

        submitButton = findViewById(R.id.submitButton);

        nameEditText = findViewById(R.id.nameEditText);
        nameEditText.setText(User.username);

        taskEditText = findViewById(R.id.taskEditText);
        taskEditText.requestFocus();

        deadlineEditText = findViewById(R.id.deadlineEditText);

        notificationSwitch = findViewById(R.id.notificationSwitch);

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sendNotification = isChecked;
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //stuff for firebase
                DatabaseReference mRef = FirebaseDatabase.getInstance()
                        .getReferenceFromUrl("https://notasana-57563.firebaseio.com/Users/Info/" + User.username + "/Tasks/");

                String name = nameEditText.getText().toString();
                String task = taskEditText.getText().toString();
                String deadline = deadlineEditText.getText().toString();

                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
                Date date = new Date();
                String currentDate = formatter.format(date);

                //store information about this task
                if(!name.equals("")) {
                    if(!task.equals("")) {
                        if(isValidDate(deadline)) {
                            Task tsk = new Task(name, task, deadline, currentDate, sendNotification);
                            tsk.allTasks.add(tsk);
                            TaskTab.taskAdapter.add(taskEditText.getText().toString());

                            //send to database
                            //MainActivity.entryCount++;
                            try {
                                DatabaseReference rootRef = mRef.child(TaskTab.taskIDs.get(TaskTab.taskIDs.size()-1).toString());

                                addChild(rootRef, "Name", name);
                                addChild(rootRef, "Task", task);
                                addChild(rootRef, "Deadline", deadline);
                                addChild(rootRef, "Date Created", currentDate);
                                addChild(rootRef, "Notification", sendNotification.toString());
                                addChild(rootRef, "ID", tsk.id + "");

                            }catch(Exception e ){
                                Toast.makeText(SetupTaskActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            finish();
                        }else{
                            deadlineEditText.setError("Date Not Valid.");
                            deadlineEditText.setText("");
                            deadlineEditText.requestFocus();
                        }
                    }else{
                        taskEditText.setError("Please Enter Task.");
                        taskEditText.requestFocus();
                    }
                }else{
                    nameEditText.setError("Please Enter Name.");
                    nameEditText.requestFocus();
                }
            }
        });
    }

    public static boolean isValidDate(String date){
        String pattern = "(^[1-9]/|^0[1-9]/|^1[0-2]/)([1-9]/|0[1-9]/|1[0-9]/|2[0-9]/|3[0-1]/)(2[0-9][1-9][0-9])";

        // Create a Pattern object
        Pattern r = Pattern.compile(pattern);

        // Now create matcher object
        Matcher m = r.matcher(date);

        return m.find();
    }

    public static void addChild(DatabaseReference rootRef, String key, String value){

        DatabaseReference childRef = rootRef.child(key);
        childRef.setValue(value);

    }

    public void toast(String msg){
        Toast.makeText(SetupTaskActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

}
