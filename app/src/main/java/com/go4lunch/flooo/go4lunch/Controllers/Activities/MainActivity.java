package com.go4lunch.flooo.go4lunch.Controllers.Activities;



import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;



import java.util.Arrays;
import java.util.List;


import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.go4lunch.flooo.go4lunch.Controllers.ApiFireBase.FireBaseFireStoreCollectionUsers;
import com.go4lunch.flooo.go4lunch.Controllers.Components.Adapters.AdapterViewPager;
import com.go4lunch.flooo.go4lunch.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements OnFailureListener {

    private int RC_SIGN_IN = 123;

    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FireBaseAuthentification();

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

    public void FireBaseAuthentification()
    {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN)
        {
            if (resultCode == RESULT_OK)
            {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                if (currentUser != null)
                {
                    String urlPicture = (currentUser.getPhotoUrl() != null) ? currentUser.getPhotoUrl().toString() : null;
                    String username = currentUser.getDisplayName();
                    String uid = currentUser.getUid();
                    System.out.println(username);
                    FireBaseFireStoreCollectionUsers.createUser(uid, username, urlPicture).addOnFailureListener(this);
                }

            } else
            { // ERRORS
                if (response == null)
                {

                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK)
                {

                }
                else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR)
                {

                }
            }
        }
    }


    @Override
    public void onFailure(@NonNull Exception e)
    {
        System.out.println(e);
    }
}
