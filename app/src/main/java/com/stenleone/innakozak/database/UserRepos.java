package com.stenleone.innakozak.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserRepos extends RealmObject {

    private String key;
    @PrimaryKey
    private String name;

    private String url;

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getKey() {
        return key;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public void setKey(final String key) {
        this.key = key;
    }
}
