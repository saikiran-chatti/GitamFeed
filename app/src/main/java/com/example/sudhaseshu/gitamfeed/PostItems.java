package com.example.sudhaseshu.gitamfeed;


import com.google.firebase.firestore.Exclude;

public class PostItems {
    String post_date, post_month, post_content, post_time, likes;

    @Exclude
    private String id;
    private String pid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Exclude
    private String title;

    public PostItems() {
    } //Added PostItems empty constructor to avoid  "users does not define a no-argument constructor. If you are using ProGuard, make sure these constructors are not stripped." error


    public PostItems(String id, String pid, String post_time, String post_date, String post_month, String title, String post_content, String likes) {

        this.id = id;
        this.pid = pid;
        this.post_date = post_date;
        this.post_month = post_month;
        this.title = title;
        this.post_content = post_content;
        this.post_time = post_time;
        this.likes = likes;

    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost_date() {
        return post_date;
    }

    public void setPost_date(String post_date) {
        this.post_date = post_date;
    }

    public String getPost_month() {
        return post_month;
    }

    public void setPost_month(String post_month) {
        this.post_month = post_month;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }
}
