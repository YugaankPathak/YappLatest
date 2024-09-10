package com.example.userloginsqlite;

public class Item {
    private String title;
    private int imageResourceId;
    private int iconResourceId;
    private int likes;
    boolean invokedOnce=false;
    public Item(String title, int imageResourceId, int iconResourceId,int likes) {
        this.title = title;
        this.imageResourceId = imageResourceId;
        this.iconResourceId = iconResourceId;
        this.likes=likes;
    }

    // Getters and setters
    public String getTitle() {
        return title;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public int getLikes() {
        return likes;
    }

    public void incLikes() {
        if(invokedOnce==false) this.likes = this.likes+1;
        invokedOnce=true;
    }

    public void decLikes() {
       if(this.likes>0) this.likes = this.likes-1;
       invokedOnce=false;
    }

}
