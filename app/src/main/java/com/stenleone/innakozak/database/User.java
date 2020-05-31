package com.stenleone.innakozak.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject {

    private String name;

    @PrimaryKey
    private int id;

    public String getTitle() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setTitle(final String name) {
        this.name = name;
    }

    public void setId(final int id) {
        this.id = id;
    }
}


