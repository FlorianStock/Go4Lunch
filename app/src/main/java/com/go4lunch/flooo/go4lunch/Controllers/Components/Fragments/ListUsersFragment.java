package com.go4lunch.flooo.go4lunch.Controllers.Components.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.go4lunch.flooo.go4lunch.Controllers.Components.Adapters.AdapterRecyclerViewListUsers;
import com.go4lunch.flooo.go4lunch.R;



public class ListUsersFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private AdapterRecyclerViewListUsers adapterRecyclerViewListUsers;
    private RecyclerView mListUsers;

    public static ListUsersFragment newInstance()
    {
        ListUsersFragment fragment = new ListUsersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_list_restaurants, container, false);

        this.mListUsers = view.findViewById(R.id.listUsers);

        //adapterRecyclerViewListUsers = new AdapterRecyclerViewListUsers(restaurants, getContext());
        //mListUsers.setLayoutManager(new LinearLayoutManager(container.getContext()));
        //mListUsers.setAdapter(adapterRecyclerViewListUsers);

        // Inflate the layout for this fragment

        return view;
    }




}
