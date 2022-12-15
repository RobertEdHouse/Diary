package com.example.swg.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.swg.fragments.FragmentPhoto;
import com.example.swg.ui.EntriesAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SaveLoader implements Serializable {
    private final transient StorageReference storageReference;
    private transient  FirebaseAuth mAuth;
    private transient  FirebaseUser user;
    private final transient  DatabaseReference mDatabase;

    List<Entry>listEntry;
    public SaveLoader(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public void authentication(Activity activity){
        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        mAuth.signInAnonymously()
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Log.d("TAG", "signInAnonymously:success");
                        user = mAuth.getCurrentUser();
                    } else {
                        Log.w("TAG", "signInAnonymously:failure", task.getException());
                    }
                });
    }
    public void loadEntry(ListView listView, Context context){
        listEntry=new ArrayList<>();
        mDatabase.child(user.getUid()).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.e("firebase", "Error getting data", task.getException());
            }
            else {
                DataSnapshot snap=task.getResult().child("entry");
                for(DataSnapshot d:snap.getChildren()){
                    listEntry.add(d.getValue(Entry.class));
                }
                ListAdapter adapter =
                        new EntriesAdapter(context, listEntry);
                listView.setAdapter(adapter);
            }
        });
    }
    public void writeNewEntry(Entry entry) {
        DatabaseReference pushedPostRef = mDatabase.push();
        String key = pushedPostRef.getKey();
        entry.setKey(key);
        assert key != null;
        mDatabase.child(user.getUid()).child("entry").child(key).setValue(entry);
    }

    public void updateEntry(Entry entry){
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(user.getUid()+"/entry/" + entry.key, entry);
        mDatabase.updateChildren(childUpdates);
    }

    public void uploadImage(Uri filePath, Context context, Fragment fragmentPhoto)
    {
        ProgressDialog progressDialog
                = new ProgressDialog(context);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        String url= UUID.randomUUID().toString();
        StorageReference ref
                = storageReference
                .child(
                        "images/"
                                + url);
        Log.d("d",url);
        ref.putFile(filePath)
                .addOnSuccessListener(
                        taskSnapshot -> {
                            progressDialog.dismiss();
                            runThread(fragmentPhoto,url);

                            Toast
                                    .makeText(context,
                                            "Image Uploaded!!",
                                            Toast.LENGTH_SHORT)
                                    .show();
                        })

                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Log.d("tag",e.getMessage());
                    Toast
                            .makeText(context,
                                    "Failed " + e.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                })
                .addOnProgressListener(
                        taskSnapshot -> {
                            double progress
                                    = (100.0
                                    * taskSnapshot.getBytesTransferred()
                                    / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage(
                                    "Uploaded "
                                            + (int)progress + "%");
                        });

    }
    private void runThread(Fragment f, String url){
        f.requireActivity().runOnUiThread(new Thread(() -> ((FragmentPhoto)f).setImage(url)));
    }

    public void loadImageFromStorage(ImageView imageView, String image){

        StorageReference islandRef = storageReference.child("images/"+image);

        final long ONE_MEGABYTE = 1024 * 1024 * 5;
        islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageView.setImageBitmap(bitmap);
        }).addOnFailureListener(exception -> {
        });
    }
}
