package ca.unb.mobiledev.mapgame.model;

public class User {

    private String email;
    private String username;
    private int points;

    public User(String email) {
        this.email = email;
        this.username = email;
        this.points = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}

