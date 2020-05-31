package com.stenleone.innakozak.api;

public class PostRepos {

    private String name;

    private String html_url;

    public String getLogin() {
        return name;
    }

    public String getId() {
        return html_url;
    }
}