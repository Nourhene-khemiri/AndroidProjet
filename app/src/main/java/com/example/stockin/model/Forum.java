package com.example.stockin.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity(tableName = "forum_table")
public class Forum  implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int bid;
    @ColumnInfo(name = "forum_title")
    private String forumTitle;
    @ColumnInfo(name = "forum_description")

    private String forumDescription;
    @ColumnInfo(name = "forum_username")

    private String forumUserName;
    @ColumnInfo(name = "blog_blog_image")

    private String forumImage;
    @ColumnInfo(name = "forum_userimage")

    private String forumUserImg;

    public Forum(int bid, String forumTitle, String forumDescription, String forumUserName, String forumImage, String forumUserImg) {
        this.bid = bid;
        this.forumTitle = forumTitle;
        this.forumDescription = forumDescription;
        this.forumUserName = forumUserName;
        this.forumImage = forumImage;
        this.forumUserImg = forumUserImg;
    }

    public Forum() {

    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getForumTitle() {
        return forumTitle;
    }

    public void setForumTitle(String forumTitle) {
        this.forumTitle = forumTitle;
    }

    public String getForumDescription() {
        return forumDescription;
    }

    public void setForumDescription(String forumDescription) {
        this.forumDescription = forumDescription;
    }

    public String getForumUserName() {
        return forumUserName;
    }

    public void setForumUserName(String forumUserName) {
        this.forumUserName = forumUserName;
    }

    public String getForumImage() {
        return forumImage;
    }

    public void setForumImage(String forumImage) {
        this.forumImage = forumImage;
    }

    public String getForumUserImg() {
        return forumUserImg;
    }

    public void setForumUserImg(String forumUserImg) {
        this.forumUserImg = forumUserImg;
    }
}