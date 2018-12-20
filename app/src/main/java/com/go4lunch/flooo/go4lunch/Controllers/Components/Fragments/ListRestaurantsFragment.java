package com.go4lunch.flooo.go4lunch.Controllers.Components.Fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.go4lunch.flooo.go4lunch.Controllers.ApiFireBase.FireBaseFireStoreCollectionUsers;
import com.go4lunch.flooo.go4lunch.Controllers.ApiGooglePlace.ApiStreamsRequest;
import com.go4lunch.flooo.go4lunch.Controllers.Components.Adapters.AdapterRecyclerViewListRestaurants;
import com.go4lunch.flooo.go4lunch.Controllers.Components.LocationUser;
import com.go4lunch.flooo.go4lunch.Models.PlaceDetails;
import com.go4lunch.flooo.go4lunch.Models.PlaceNearBySearch;
import com.go4lunch.flooo.go4lunch.Models.User;
import com.go4lunch.flooo.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;


public class ListRestaurantsFragment extends Fragment implements LocationUser.CallBacks {

    //@BindView(R.id.listRestaurants)
    RecyclerView mListRestaurants;

    private   ArrayList<PlaceNearBySearch.Results> restaurants = new ArrayList<>();

    private AdapterRecyclerViewListRestaurants adapterRecyclerViewListRestaurants;

    private static ListRestaurantsFragment fragment;

    private  PlaceNearBySearch results;

    private Disposable disposable;
    private Disposable disposableDetailRestaurants;
    private Context context;
    private Location location;

    public static ListRestaurantsFragment newInstance()
    {
        fragment = new ListRestaurantsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static ListRestaurantsFragment getInstance()
    {
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        context = this.getContext();

        PlaceNearBySearch results = new PlaceNearBySearch();
        results.getResults().clear();

    }

    public void refresh()
    {
        configureUsers();
        adapterRecyclerViewListRestaurants.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_restaurants, container, false);

        this.mListRestaurants = view.findViewById(R.id.listRestaurants);
        mListRestaurants.setLayoutManager(new LinearLayoutManager(container.getContext()));
        adapterRecyclerViewListRestaurants = new AdapterRecyclerViewListRestaurants(restaurants, getContext());
        mListRestaurants.setAdapter(adapterRecyclerViewListRestaurants);

        new LocationUser(this.getContext(),this.getActivity(),0,0,this);

        new LocationUser(this.getContext(), this.getActivity(),50,0, new LocationUser.CallBacks()
        {
            @Override
            public void currentPositionUserChanged(@Nullable final Location locationUser) throws ParseException
            {

                location=locationUser;

                StringBuilder localisation = new StringBuilder();
                localisation.append(locationUser.getLatitude());
                localisation.append(",");
                localisation.append(locationUser.getLongitude());

                disposable = ApiStreamsRequest.searchRestaurant(localisation.toString()).subscribeWith(new DisposableObserver<PlaceNearBySearch>()
                {



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


                        getDetailsForRestaurants(locationUser);

                    }
                });


            }
        });



        // Inflate the layout for this fragment
        return view;

    }



    private void getDetailsForRestaurants(final Location location)
    {

        for(final PlaceNearBySearch.Results result:results.getResults())
        {


        disposableDetailRestaurants = ApiStreamsRequest.searchDetailsRestaurant(result.getId()).subscribeWith(new DisposableObserver<PlaceDetails>()
        {

            PlaceDetails emptyPlaceDetails;

            @Override
            public void onNext(PlaceDetails placeDetails)
            {

                emptyPlaceDetails = placeDetails;

            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e);
            }

            @Override
            public void onComplete()
            {

                result.setPlaceDetails(emptyPlaceDetails);


                if(result == results.getResults().get(results.getResults().size()-1)){
                    restaurants.clear();
                    restaurants.addAll(results.getResults());

                    distanceToRestaurants(location);
                    configureUsers();


                }

            }
        });

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    private void configureUsers()
    {

        for(PlaceNearBySearch.Results result:restaurants) {
            result.setCounterLaunch(0);
            result.setRatingUsers(0);
        }

        for(PlaceNearBySearch.Results result:results.getResults()) {
            result.setCounterLaunch(0);
            result.setRatingUsers(0);
        }

        FireBaseFireStoreCollectionUsers.getUsersCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful())
                {

                    Iterator<PlaceNearBySearch.Results> restaurantsI = restaurants.iterator();


                        for(PlaceNearBySearch.Results result:restaurants)
                        {
                            result.setCounterLaunch(0);
                            result.setRatingUsers(0);
                            int counterLaunch=0;
                            int counterLikeByRestaurant=0;
                            int users = task.getResult().getDocuments().size();

                            for (int i = 0; i < task.getResult().getDocuments().size(); i++)
                            {
                                User user = task.getResult().getDocuments().get(i).toObject(User.class);

                                //If a user has choice this restaurant to eat, adding in the counter item.
                                if(user.getPlaceID().equals(result.getId()))
                                {
                                    counterLaunch+=1;
                                }

                                Iterator<String> restaurantI = user.getRestaurantsUserLike().iterator();

                                while (restaurantI.hasNext())
                                {
                                String str = restaurantI.next();

                                //If a user like a restaurant
                                if(str.equals(result.getId()))
                                {
                                    counterLikeByRestaurant+=1;

                                }
                            }

                                if(users!=0)
                                {
                                    int rating = Math.round((counterLikeByRestaurant*3)/users);
                                    result.setRatingUsers(rating);

                                }

                            }





                            result.setCounterLaunch(counterLaunch);

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
    }

}
