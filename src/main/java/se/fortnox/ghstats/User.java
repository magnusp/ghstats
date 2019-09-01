package se.fortnox.ghstats;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class User {
    private String name;

    @JsonProperty("dateOfBirth")
    public LocalDateTime getDob() {
        return dob;
    }

    private LocalDateTime dob;

    public User(String name) {
        this.name = name;
        this.dob = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
