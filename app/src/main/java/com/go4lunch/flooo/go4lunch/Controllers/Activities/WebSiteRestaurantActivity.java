package com.go4lunch.flooo.go4lunch.Controllers.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.go4lunch.flooo.go4lunch.R;

public class WebSiteRestaurantActivity extends AppCompatActivity
{

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {

            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_website_view);

            //With url in intent, we create the WebView
            String url = getIntent().getStringExtra("URL");
            WebView myWebView = findViewById(R.id.webview);
            myWebView.loadUrl(url);
        }

}
