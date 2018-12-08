package ca.uoit.flip_flop.flipflop;

import java.io.Serializable;

public class Comment implements Serializable{
    private int commentId;
    private int commentNumber;
    private int postId;
    private int userId;
    private String comment;
    private String commenterName;

    /**
     * Getter / Setters for Comment Class
     */

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getUserId() { return userId; }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getPostId() { return postId; }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getCommenterName() { return commenterName; }

    public void setCommenterName(String commenterName) { this.commenterName = commenterName; }

    public int getCommentNumber() { return commentNumber; }

    public void setCommentNumber(int commentNumber) { this.commentNumber = commentNumber; }
}
