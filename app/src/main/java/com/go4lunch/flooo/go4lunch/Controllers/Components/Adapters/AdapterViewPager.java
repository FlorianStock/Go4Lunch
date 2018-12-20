package com.go4lunch.flooo.go4lunch.Controllers.Components.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.go4lunch.flooo.go4lunch.Controllers.Components.Fragments.ListRestaurantsFragment;
import com.go4lunch.flooo.go4lunch.Controllers.Components.Fragments.ListUsersFragment;
import com.go4lunch.flooo.go4lunch.Controllers.Components.Fragments.MapViewFragment;


public class AdapterViewPager extends FragmentPagerAdapter
{

        private String[] tabTitles ;
        private ViewPager viewPager;

        // Default Constructor
        public AdapterViewPager(FragmentManager mgr, String[] tabTitles, ViewPager pager)
        {
            super(mgr);
            this.tabTitles = tabTitles;
        }

        @Override
        public int getCount()
        {
            return (tabTitles.length);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return tabTitles[position];
        }

        @Override
        public Fragment getItem(int position)
        {
            switch(tabTitles[position])
            {
                case "Map View":return MapViewFragment.newInstance();
                case "List View":return ListRestaurantsFragment.newInstance();
                case "WorkMates":return ListUsersFragment.newInstance();
                default:return MapViewFragment.newInstance();
            }

        }





}

