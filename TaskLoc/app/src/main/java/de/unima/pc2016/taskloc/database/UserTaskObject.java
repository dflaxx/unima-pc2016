package de.unima.pc2016.taskloc.database;

import android.location.Location;

import java.util.List;

/**
 * Created by sven on 14.04.16.
 */
public class UserTaskObject {
    private int id;
    private String name;
    private String surname;
    private String age;


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getAge() {
        return age;
    }

    public UserTaskObject(){

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
