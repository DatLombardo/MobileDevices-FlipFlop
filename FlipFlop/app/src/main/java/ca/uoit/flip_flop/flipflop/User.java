package ca.uoit.flip_flop.flipflop;

import java.io.Serializable;

public class User implements Serializable{
    private int userId;
    private String username;
    private String password;
    private String dateCreated;

    /**
     * Getter / Setters for User Class
     */

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
