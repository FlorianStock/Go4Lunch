package com.go4lunch.flooo.go4lunch.Controllers.ApiFireBase;

import android.support.annotation.NonNull;

import com.go4lunch.flooo.go4lunch.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class FireBaseFireStoreCollectionUsers
{

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getUsersCollection()
    {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);

        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);

    }

    // --- CREATE ---

    public static Task<Void> createUser(String id, String username, String urlPicture)
    {
        User userToCreate = new User(id, username, urlPicture);
        return FireBaseFireStoreCollectionUsers.getUsersCollection().document(id).set(userToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getUser(String uid){
        return FireBaseFireStoreCollectionUsers.getUsersCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return FireBaseFireStoreCollectionUsers.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateRestaurantChoiceName(String uid, String restaurantName) {
        return FireBaseFireStoreCollectionUsers.getUsersCollection().document(uid).update("haveChosenRestaurant", restaurantName);
    }
    public static Task<Void> updateRestaurantChoicePlaceID(String uid, String restaurantPlace) {
        return FireBaseFireStoreCollectionUsers.getUsersCollection().document(uid).update("placeID", restaurantPlace);
    }
    public static Task<Void> updateListLikeRestaurants(ArrayList<String> listRestaurants, String uid) {
        return FireBaseFireStoreCollectionUsers.getUsersCollection().document(uid).update("restaurantsUserLike", listRestaurants);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return FireBaseFireStoreCollectionUsers.getUsersCollection().document(uid).delete();
    }

    public static int getUsersWhoLikesRestaurant(final String placeID)
    {

       final ArrayList<User> users = new ArrayList<>();
       int usersNumber;

        FireBaseFireStoreCollectionUsers.getUsersCollection().orderBy("restaurantsUserLike").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task)
            {


                if (task.isSuccessful())
                {
                    for(int i=0;i<task.getResult().getDocuments().size();i++)
                    {
                        User user = task.getResult().getDocuments().get(i).toObject(User.class);

                        for(String restaurant:user.getRestaurantsUserLike())
                        {
                            if(restaurant.equals(placeID))
                            {
                                users.add(user);
                            }
                        }
                    }

                }
            }
        });

        usersNumber = users.size();
        return usersNumber;
    }

}