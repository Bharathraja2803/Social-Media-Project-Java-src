/**
 * This is the bean class for post table
 */
package org.bharath.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Post {
    private int postId;
    private int userId;
    private LocalDate postedDate;
    private LocalTime postedTime;
    private String postContent;

    public Post() {
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDate getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(LocalDate postedDate) {
        this.postedDate = postedDate;
    }

    public LocalTime getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(LocalTime postedTime) {
        this.postedTime = postedTime;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", userId=" + userId +
                ", postedDate=" + postedDate +
                ", postedTime=" + postedTime +
                ", postContent='" + postContent + '\'' +
                '}';
    }
}
