package com.dennis.simplealbum.po;

import java.sql.Date;
import java.util.List;

public class entryVo {

    private int num_entry;

    private String username;

    private String message;

    private Date time;

    private List<image> images;

    public List<image> getImages() {
        return images;
    }

    public void setImages(List<image> images) {
        this.images = images;
    }

    public int getNum_entry() {
        return num_entry;
    }

    public void setNum_entry(int num_entry) {
        this.num_entry = num_entry;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        return "entryVo{" +
                "num_entry=" + num_entry +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                ", images=" + images +
                '}';
    }
}
