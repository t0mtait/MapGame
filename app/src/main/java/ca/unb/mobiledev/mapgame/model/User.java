package ca.unb.mobiledev.mapgame.model;

public class User {

    private String email;
    private String username;
    private String points;

    private int[] solvedChallenges;

    public User()
    {

    }

    public User(String email) {
        this.email = email;
        this.username = email;
        this.points = "0";
        this.solvedChallenges = null;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }
}

