package com.go4lunch.flooo.go4lunch.Controllers.ApiGooglePlace;

import com.go4lunch.flooo.go4lunch.Models.PlaceDetails;
import com.go4lunch.flooo.go4lunch.Models.PlaceNearBySearch;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ApiStreamsRequest
{


        public static Observable<PlaceNearBySearch>searchRestaurant(String location)
        {
            GooglePlaceServiceAPI playService = GooglePlaceServiceAPI.retrofit.create(GooglePlaceServiceAPI.class);

            return playService.getPlaceNearBySearch(location)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .timeout(10, TimeUnit.SECONDS);
        }

        public static Observable<PlaceDetails>searchDetailsRestaurant(String id)
        {

            GooglePlaceServiceAPI playService = GooglePlaceServiceAPI.retrofit.create(GooglePlaceServiceAPI.class);

            return playService.getPlaceDetails(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .timeout(10, TimeUnit.SECONDS);
        }





}
