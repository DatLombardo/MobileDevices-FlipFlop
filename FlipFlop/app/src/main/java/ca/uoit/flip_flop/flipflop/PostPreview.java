package ca.uoit.flip_flop.flipflop;

import java.util.Date;

public class PostPreview {

    private int postId;
    private String title;
    private String posterName;
    private String contents;
    private Date date;
    private int reputation;

    public PostPreview(int postId, String title, String posterName, String contents, int reputation) {
        this.postId = postId;
        this.title = title;
        this.posterName = posterName;
        this.contents = contents;
        this.reputation = reputation;
    }

    public int getPostId() {
        return postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterName() {
        return posterName;
    }

    public void setPosterName(String username) {
        this.posterName = username;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }
}