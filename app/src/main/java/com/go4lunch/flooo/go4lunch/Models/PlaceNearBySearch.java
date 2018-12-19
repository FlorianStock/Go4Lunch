package com.go4lunch.flooo.go4lunch.Models;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class PlaceNearBySearch
{

    @SerializedName("results")
    @Expose
    private List<Results> results = new ArrayList<>();

    public class Results
    {
        @SerializedName("geometry")
        @Expose
        private Geometry geometry;

        @SerializedName("icon")
        @Expose
        private String icon;

        @SerializedName("place_id")
        @Expose
        private String id;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("opening_hours")
        @Expose
        private OpeningHours openingHours;

        @SerializedName("photos")
        @Expose
        private List<Photo> photos;

        @SerializedName("vicinity")
        @Expose
        private String vicinity;

        @SerializedName("rating")
        @Expose
        private String rating;

        private PlaceDetails placeDetails;

        private int counterLaunch = 0;
        private int ratingUsers = 0;

        public String getName() { return name; }
        public String getIcon() { return icon; }
        public String getId() { return id; }
        public Geometry getGeometry(){return geometry;}
        public OpeningHours getOpeningHours() { return openingHours; }
        public List<Photo> getPhotos() { return photos; }
        public String getRating() { return rating; }
        public String getVicinity() { return vicinity; }
        public PlaceDetails getPlaceDetails() { return placeDetails; }
        public int getRatingUsers(){return ratingUsers;}
        public int getCounterLaunch(){return counterLaunch;}

        public void setPlaceDetails(PlaceDetails placeDetails){this.placeDetails = placeDetails;}
        public void addUserToCounterLaunch( ){this.counterLaunch+=1;}
        public void setRatingUsers(int ratingUsers){this.ratingUsers=ratingUsers;}
        public void setCounterLaunch(int counterLaunch) { this.counterLaunch = counterLaunch; }
    }


    public class Photo
    {
        @SerializedName("photo_reference")
        @Expose
        private String photoReference;

        public String getphotoReference() { return photoReference; }
    }

    public class OpeningHours
    {
        @SerializedName("open_now")
        @Expose
        private Boolean openNow;

        public Boolean getOpenNow() { return openNow; }
    }

    public class Geometry
    {

        @SerializedName("location")
        @Expose
        private Location location;

        private int distanceToPoint;

        public Location getLocation() { return location; }
        public int getDistanceToPoint() { return distanceToPoint; }
        public void setDistanceToPoint(int distanceToPoint) { this.distanceToPoint = distanceToPoint; }
    }

    public class Location
    {
        @SerializedName("lng")
        @Expose
        public double lng;

        @SerializedName("lat")
        @Expose
        public double lat;

        public double getLat() { return lat; }
        public double getLng() { return lng; }
    }

    public List<Results> getResults()
    {
        return results;
    }
}
