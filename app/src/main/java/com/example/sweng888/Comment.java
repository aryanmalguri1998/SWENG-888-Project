package com.example.sweng888;

/**
 * This class represents a comment with an ID and text.
 */
public class Comment {

    // Unique identifier for the comment
    private String commentId;
    
    // Text content of the comment
    private String commentText;

    /**
     * Default constructor required for calls to DataSnapshot.getValue(Comment.class).
     * Firebase Realtime Database requires a no-argument constructor.
     */
    public Comment() {
        // Default constructor
    }

    /**
     * Constructor to create a Comment object with specified ID and text.
     * 
     * @param commentId  the unique identifier of the comment
     * @param commentText  the text content of the comment
     */
    public Comment(String commentId, String commentText) {
        this.commentId = commentId;
        this.commentText = commentText;
    }

    /**
     * Gets the comment ID.
     * 
     * @return the comment ID
     */
    public String getCommentId() {
        return commentId;
    }

    /**
     * Sets the comment ID.
     * 
     * @param commentId  the comment ID to set
     */
    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    /**
     * Gets the text content of the comment.
     * 
     * @return the comment text
     */
    public String getCommentText() {
        return commentText;
    }

    /**
     * Sets the text content of the comment.
     * 
     * @param commentText  the comment text to set
     */
    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }
}
