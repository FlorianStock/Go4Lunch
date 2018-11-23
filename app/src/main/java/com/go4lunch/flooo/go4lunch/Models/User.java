package com.go4lunch.flooo.go4lunch.Models;

import com.google.firebase.database.annotations.Nullable;

public class User
{
    private String id;
    private String username;
    private Boolean haveChosenRestaurant;

    @Nullable
    private String urlPicture;

    public User() { }

    public User(String uid, String username, String urlPicture)
    {
        this.id = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.haveChosenRestaurant = false;
    }

    // --- GETTERS ---
    public String getid() { return id; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public Boolean gethaveChosenRestaurant() { return haveChosenRestaurant; }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setid(String uid) { this.id = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void sethaveChosenRestaurant(Boolean choice) { haveChosenRestaurant = choice; }
}
