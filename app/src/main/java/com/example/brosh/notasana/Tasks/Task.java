package com.example.brosh.notasana.Tasks;

import android.widget.Toast;

import com.example.brosh.notasana.LoginActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class Task implements Serializable{

    String owner, task, deadline, dateCreated;
    int id;
    Boolean sendNotification;
    public static ArrayList<Task> allTasks = new ArrayList<>();

    public Task(){}

    public Task(String owner, String task, String deadline, String dateCreated, Boolean sendNotification) {

        Random rand = new Random();

        do {
            id = rand.nextInt(100000) + 1;
        } while (TaskTab.taskIDs.contains(id));

        TaskTab.taskIDs.add(id);

        this.owner = owner;
        this.task = task;
        this.deadline = deadline;
        this.dateCreated = dateCreated;
        this.sendNotification = sendNotification;

    }

    public Task(String owner, String task, String deadline, String dateCreated, Boolean sendNotification, int id) {
        this.id = id;
        TaskTab.taskIDs.add(id);
        this.owner = owner;
        this.task = task;
        this.deadline = deadline;
        this.dateCreated = dateCreated;
        this.sendNotification = sendNotification;

    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getSendNotification() {
        return sendNotification;
    }

    public void setSendNotification(Boolean sendNotification) {
        this.sendNotification = sendNotification;
    }
}
