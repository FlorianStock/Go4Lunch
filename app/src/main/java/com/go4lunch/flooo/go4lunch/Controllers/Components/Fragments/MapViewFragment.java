package com.go4lunch.flooo.go4lunch.Controllers.Components.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.go4lunch.flooo.go4lunch.Controllers.ApiFireBase.FireBaseFireStoreCollectionUsers;
import com.go4lunch.flooo.go4lunch.Controllers.Components.LocationUser;
import com.go4lunch.flooo.go4lunch.Models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.go4lunch.flooo.go4lunch.Controllers.Activities.RestaurantProfileActivity;
import com.go4lunch.flooo.go4lunch.Controllers.ApiGooglePlace.ApiStreamsRequest;
import com.go4lunch.flooo.go4lunch.Models.PlaceNearBySearch;
import com.go4lunch.flooo.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;

import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class MapViewFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener, LocationUser.CallBacks {

    private static final String ARG_PARAM1 = "param1";


    private String page;

    private MapView mMapView;
    private GoogleMap googleMap;
    private LocationManager mlocationmanager;
    private LocationListener mlocationlistener;
    private Marker mUserMarker;
    private Marker mRestaurantMarker;

    private static MapViewFragment fragment;

    //FOR DATA
    private Location location;
    public Disposable disposable;
    public PlaceNearBySearch restaurants;

    public static MapViewFragment newInstance()
    {
        fragment = new MapViewFragment();
        return fragment;
    }

    public static MapViewFragment getInstance()
    {
        return fragment;
    }

    public void refresh()
    {
        googleMap.clear();
        drawMarkerUser(location);
        searchRestaurant(location);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //int distance = (int) location.distanceTo(placeLocation);

        if (getArguments() != null)
        {
            page = getArguments().getString(ARG_PARAM1);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_map_view, container, false);

        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        mMapView.getMapAsync(this);

        return rootView;
    }




    @Override
    public void onDetach()
    {
        super.onDetach();

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
       this.googleMap = googleMap;

        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        googleMapOptions.scrollGesturesEnabled(true);

        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getContext(), R.raw.mapstyle));
            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }

        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        googleMap.setOnMarkerClickListener(this);

         new LocationUser(this.getContext(),this.getActivity(),10,0,this);

    }


    private void drawMarkerUser(Location location)
    {
        if (googleMap != null)
        {

            BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_action_user);

            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
            mUserMarker = googleMap.addMarker(new MarkerOptions()
                    .position(gps)
                    .icon(icon)
                    .title("Current Position"));
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 13));

        }
    }

    private void drawMarkerRestaurant(final Location location, final String name, final String id)
    {
        if (googleMap != null)
        {


            FireBaseFireStoreCollectionUsers.getUsersCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    if (task.isSuccessful())
                    {
                        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_red);

                        for(int i=0;i<task.getResult().getDocuments().size();i++)
                        {
                            User user = task.getResult().getDocuments().get(i).toObject(User.class);

                            if(user.getPlaceID().equals(id))
                            {

                                icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_restaurant_green);
                                break;
                            }
                        }

                        LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());
                        mRestaurantMarker = googleMap.addMarker(new MarkerOptions()
                                .position(gps)
                                .icon(icon)
                                .title(name));

                        mRestaurantMarker.setSnippet(id);
                    }



                }});



            //googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 13));

        }
    }

    private void drawRestaurants(PlaceNearBySearch restaurants)
    {
        for (int i=0;i<restaurants.getResults().size();i++)
        {

            Location location = new Location("location_restaurant");
            location.setLongitude(restaurants.getResults().get(i).getGeometry().getLocation().lng);
            location.setLatitude(restaurants.getResults().get(i).getGeometry().getLocation().lat);

            String nameRestaurant = restaurants.getResults().get(i).getName();
            String idRestaurant = restaurants.getResults().get(i).getId();

            drawMarkerRestaurant(location,nameRestaurant,idRestaurant);

            //System.out.println(location+nameRestaurant);
        }
    }





    @Override
    public boolean onMarkerClick(Marker marker)
    {
        if(marker.equals(mUserMarker))
        {

        }
        else
        {

            Intent intent = new Intent(this.getActivity(), RestaurantProfileActivity.class);
            intent.putExtra("ID",marker.getSnippet());
            startActivity(intent);
            return true;
        }

        return false;
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    // 1 - Execute our Stream
    private void searchRestaurant(Location location)
    {

        StringBuilder localization = new StringBuilder();
        localization.append(location.getLatitude());
        localization.append(",");
        localization.append(location.getLongitude());

        // 1.2 - Execute the stream subscribing to Observable defined inside GithubStream
        this.disposable = ApiStreamsRequest.searchRestaurant(localization.toString()).subscribeWith(new DisposableObserver<PlaceNearBySearch>()
        {

            @Override
            public void onNext(PlaceNearBySearch placeNearBySearches)
            {
                //Location location = new Location("location restaurant");
                //location.setLatitude(placeNearBySearches.getResults().get(0).getGeometry().getLocation().getLat());
                //location.setLongitude(placeNearBySearches.getResults().get(0).getGeometry().getLocation().getLng());

               //drawMarkerRestaurant(location,placeNearBySearches.getResults().get(0).getName());
                restaurants = placeNearBySearches;

            }

            @Override
            public void onError(Throwable e)
            {
                System.out.println(e);
            }

            @Override
            public void onComplete()
            {


                //System.out.println(restaurants.getResults().get(0).getGeometry().get(0).getLocation().get(0).lat);
                //System.out.println(restaurants.getResults().size());

                drawRestaurants(restaurants);
            }
        });
    }


    @Override
    public void currentPositionUserChanged(@Nullable Location locationUser) throws ParseException
    {
        if(locationUser!=null)
        {
            location=locationUser;
            googleMap.clear();
            drawMarkerUser(locationUser);
            searchRestaurant(locationUser);
        }
    }
}
