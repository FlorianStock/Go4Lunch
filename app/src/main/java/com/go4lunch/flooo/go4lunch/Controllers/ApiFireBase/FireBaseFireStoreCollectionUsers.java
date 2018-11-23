package com.go4lunch.flooo.go4lunch.Controllers.ApiFireBase;

import com.go4lunch.flooo.go4lunch.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;


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

   /* public static Query getAllMessageForChat(String chat){
        return ChatHelper.getChatCollection()
                .document(chat)
                .collection(COLLECTION_NAME)
                .orderBy("dateCreated")
                .limit(50);
    } */

    // --- UPDATE ---

    public static Task<Void> updateUsername(String username, String uid) {
        return FireBaseFireStoreCollectionUsers.getUsersCollection().document(uid).update("username", username);
    }

    public static Task<Void> updateIsMentor(String uid, Boolean isMentor) {
        return FireBaseFireStoreCollectionUsers.getUsersCollection().document(uid).update("isMentor", isMentor);
    }

    // --- DELETE ---

    public static Task<Void> deleteUser(String uid) {
        return FireBaseFireStoreCollectionUsers.getUsersCollection().document(uid).delete();
    }

}