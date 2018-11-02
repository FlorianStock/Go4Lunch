package com.go4lunch.flooo.go4lunch;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.firebase.ui.auth.AuthUI;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.Arrays;
import java.util.List;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity
{

    private int RC_SIGN_IN = 123;

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        loginFireBaseUI();
        setContentView(R.layout.activity_main);

        configurationViewPager();

        /*
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.go4lunch.flooo.go4lunch",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        */
    }

    private void loginFireBaseUI()
    {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());



        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    private void configurationViewPager()
    {
        final BottomNavigationView bottom_navigation = findViewById(R.id.bottom_navigation);

        String[] tabTitles = {"Map View","List View","WorkMates"};

        pager = findViewById(R.id.viewpager);
        pager.setAdapter(new AdapterViewPager(getSupportFragmentManager(),tabTitles,pager));

        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {

                switch (item.getItemId())
                {
                    case R.id.action_map:
                        pager.setCurrentItem(0);
                        break;
                    case R.id.action_list:
                        pager.setCurrentItem(1);
                        break;
                    case R.id.action_workmates:
                        pager.setCurrentItem(2);
                        break;
                }

                return false;
            }
        });


        
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {

            MenuItem prevMenuItem;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                if (prevMenuItem != null)
                {
                    prevMenuItem.setChecked(false);
                } else
                    {
                    bottom_navigation.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: " + position);
                bottom_navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottom_navigation.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int i)
            {

            }
            //pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


    });
    }



}
