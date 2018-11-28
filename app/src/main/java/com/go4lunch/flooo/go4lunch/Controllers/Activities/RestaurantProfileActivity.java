package com.go4lunch.flooo.go4lunch.Controllers.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.go4lunch.flooo.go4lunch.Controllers.ApiFireBase.FireBaseFireStoreCollectionUsers;
import com.go4lunch.flooo.go4lunch.Controllers.ApiGooglePlace.ApiStreamsRequest;
import com.go4lunch.flooo.go4lunch.Controllers.ApiGooglePlace.GooglePlaceServiceAPI;
import com.go4lunch.flooo.go4lunch.Controllers.Components.Adapters.AdapterRecyclerViewListUsers;
import com.go4lunch.flooo.go4lunch.Models.PlaceDetails;
import com.go4lunch.flooo.go4lunch.Models.User;
import com.go4lunch.flooo.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;

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
    @BindView(R.id.imageButtonlike)
    ImageView imageButtonlike;
    @BindView(R.id.textViewLike)
    TextView textButtonlike;
    @BindView(R.id.star_restaurant_1)
    ImageView starRestaurant1;
    @BindView(R.id.star_restaurant_2)
    ImageView starRestaurant2;
    @BindView(R.id.star_restaurant_3)
    ImageView starRestaurant3;


    private RecyclerView recyclerViewListUsers;
    private AdapterRecyclerViewListUsers adapterRecyclerViewListUsers;
    private Disposable disposable;
    private PlaceDetails details;
    private Context context;
    private String phoneNumber;
    private String website;
    private ArrayList<User> usersWhoEat;
    private ArrayList<String> currentUserLikeRestaurants = new ArrayList<>();
    private Boolean currentUserWhoEat =false;
    private int numberOfUsers=0;

    public RestaurantProfileActivity() {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_profile);
        ButterKnife.bind(this);

        context = this.getApplicationContext();

        String id = getIntent().getStringExtra("ID");

        usersWhoEat = new ArrayList<>();
        recyclerViewListUsers = findViewById(R.id.listUsersEating);
        recyclerViewListUsers.setLayoutManager(new LinearLayoutManager(context));
        adapterRecyclerViewListUsers = new AdapterRecyclerViewListUsers(usersWhoEat,context,true);
        recyclerViewListUsers.setAdapter(adapterRecyclerViewListUsers);
        currentUserLikeRestaurants.clear();
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
                Picasso.with(context).load(urlImage).resize(viewRestaurant.getWidth(), viewRestaurant.getHeight()-90).into(viewRestaurant);

                mRestaurantTitle.setText(details.getResults().getName());
                mRestaurantAdress.setText(details.getResults().getAdress());

                phoneNumber = details.getResults().getPhone();
                website = details.getResults().getWebsite();

               getUsersWhoJoining();



            }
        });
    }

    private void getUsersWhoJoining()
    {
        currentUserLikeRestaurants.clear();
        FireBaseFireStoreCollectionUsers.getUsersCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful()) {

                    usersWhoEat.clear();

                    int getLikes=0;

                    for (int i = 0; i < task.getResult().getDocuments().size(); i++) {

                        User user = task.getResult().getDocuments().get(i).toObject(User.class);
                        numberOfUsers = task.getResult().getDocuments().size();

                        //If the users eat in this restaurant.
                        if (task.getResult().getDocuments().get(i).get("placeID").toString().equals(details.getResults().getId()))
                        {
                            usersWhoEat.add(user);
                            if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(task.getResult().getDocuments().get(i).getId())){
                                userEatInThisRestaurant(true); }
                        }
                            //For all users get theirs RestaurantsLike
                            for (String restaurant : user.getRestaurantsUserLike())
                            {
                                currentUserLikeRestaurants.add(restaurant);

                                //in their list, if the restaurant equals the current place id
                                if (restaurant.equals(details.getResults().getId()))
                                {
                                    //In this list, if the user is the current user:
                                    if(user.getid().equals(FirebaseAuth.getInstance().getUid())){
                                    imageButtonlike.setImageDrawable(context.getResources().getDrawable(android.R.drawable.btn_star_big_on));
                                    textButtonlike.setText(context.getResources().getText(R.string.dislike));}

                                    getLikes+=1;

                                }
                            }
                    }

                    configureStars(getLikes);
                }
                adapterRecyclerViewListUsers.notifyDataSetChanged();
            }
        });
    }

    private void configureStars(int getLikes)
    {
        int rating=0;
        if(numberOfUsers!=0){
            rating = Math.round((getLikes*3)/numberOfUsers);}
        if(rating>0){starRestaurant1.setVisibility(View.VISIBLE);}
        if(rating>1){starRestaurant2.setVisibility(View.VISIBLE);}
        if(rating>2){starRestaurant3.setVisibility(View.VISIBLE);}
    }

    @OnClick(R.id.floatingActionBar)
    public void clickActionBar() {
        if(currentUserWhoEat) {
            userEatInThisRestaurant(false);
        } else {
            userEatInThisRestaurant(true);
        }
        getUsersWhoJoining();
    }

    private void userEatInThisRestaurant(Boolean YesorNo)
    {
        if(YesorNo)
        {
            currentUserWhoEat=true;
            FireBaseFireStoreCollectionUsers.updateRestaurantChoiceName(FirebaseAuth.getInstance().getCurrentUser().getUid(),details.getResults().getName());
            FireBaseFireStoreCollectionUsers.updateRestaurantChoicePlaceID(FirebaseAuth.getInstance().getCurrentUser().getUid(),details.getResults().getId());
            floatingActionBar.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            floatingActionBar.setRippleColor(Color.GREEN);
        }
        else
        {
            currentUserWhoEat=false;
            FireBaseFireStoreCollectionUsers.updateRestaurantChoiceName(FirebaseAuth.getInstance().getCurrentUser().getUid(), "");
            FireBaseFireStoreCollectionUsers.updateRestaurantChoicePlaceID(FirebaseAuth.getInstance().getCurrentUser().getUid(), "");
            floatingActionBar.setBackgroundTintList(ColorStateList.valueOf(Color.BLUE));
            floatingActionBar.setRippleColor(Color.BLUE);
        }
    }

    @OnClick(R.id.callAction)
    public void clickCallButton()
    {
        if(phoneNumber!=null){
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+phoneNumber));
            startActivity(intent);
        }else
        {
            showAlertDialogu(context.getResources().getString(R.string.call),context.getResources().getString(R.string.no_phone));
        }

    }
    @OnClick(R.id.likeAction)
    public void clickLikeButton()
    {
        Boolean set=true;
        Iterator<String> iter = currentUserLikeRestaurants.iterator();

        while (iter.hasNext()) {
            String str = iter.next();

            if (str.equals(details.getResults().getId())){

                iter.remove();
                FireBaseFireStoreCollectionUsers.updateListLikeRestaurants(currentUserLikeRestaurants,FirebaseAuth.getInstance().getCurrentUser().getUid());
                //UI SHOW LIKE ACTION
                imageButtonlike.setImageDrawable(context.getResources().getDrawable(android.R.drawable.btn_star_big_off));
                textButtonlike.setText(context.getResources().getText(R.string.like));
                set=false;
            }

        }

        if(set)
        {
        //DATA UPDATE LIKE
        currentUserLikeRestaurants.add(details.getResults().getId());
        FireBaseFireStoreCollectionUsers.updateListLikeRestaurants(currentUserLikeRestaurants,FirebaseAuth.getInstance().getCurrentUser().getUid());
        //UI SHOW LIKE ACTION
        imageButtonlike.setImageDrawable(context.getResources().getDrawable(android.R.drawable.btn_star_big_on));
        textButtonlike.setText(context.getResources().getText(R.string.dislike));
        }

    }
    @OnClick(R.id.webSiteAction)
    public void clickWebSiteButton()
    {
        if(website!=null){

            if(!website.equalsIgnoreCase("")) {
                Intent intent = new Intent(this.getApplication().getBaseContext(), WebSiteRestaurantActivity.class);
                intent.putExtra("URL", website);
                startActivity(intent);
            }
            else
            {
                showAlertDialogu(context.getResources().getString(R.string.website),context.getResources().getString(R.string.no_site_web));
            }
        }
        else { showAlertDialogu(context.getResources().getString(R.string.website),context.getResources().getString(R.string.no_site_web));
        }

    {

    }
    }

    private void showAlertDialogu(String title, String text)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(RestaurantProfileActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

    }



}
