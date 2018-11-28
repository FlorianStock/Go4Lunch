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


import com.go4lunch.flooo.go4lunch.Controllers.ApiFireBase.FireBaseFireStoreCollectionUsers;
import com.go4lunch.flooo.go4lunch.Controllers.Components.Adapters.AdapterRecyclerViewListUsers;
import com.go4lunch.flooo.go4lunch.Models.User;
import com.go4lunch.flooo.go4lunch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;



public class ListUsersFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private AdapterRecyclerViewListUsers adapterRecyclerViewListUsers;

    private RecyclerView mListUsers;

    private ArrayList<User> users = new ArrayList<>();


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
        View view = inflater.inflate(R.layout.fragment_list_users, container, false);

        this.mListUsers = view.findViewById(R.id.listUsers);

        this.adapterRecyclerViewListUsers = new AdapterRecyclerViewListUsers(RequestListUsers(),this.getContext(),false);
        mListUsers.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mListUsers.setAdapter(adapterRecyclerViewListUsers);

        // Inflate the layout for this fragment

        return view;
    }



    private ArrayList<User> RequestListUsers()
    {
        final ArrayList<User> users = new ArrayList<>();

        FireBaseFireStoreCollectionUsers.getUsersCollection().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {
                if (task.isSuccessful())
                {
                    for(int i=0;i<task.getResult().getDocuments().size();i++)
                    {
                        User user = new User();
                        user.setid(task.getResult().getDocuments().get(i).get("id").toString());
                        user.setUrlPicture(task.getResult().getDocuments().get(i).get("urlPicture").toString());
                        user.setUsername(task.getResult().getDocuments().get(i).get("username").toString());
                        user.sethaveChosenRestaurant(task.getResult().getDocuments().get(i).get("haveChosenRestaurant").toString());
                        user.setPlaceID(task.getResult().getDocuments().get(i).get("placeID").toString());
                        users.add(user);
                    }
                }
                    adapterRecyclerViewListUsers.notifyDataSetChanged();
            }
        });

        return users;
    }




}
