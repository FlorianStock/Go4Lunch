package com.go4lunch.flooo.go4lunch.Controllers.Components;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.go4lunch.flooo.go4lunch.Controllers.ApiGooglePlace.GooglePlaceServiceAPI;
import com.go4lunch.flooo.go4lunch.Models.PlaceNearBySearch;
import com.go4lunch.flooo.go4lunch.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

public class RecyclerViewListRestaurants extends RecyclerView.ViewHolder
{

    private PlaceNearBySearch placeNearBySearch;

    TextView mRestaurantTitle;
    TextView mAdressRestaurant;
    TextView mOpeningHours;
    ImageView mImageRestaurant;
    TextView mDistanceText;

    public RecyclerViewListRestaurants(View itemView,PlaceNearBySearch results)
    {
        super(itemView);

        mRestaurantTitle = itemView.findViewById(R.id.nameRestaurant);
        mAdressRestaurant = itemView.findViewById(R.id.adressRestaurant);
        mOpeningHours = itemView.findViewById(R.id.openingHours);
        mImageRestaurant = itemView.findViewById(R.id.imageViewRestaurantList);
        mDistanceText = itemView.findViewById(R.id.distanceText);

        this.placeNearBySearch = results;
    }

    public void updateView(int position, Context context)
    {

        mRestaurantTitle.setText(placeNearBySearch.getResults().get(position).getName());
        mAdressRestaurant.setText(placeNearBySearch.getResults().get(position).getVicinity());
        String distanceText = String.valueOf(placeNearBySearch.getResults().get(position).getGeometry().getDistanceToPoint())+"m";
        mDistanceText.setText(distanceText);

        String url;
        if(placeNearBySearch.getResults().get(position).getPhotos()!=null)
        {
            String reference = placeNearBySearch.getResults().get(position).getPhotos().get(0).getphotoReference();
            url = GooglePlaceServiceAPI.baseUrl+"photo?maxwidth=50&photoreference="+reference+"&key="+GooglePlaceServiceAPI.KEY_API_MAPS;
        }
        else
        {
            url = context.getResources().getString(R.string.restaurant_random_2);
        }
        Picasso.with(context).load(url).resize(50, 50).centerCrop().into(mImageRestaurant);

    }


}
