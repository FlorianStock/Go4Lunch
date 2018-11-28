package com.go4lunch.flooo.go4lunch.Controllers.Components.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.go4lunch.flooo.go4lunch.Controllers.Components.RecyclerViewListUsers;
import com.go4lunch.flooo.go4lunch.Models.User;
import com.go4lunch.flooo.go4lunch.R;

import java.util.ArrayList;

public class AdapterRecyclerViewListUsers extends RecyclerView.Adapter<RecyclerViewListUsers>
{

    private Boolean joiningList = false;
    private Context context ;
    private ArrayList<User> users;

    public AdapterRecyclerViewListUsers(ArrayList<User> users, Context context,Boolean joiningList)
    {
        this.joiningList = joiningList;
        this.users = users;
        this.context = context;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewListUsers recyclerViewListUsers, int position)
    {
        recyclerViewListUsers.updateList(users.get(position));
    }

    @Override
    public int getItemCount()
    {
        return users.size();
    }

    @NonNull
    @Override
    public RecyclerViewListUsers onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cell_list_user, parent, false);

        return new RecyclerViewListUsers(view,context,joiningList);
    }


}
