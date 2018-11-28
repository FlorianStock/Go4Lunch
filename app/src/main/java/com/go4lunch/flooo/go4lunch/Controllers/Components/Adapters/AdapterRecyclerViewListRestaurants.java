package com.go4lunch.flooo.go4lunch.Controllers.Components.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.go4lunch.flooo.go4lunch.Controllers.Components.RecyclerViewListRestaurants;
import com.go4lunch.flooo.go4lunch.Models.PlaceNearBySearch;
import com.go4lunch.flooo.go4lunch.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterRecyclerViewListRestaurants extends RecyclerView.Adapter<RecyclerViewListRestaurants>
{

    private ArrayList<PlaceNearBySearch.Results> restaurants;
    private Context context;

    public AdapterRecyclerViewListRestaurants(ArrayList<PlaceNearBySearch.Results> results,Context context)
    {
         restaurants=results;
         this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewListRestaurants onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cell_list_restaurant, parent, false);

        return new RecyclerViewListRestaurants(view,restaurants);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewListRestaurants holder, int position)
    {
        holder.updateView(position,this.context);
    }

    @Override
    public int getItemCount()
    {
        return restaurants.size();
    }


}
