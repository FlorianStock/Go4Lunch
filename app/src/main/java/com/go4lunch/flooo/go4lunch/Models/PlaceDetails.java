package com.go4lunch.flooo.go4lunch.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PlaceDetails
{

    @SerializedName("result")
    @Expose
    private Results results;

    public class Results
    {
        @SerializedName("international_phone_number")
        @Expose
        private String phone;

        @SerializedName("formatted_address")
        @Expose
        private String adress;

        @SerializedName("website")
        @Expose
        private String website;

        @SerializedName("url")
        @Expose
        private String url;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("photos")
        @Expose
        private List<Photo> photo;

        public List<Photo> getPhoto() { return photo; }
        public String getAdress() { return adress; }
        public String getPhone() { return phone; }
        public String getWebsite() { return website;}
        public String getName() { return name; }
        public String getUrl() { return url; }
    }

    public class  Photo
    {
        @SerializedName("photo_reference")
        @Expose
        private String photoReference;

        public String getphotoReference() { return photoReference; }
    }

    public Results getResults() { return results; }
}
