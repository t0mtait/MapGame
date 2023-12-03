package ca.unb.mobiledev.mapgame.model;

import java.util.List;

public class User {

    private String email;
    private String username;
    private int points;
    List<Integer> solvedChallenges;

    public User()
    {

    }



    public User(String email)
    {
        this.email = email;
        this.username = email;
        this.points = 0;
        this.solvedChallenges = null;
    }


    public String getUsername() {
        return username;
    }

    public String getEmail() { return email; }

    public List<Integer> getSolvedChallenges() { return this.solvedChallenges; }

    public void setUsername(String username) { this.username = username; }

    public int getPoints() {
        return points;
    }

    public void setEmail(String email) { this.email = email; }
    public void setSolved(List<Integer> solved) { this.solvedChallenges = solved; }
    public void setPoints(int points) {
        this.points = points;
    }
}

