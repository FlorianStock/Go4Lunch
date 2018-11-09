package com.go4lunch.flooo.go4lunch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RestaurantProfileActivity extends AppCompatActivity
{

    @BindView(R.id.textViewNameRestaurant)
    TextView mRestaurantTitle;
    @BindView(R.id.textViewAdress)
    TextView mRestaurantAdress;

    public RestaurantProfileActivity()
    {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_profile);
        ButterKnife.bind(this);


    }

    @OnClick(R.id.callAction)
    public void clickCallButton()
    {
        Toast.makeText(getApplicationContext(), "mon message", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.likeAction)
    public void clickLikeButton()
    {
        Toast.makeText(getApplicationContext(), "mon message", Toast.LENGTH_SHORT).show();
    }
    @OnClick(R.id.webSiteAction)
    public void clickWebSiteButton()
    {
        Toast.makeText(getApplicationContext(), "mon message", Toast.LENGTH_SHORT).show();
    }
}
