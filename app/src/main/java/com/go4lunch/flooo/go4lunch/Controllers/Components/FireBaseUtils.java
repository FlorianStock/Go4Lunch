package com.go4lunch.flooo.go4lunch.Controllers.Components;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireBaseUtils implements ValueEventListener
{
    private static FireBaseUtils INSTANCE = null;

    public static FireBaseUtils getInstance()
    {
        if(INSTANCE ==null)
        {
             INSTANCE = new FireBaseUtils();
        }
            return INSTANCE;
    }

    public FireBaseUtils()
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("users");
        usersdRef.addListenerForSingleValueEvent(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
    {

        System.out.println(dataSnapshot.getRef());
        for(DataSnapshot ds : dataSnapshot.getChildren())
        {
            String name = ds.child("name").getValue(String.class);
            Log.d("TAG", name);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError)
    {

    }


}
