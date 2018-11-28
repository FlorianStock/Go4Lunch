package com.go4lunch.flooo.go4lunch.Models;

import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

public class User
{
    private String id;
    private String username;
    private String  haveChosenRestaurant = "";
    private String placeID = "";
    private ArrayList<String> restaurantsUserLike = new ArrayList<>();

    @Nullable
    private String urlPicture;

    public User() { }

    public User(String uid, String username, String urlPicture)
    {
        this.id = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.haveChosenRestaurant = "";

    }

    // --- GETTERS ---
    public String getid() { return id; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public String gethaveChosenRestaurant() { return haveChosenRestaurant; }
    public String getPlaceID() { return placeID; }
    public ArrayList<String> getRestaurantsUserLike(){return restaurantsUserLike;}

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setid(String uid) { this.id = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void sethaveChosenRestaurant(String choice) { haveChosenRestaurant = choice; }
    public void setPlaceID(String placeID) { this.placeID = placeID; }
    public void setRestaurantsUserLike(ArrayList<String> restaurantUserLike){this.restaurantsUserLike = restaurantUserLike;}
}
