package se.fortnox.ghstats.github;


import java.util.ArrayList;
import java.util.List;

public class GithubUser {
    private String name;
    private List<String> repos = new ArrayList<>();

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

    public List<String> getRepos() {
        return repos;
    }
}
