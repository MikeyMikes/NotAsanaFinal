package com.example.brosh.notasana.Tasks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.brosh.notasana.MainActivity;
import com.example.brosh.notasana.R;

public class TaskDetailActivity extends AppCompatActivity {
    
    TextView ownerTextView, taskTextView, deadlineText, dateCreatedTextView, notificationTextView;
    Button editTaskButton, finishedButton;
    Task t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        t = (Task) getIntent().getSerializableExtra("task");
        
        ownerTextView = findViewById(R.id.ownerTextView);
        taskTextView = findViewById(R.id.taskTextView);
        deadlineText = findViewById(R.id.deadlineTextView);
        dateCreatedTextView = findViewById(R.id.dateCreatedTextView);
        notificationTextView = findViewById(R.id.notificationTextView);
        
        editTaskButton = findViewById(R.id.editTaskButton);
        finishedButton = findViewById(R.id.finishedButton);
        
        ownerTextView.setText(t.owner);
        taskTextView.setText(t.task);
        deadlineText.setText(t.deadline);
        dateCreatedTextView.setText(t.dateCreated);
        notificationTextView.setText(t.sendNotification + "");
        
        editTaskButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //I'm just passing this object from MainActivity to ChangeTaskActivity
                Intent i = new Intent(TaskDetailActivity.this, ChangeTaskActivity.class);
                i.putExtra("task", t);
                startActivity(i);

                finish();
            }
        });

        finishedButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(TaskDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        
    }

}
