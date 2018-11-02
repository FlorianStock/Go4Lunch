package com.go4lunch.flooo.go4lunch;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.Permission;
import java.util.List;


/**

 */

public class MapViewFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private static final String ARG_PARAM1 = "param1";


    private String page;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    private MapView mMapView;

    private GoogleMap googleMap;

    private LocationManager mlocationmanager;
    private LocationListener mlocationlistener;
    private Marker mUserMarker;


    public MapViewFragment()
    {
        // Required empty public constructor
    }

    public static MapViewFragment newInstance()
    {
        MapViewFragment fragment = new MapViewFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mlocationmanager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);

        mlocationlistener = new LocationListener()
        {
            @Override
            public void onLocationChanged(Location location)
            {
                if(googleMap!=null)
                {
                    googleMap.clear();
                    drawMarkerUser(location);
                }
                System.out.println(location.getLatitude()+" "+location.getLongitude());
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle)
            {

            }

            @Override
            public void onProviderEnabled(String s)
            {

            }

            @Override
            public void onProviderDisabled(String s)
            {

            }
        };



        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this.getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);

        }
        else
        {
            mlocationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocationlistener);
        }




        if (getArguments() != null)
        {
            page = getArguments().getString(ARG_PARAM1);

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
        {
            if(ContextCompat.checkSelfPermission(this.getContext(),Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                mlocationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocationlistener);

            }
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

        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setAllGesturesEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setZoomControlsEnabled(true);

        googleMap.setOnMarkerClickListener(this);



    }




        //googleMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        // For dropping a marker at a point on the Map
        //LatLng sydney = googleMap.getMyLocation().;
        //googleMap.addMarker(new MarkerOptions().position().title("Marker Title").snippet("Marker Description"));
        // For zooming automatically to the location of the marker
        //CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
        //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));





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
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 20));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker)
    {
        if(marker.equals(mUserMarker))
        {
            Intent intent = new Intent(this.getActivity(), RestaurantProfile.class);
            intent.putExtra("LoadLayout","Notifications");
            startActivity(intent);
        }

    }


    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


}
