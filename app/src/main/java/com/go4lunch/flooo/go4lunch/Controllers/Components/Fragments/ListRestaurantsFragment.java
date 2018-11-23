package com.go4lunch.flooo.go4lunch.Controllers.Components.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.go4lunch.flooo.go4lunch.Controllers.ApiGooglePlace.ApiStreamsRequest;
import com.go4lunch.flooo.go4lunch.Controllers.ApiGooglePlace.GooglePlaceServiceAPI;
import com.go4lunch.flooo.go4lunch.Controllers.Components.Adapters.AdapterRecyclerViewListRestaurants;
import com.go4lunch.flooo.go4lunch.Controllers.Components.LocationUser;
import com.go4lunch.flooo.go4lunch.Models.PlaceDetails;
import com.go4lunch.flooo.go4lunch.Models.PlaceNearBySearch;
import com.go4lunch.flooo.go4lunch.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


public class ListRestaurantsFragment extends Fragment implements LocationUser.CallBacks {

    //@BindView(R.id.listRestaurants)
    RecyclerView mListRestaurants;

    PlaceNearBySearch restaurants;

    private AdapterRecyclerViewListRestaurants adapterRecyclerViewListRestaurants;

    private Disposable disposable;
    private Disposable disposableDetailRestaurants;
    private Context context;




    public static ListRestaurantsFragment newInstance()
    {
        ListRestaurantsFragment fragment = new ListRestaurantsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = this.getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_restaurants, container, false);

        this.mListRestaurants = view.findViewById(R.id.listRestaurants);
        mListRestaurants.setLayoutManager(new LinearLayoutManager(container.getContext()));
        this.restaurants = new PlaceNearBySearch();


        new LocationUser(this.getContext(),this.getActivity(),0,0,this);

        new LocationUser(this.getContext(), this.getActivity(),50,0, new LocationUser.CallBacks()
        {
            @Override
            public void currentPositionUserChanged(@Nullable final Location locationUser) throws ParseException
            {

                StringBuilder localization = new StringBuilder();
                localization.append(locationUser.getLatitude());
                localization.append(",");
                localization.append(locationUser.getLongitude());

                disposable = ApiStreamsRequest.searchRestaurantsAndDetails(localization.toString()).subscribeWith(new DisposableObserver<PlaceNearBySearch>()
                {
                    @Override
                    public void onNext(PlaceNearBySearch placeNearBySearch)
                    {

                        restaurants = placeNearBySearch;
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e);
                    }

                    @Override
                    public void onComplete()
                    {

                        System.out.println("REQUEST");

                        distanceToRestaurants(locationUser);

                        //requestDetailsRestaurants();

                        if(adapterRecyclerViewListRestaurants==null)
                        {
                            adapterRecyclerViewListRestaurants = new AdapterRecyclerViewListRestaurants(restaurants, getContext());
                            mListRestaurants.setAdapter(adapterRecyclerViewListRestaurants);
                        }
                        else
                        {
                            adapterRecyclerViewListRestaurants.notifyDataSetChanged();
                        }

                    }
                });


            }
        });


        // Inflate the layout for this fragment
        return view;

    }




    private void distanceToRestaurants(Location user)
    {

        for(int i=0;i<restaurants.getResults().size();i++)
        {
            double lat = restaurants.getResults().get(i).getGeometry().getLocation().lat;
            double lng = restaurants.getResults().get(i).getGeometry().getLocation().lng;
            Location locationRestaurant = new Location("position restaurant");
            locationRestaurant.setLatitude(lat);
            locationRestaurant.setLongitude(lng);

            int distance = (int) user.distanceTo(locationRestaurant);
            restaurants.getResults().get(i).getGeometry().setDistanceToPoint(distance);

        }

    }

    @Override
    public void currentPositionUserChanged(@Nullable Location locationUser) throws ParseException
    {



        //mise a jour de la distance avec les marqueurs
    }
}
