package com.go4lunch.flooo.go4lunch.Models;

import android.util.Pair;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PlaceDetails
{

    @SerializedName("result")
    @Expose
    private Results results = new Results();

    public class Results
    {
        @SerializedName("international_phone_number")
        @Expose
        private String phone;

        @SerializedName("formatted_address")
        @Expose
        private String adress="";

        @SerializedName("website")
        @Expose
        private String website;

        @SerializedName("url")
        @Expose
        private String url;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("place_id")
        @Expose
        private String id;

        @SerializedName("photos")
        @Expose
        private List<Photo> photo;

        @SerializedName("opening_hours")
        @Expose
        private OpeningHours openingHours;

        @SerializedName("permanently_closed")
        @Expose
        private Boolean closed = false;


        public List<Photo> getPhoto() { return photo; }
        public String getAdress() { return adress; }
        public String getPhone() { return phone; }
        public String getWebsite() { return website;}
        public String getName() { return name; }
        public String getUrl() { return url; }
        public String getId() { return id; }
        public Boolean getClosed(){return closed;}
        public OpeningHours getOpeningHours() { return openingHours; }


        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

    public class OpeningHours
    {
        @SerializedName("open_now")
        @Expose
        private Boolean open = false;

        @SerializedName("periods")
        @Expose
        private ArrayList<Period> periods;

        public Boolean getOpen() { return open;}
        public ArrayList<Period> getPeriods() { return periods; }
    }


    public class Period
    {
        @SerializedName("open")
        @Expose
        private Pair<Integer, Integer> DayandTimeOpen;

        @SerializedName("close")
        @Expose
        private Pair<Integer, Integer> DayandTimeClosed;

        public Pair<Integer, Integer> getDayandTimeClosed() { return DayandTimeClosed; }
        public Pair<Integer, Integer> getDayandTimeOpen() { return DayandTimeOpen; }
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
