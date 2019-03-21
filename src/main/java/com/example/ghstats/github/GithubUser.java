package com.example.ghstats.github;


import java.time.LocalDateTime;

public class GithubUser {
    private String name;

    public GithubUser() {

    }

    public GithubUser(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
