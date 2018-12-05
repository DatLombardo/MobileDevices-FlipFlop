package ca.uoit.flip_flop.flipflop;

import java.io.Serializable;

public class User implements Serializable{
    private int userId;
    private String username;
    private String password;
    //private String iconFileName;
    private String dateCreated;

    /*
    Date todayDate = Calendar.getInstance().getTime();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
    String todayString = formatter.format(todayDate);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
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

    /*public String getIconFileName() {
        return iconFileName;
    }

    public void setIconFileName(String iconFileName) {
        this.iconFileName = iconFileName;
    }
    */
    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
    /*
    public int[] getLikes() {
        return likes;
    }

    public void setLikes(int[] likes) {
        this.likes = likes;
    }


    public int[] getPosts() {
        return posts;
    }

    public void setPosts(int[] posts) {
        this.posts = posts;
    }
    */
}
