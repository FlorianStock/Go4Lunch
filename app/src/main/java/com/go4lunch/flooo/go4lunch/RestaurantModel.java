package com.go4lunch.flooo.go4lunch;

import java.util.List;

public class RestaurantModel
{
    private String name;
    private String adress;
    private String imageUrl;
    private String webSite;
    private String phoneNumer;
    private List<Colleague> colleaguesLicked;

    private int longitude;
    private int latitude;

    public void setInformations(String name,String adress, String imageUrl, String webSite, String phoneNumber, List<Colleague> colleaguesLicked)
    {
        this.name = name;
        this.adress = adress;
        this.imageUrl = imageUrl;
        this.webSite = webSite;
        this.phoneNumer = phoneNumber;
        this.colleaguesLicked = colleaguesLicked;
    }
}


