package com.go4lunch.flooo.go4lunch.Controllers.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.go4lunch.flooo.go4lunch.Controllers.ApiGooglePlace.ApiStreamsRequest;
import com.go4lunch.flooo.go4lunch.Controllers.ApiGooglePlace.GooglePlaceServiceAPI;
import com.go4lunch.flooo.go4lunch.Models.PlaceDetails;
import com.go4lunch.flooo.go4lunch.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

public class RestaurantProfileActivity extends AppCompatActivity {

    @BindView(R.id.textViewNameRestaurant)
    TextView mRestaurantTitle;
    @BindView(R.id.textViewAdress)
    TextView mRestaurantAdress;
    @BindView(R.id.imageViewRestaurant)
    ImageView viewRestaurant;
    @BindView(R.id.floatingActionBar)
    FloatingActionButton floatingActionBar;

    public Disposable disposable;
    public PlaceDetails details;
    public Context context;
    private String phoneNumber;
    private String website;

    public RestaurantProfileActivity() {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_profile);
        ButterKnife.bind(this);

        context = this.getApplicationContext();

        String id = getIntent().getStringExtra("ID");
        System.out.println(id);

        this.disposable = ApiStreamsRequest.searchDetailsRestaurant(id).subscribeWith(new DisposableObserver<PlaceDetails>() {

            @Override
            public void onNext(PlaceDetails placeDetails)
            {
                details = placeDetails;
            }

            @Override
            public void onError(Throwable e) {
                System.out.println(e);
            }

            @Override
            public void onComplete()
            {

                String urlImage;

                if(details.getResults().getPhoto()!=null)
                {
                    String reference = details.getResults().getPhoto().get(0).getphotoReference();
                    String widthImage = Integer.toString(viewRestaurant.getWidth());
                    urlImage = GooglePlaceServiceAPI.baseUrl+"photo?maxwidth="+widthImage+"&photoreference="+reference+"&key="+GooglePlaceServiceAPI.KEY_API_MAPS;

                }
                else
                {
                    urlImage = getString(R.string.restaurant_random_1);
                }

                //Picasso.with(context).load(urlImage).into(viewRestaurant);
                Picasso.with(context).load(urlImage).resize(viewRestaurant.getWidth(), viewRestaurant.getHeight()).centerCrop().into(viewRestaurant);

                mRestaurantTitle.setText(details.getResults().getName());
                mRestaurantAdress.setText(details.getResults().getAdress());

                phoneNumber = details.getResults().getPhone();
                website = details.getResults().getWebsite();

            }
        });


    }


    @OnClick(R.id.floatingActionBar)
    public void clickActionBar()
    {
        Toast.makeText(getApplicationContext(), "je mange ici", Toast.LENGTH_SHORT).show();
        floatingActionBar.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        floatingActionBar.setRippleColor(Color.GREEN);
    }

    @OnClick(R.id.callAction)
    public void clickCallButton()
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+phoneNumber));
        startActivity(intent);
    }
    @OnClick(R.id.likeAction)
    public void clickLikeButton()
    {
        Toast.makeText(getApplicationContext(), "enregistré en favori", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.webSiteAction)
    public void clickWebSiteButton()
    {
        Intent intent = new Intent(this.getApplication().getBaseContext(), WebSiteRestaurantActivity.class);
        intent.putExtra("URL",website);
        startActivity(intent);
    }


}
