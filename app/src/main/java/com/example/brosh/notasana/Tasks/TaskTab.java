package com.example.brosh.notasana.Tasks;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brosh.notasana.Feature;
import com.example.brosh.notasana.MainActivity;
import com.example.brosh.notasana.R;
import com.example.brosh.notasana.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class TaskTab extends Fragment implements Feature {

    FloatingActionButton addTaskBtn, removeTaskBtn;
    Button taskDetailsButton;
    TextView daysUntilDue;
    static ListView tasks;
    public static ArrayList<Integer> taskIDs = new ArrayList<>();
    int itemID = -1;
    int itemPosition = 0;
    String itemName = "";
    private String title = "Tasks";

    String[] taskArray;
    static ArrayAdapter<String> taskAdapter;

    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.activity_task_tab, container, false);

        addExistingTasks();

        addTaskBtn = rootView.findViewById(R.id.addTaskButton);
        removeTaskBtn = rootView.findViewById(R.id.removeTaskButton);
        taskDetailsButton = rootView.findViewById(R.id.taskDetailsButton);

        daysUntilDue = rootView.findViewById(R.id.daysUntilDueTextView);

        tasks = rootView.findViewById(R.id.taskView);
        tasks.setItemsCanFocus(true);

        taskArray = new String[]{"Example task"};
        final List<String> taskList = new ArrayList<>(Arrays.asList(taskArray));
        taskAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, taskList);
        taskAdapter.remove("Example task");

        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SetupTaskActivity.class);
                startActivity(intent);
            }
        });

        tasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           // @RequiresApi(api = Build.VERSION_CODES.O)
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                itemID = (int) taskIDs.get(position);
                itemName = taskAdapter.getItem(position);
                itemPosition = position;

                setDaysUntilDue();
            }

        });

        removeTaskBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    toast("Removed: " + itemName);
                    taskAdapter.remove(itemName);
                    taskIDs.remove(taskIDs.indexOf(itemID));
                }catch(Exception e){
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                try {
                    //removing that task from database
                    DatabaseReference mRef = FirebaseDatabase.getInstance()
                            .getReferenceFromUrl("https://notasana-57563.firebaseio.com/Users/Info/" + User.username + "/Tasks/");
                    DatabaseReference idRef = mRef.child(itemID + "");

                    Toast.makeText(getActivity(), "Deleted: " + itemID, Toast.LENGTH_SHORT).show();

                    idRef.removeValue();
                }catch (Exception e){
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);


            }
        });

        taskDetailsButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
                try {
                    if (tasks.getItemAtPosition(itemPosition).equals(itemName)) {
                        for (Task t : Task.allTasks) {
                            if (t.id == itemID) {
                                Intent i = new Intent(getContext(), TaskDetailActivity.class);
                                i.putExtra("task", t);
                                startActivity(i);
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Select task", Toast.LENGTH_SHORT);
                    }
                }catch(Exception e){
                    toast(e.getMessage());
                }
            }
        });

        tasks.setAdapter(taskAdapter);

        return rootView;
    }

    private void setDaysUntilDue() {

        for (Task t : Task.allTasks) {
            if (t.id == itemID) {
                SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy", Locale.ENGLISH);
                Calendar cal = Calendar.getInstance();
                cal.setTime(Calendar.getInstance().getTime());

                int day = cal.get(Calendar.DAY_OF_MONTH);
                int month = cal.get(Calendar.MONTH)+1;
                int year = cal.get(Calendar.YEAR);

                Date currentDate = null;
                try {
                    currentDate = sdf.parse(month + "/" + day + "/" + year);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Date dueDate = null;
                try {
                    dueDate = sdf.parse(t.getDeadline());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long diffInMillies = Math.abs(currentDate.getTime() - dueDate.getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                if(currentDate.getTime() <= dueDate.getTime()) {
                    if(diff == 1){
                        daysUntilDue.setText(diff + " day remaining");
                    }else {
                        daysUntilDue.setText(diff + " days remaining");
                    }
                }
                else{
                    daysUntilDue.setText("0 days remaining");
                }
            }
        }

    }

    public void addExistingTasks(){

        DatabaseReference mRef = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://notasana-57563.firebaseio.com/Users/Info/" + User.username + "/Tasks/");

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                taskIDs.clear();

                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                    try {
                        int id = Integer.parseInt(dsp.getKey());
                        String name = (String) dsp.child("Name").getValue();
                        String task = (String) dsp.child("Task").getValue();
                        String deadline = (String) dsp.child("Deadline").getValue();
                        String dateCreated = (String) dsp.child("Date Created").getValue();
                        Boolean sendNotification = Boolean.parseBoolean(dsp.child("Notification").getValue().toString());

                        Task tsk = new Task(name, task, deadline, dateCreated, sendNotification, id);
                        tsk.allTasks.add(tsk);
                        TaskTab.taskAdapter.add(tsk.task);
                    }
                    catch(Exception e){
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void toast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }
}
