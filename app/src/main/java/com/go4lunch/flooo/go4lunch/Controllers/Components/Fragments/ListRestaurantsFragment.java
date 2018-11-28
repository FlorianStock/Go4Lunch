package com.go4lunch.flooo.go4lunch.Controllers.Components.Fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.go4lunch.flooo.go4lunch.Controllers.ApiFireBase.FireBaseFireStoreCollectionUsers;
import com.go4lunch.flooo.go4lunch.Controllers.ApiGooglePlace.ApiStreamsRequest;
import com.go4lunch.flooo.go4lunch.Controllers.ApiGooglePlace.GooglePlaceServiceAPI;
import com.go4lunch.flooo.go4lunch.Controllers.Components.Adapters.AdapterRecyclerViewListRestaurants;
import com.go4lunch.flooo.go4lunch.Controllers.Components.LocationUser;
import com.go4lunch.flooo.go4lunch.Models.PlaceDetails;
import com.go4lunch.flooo.go4lunch.Models.PlaceNearBySearch;
import com.go4lunch.flooo.go4lunch.Models.User;
import com.go4lunch.flooo.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

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

    ArrayList<PlaceNearBySearch.Results> restaurants;

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
        this.restaurants = new ArrayList<>();
        adapterRecyclerViewListRestaurants = new AdapterRecyclerViewListRestaurants(restaurants, getContext());
        mListRestaurants.setAdapter(adapterRecyclerViewListRestaurants);

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


                disposable = ApiStreamsRequest.searchRestaurant(localization.toString()).subscribeWith(new DisposableObserver<PlaceNearBySearch>()
                {

                    PlaceNearBySearch results;

                    @Override
                    public void onNext(PlaceNearBySearch placeNearBySearch)
                    {

                        results = placeNearBySearch;

                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(e);
                    }

                    @Override
                    public void onComplete()
                    {

                        restaurants.clear();
                        restaurants.addAll(results.getResults());

                        distanceToRestaurants(locationUser);

                        adapterRecyclerViewListRestaurants.notifyDataSetChanged();
                        configureUsers();
                    }
                });


            }
        });


        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    private void configureUsers()
    {
        FireBaseFireStoreCollectionUsers.getUsersCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful())
                {
                    int counterLikeByRestaurant=0;
                    int rating=0;

                    for (int i = 0; i < task.getResult().getDocuments().size(); i++)
                    {
                        User user = task.getResult().getDocuments().get(i).toObject(User.class);

                        for(PlaceNearBySearch.Results result:restaurants)
                        {
                            counterLikeByRestaurant=0;
                            rating=0;
                            System.out.println(result.getId()+" "+user.getPlaceID());
                            //If a user has choice this restaurant to launch, adding in the counter item.
                            if(result.getId().equals(user.getPlaceID())){
                                result.addUserToCounterLaunch();
                            }

                            for(String restaurant:user.getRestaurantsUserLike())
                            {
                                //If a user like a restaurant
                                if(restaurant.equals(result.getId()))
                                {
                                    counterLikeByRestaurant+=1;
                                }
                            }

                            int users = task.getResult().getDocuments().size();

                            if(users!=0){
                                rating = Math.round((counterLikeByRestaurant*3)/users);
                                result.setRatingUsers(rating);
                            }

                        }

                    }

                    adapterRecyclerViewListRestaurants.notifyDataSetChanged();
                }
            }
        });
    }

    private void distanceToRestaurants(Location user)
    {

        for(int i=0;i<restaurants.size();i++)
        {
            double lat = restaurants.get(i).getGeometry().getLocation().lat;
            double lng = restaurants.get(i).getGeometry().getLocation().lng;
            Location locationRestaurant = new Location("position restaurant");
            locationRestaurant.setLatitude(lat);
            locationRestaurant.setLongitude(lng);

            int distance = (int) user.distanceTo(locationRestaurant);
            restaurants.get(i).getGeometry().setDistanceToPoint(distance);

        }

    }

    @Override
    public void currentPositionUserChanged(@Nullable Location locationUser) throws ParseException
    {
        distanceToRestaurants(locationUser);
        adapterRecyclerViewListRestaurants.notifyDataSetChanged();

        //mise a jour de la distance avec les marqueurs
    }
}
