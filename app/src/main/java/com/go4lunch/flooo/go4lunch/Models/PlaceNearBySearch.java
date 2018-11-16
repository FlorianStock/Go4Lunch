package com.go4lunch.flooo.go4lunch.Models;

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




        public String getName() { return name; }
        public String getIcon() { return icon; }
        public String getId() { return id; }
        public Geometry getGeometry(){return geometry;}

    }

    public class Geometry
    {

        @SerializedName("location")
        @Expose
        private Location location;

        public Location getLocation() { return location; }


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
