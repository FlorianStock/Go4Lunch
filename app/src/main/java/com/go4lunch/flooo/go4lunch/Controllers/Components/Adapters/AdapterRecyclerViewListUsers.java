package com.go4lunch.flooo.go4lunch.Controllers.Components.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.go4lunch.flooo.go4lunch.Controllers.Components.RecyclerViewListUsers;

public class AdapterRecyclerViewListUsers extends RecyclerView.Adapter<RecyclerViewListUsers>
{
    public AdapterRecyclerViewListUsers()
    {

    }

    @NonNull
    @Override
    public RecyclerViewListUsers onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return null;
    }

    @Override
    public int getItemCount()
    {
        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewListUsers holder, int position)
    {
        //holder.updateWithMessage(model, this.idCurrentUser, this.glide);
    }


}
