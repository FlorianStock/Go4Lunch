package com.go4lunch.flooo.go4lunch.Controllers.Components;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.go4lunch.flooo.go4lunch.Controllers.Activities.RestaurantProfileActivity;
import com.go4lunch.flooo.go4lunch.Controllers.ApiFireBase.FireBaseFireStoreCollectionUsers;
import com.go4lunch.flooo.go4lunch.Controllers.ApiGooglePlace.GooglePlaceServiceAPI;
import com.go4lunch.flooo.go4lunch.Models.PlaceNearBySearch;
import com.go4lunch.flooo.go4lunch.Models.User;
import com.go4lunch.flooo.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecyclerViewListRestaurants extends RecyclerView.ViewHolder implements View.OnClickListener
{

    private ArrayList<PlaceNearBySearch.Results> placeNearBySearch;

    @BindView(R.id.nameRestaurant)
    TextView mRestaurantTitle;
    @BindView(R.id.adressRestaurant)
    TextView mAdressRestaurant;
    @BindView(R.id.openingHours)
    TextView mOpeningHours;
    @BindView(R.id.imageViewRestaurantList)
    ImageView mImageRestaurant;
    @BindView(R.id.distanceText)
    TextView mDistanceText;
    @BindView(R.id.number_poeple_gone)
    TextView mNumberPoepleLaunch;
    @BindView(R.id.launch_info)
    LinearLayout launchUi;
    @BindView(R.id.star_1)
    ImageView starImage1;
    @BindView(R.id.star_2)
    ImageView starImage2;
    @BindView(R.id.star_3)
    ImageView starImage3;

    private String placeId;
    private Context context;


    public RecyclerViewListRestaurants(View itemView,ArrayList<PlaceNearBySearch.Results> results,Context context)
    {
        super(itemView);
        ButterKnife.bind(this, itemView);

        this.placeNearBySearch = results;
        this.context=context;

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view)
    {

            Intent intent = new Intent(context, RestaurantProfileActivity.class);
            intent.putExtra("ID",placeId);
            context.startActivity(intent);
    }

    public void updateView(PlaceNearBySearch.Results position)
    {
        placeId = position.getId();


        mRestaurantTitle.setText(position.getName());
        mAdressRestaurant.setText(position.getPlaceDetails().getResults().getAdress());
        String distanceText = String.valueOf(position.getGeometry().getDistanceToPoint())+"m";
        mDistanceText.setText(distanceText);

        String url;
        if(position.getPhotos()!=null)
        {
            String reference = position.getPhotos().get(0).getphotoReference();
            url = GooglePlaceServiceAPI.baseUrl+"photo?maxwidth=50&photoreference="+reference+"&key="+GooglePlaceServiceAPI.KEY_API_MAPS;
        }
        else
        {
            url = context.getResources().getString(R.string.restaurant_random_2);
        }
        Picasso.with(context).load(url).resize(50, 50).centerCrop().into(mImageRestaurant);



        mOpeningHours.setText(position.getPlaceDetails().getResults().getWebsite());



        int rating = position.getRatingUsers();
        starImage1.setVisibility(View.INVISIBLE);
        starImage2.setVisibility(View.INVISIBLE);
        starImage3.setVisibility(View.INVISIBLE);
        if(rating>0){starImage1.setVisibility(View.VISIBLE);}
        if(rating>1){starImage2.setVisibility(View.VISIBLE);}
        if(rating>2){starImage3.setVisibility(View.VISIBLE);}

        int goForLaunch = position.getCounterLaunch();
        if(goForLaunch!=0){
            launchUi.setVisibility(View.VISIBLE);
            String txt ="("+String.valueOf(goForLaunch)+")";
            mNumberPoepleLaunch.setText(txt);
        }

    }

}
