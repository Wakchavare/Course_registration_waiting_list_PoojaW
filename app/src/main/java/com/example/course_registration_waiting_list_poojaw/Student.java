package com.example.course_registration_waiting_list_poojaw;

import java.io.Serializable;

public class Student implements Serializable {
    private int id;
    private String name;
    private String priority;

    public Student(int id, String name, String priority) {
        this.id = id;
        this.name = name;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}

