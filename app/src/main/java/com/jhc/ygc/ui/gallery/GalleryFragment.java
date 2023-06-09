package com.jhc.ygc.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jhc.ygc.R;
import com.jhc.ygc.databinding.FragmentGalleryBinding;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class GalleryFragment extends Fragment {

private FragmentGalleryBinding binding;

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    String fUserUid;
    TextView mEmail,mFname,mGrade,UserID;
    ImageView imageView;
    FirebaseFirestore db;
    DocumentReference userDocRef;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

    binding = FragmentGalleryBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        mEmail = root.findViewById(R.id.textView4);
        mFname = root.findViewById(R.id.textView3);
        mGrade = root.findViewById(R.id.detail1);
        UserID = root.findViewById(R.id.detail3);
        imageView = root.findViewById(R.id.imageView4);
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        fUserUid = "123";

        if (fUser != null) {
            fUserUid = fUser.getUid();
            userDocRef = db.collection("user").document(fUserUid);
            userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Map<String, Object> userData = documentSnapshot.getData();
                    if(userData!=null) {
                        String fname = (String) userData.get("fname");
                        String email = (String) userData.get("email");
                        String grade = (String) userData.get("grade");
                        // Access the user data and do whatever you need with it
                        mEmail.setText(email);
                        mFname.setText(fname);
                        mGrade.setText(grade);
                        UserID.setText(fUserUid);
                    } else {
                        Log.d("FireStoreError","UserData is null, Checking google");
                        mEmail.setText(fUser.getEmail());
                        if(fUser.getDisplayName() != null || fUser.getDisplayName() != "") {
                            mFname.setText(fUser.getDisplayName());
                        } else {
                            String newName = fUser.getEmail();
                            if(newName.contains("@gmail.com")) {
                                newName.replace("@gmail.com", "");
                                mFname.setText(newName);
                            } else {
                                mFname.setText("User");
                            }
                        }
                        if(fUser.getPhotoUrl() != null) {
                            Picasso.get().load(fUser.getPhotoUrl()).into(imageView);
                        }
                    }
                } else {
                    // The document doesn't exist
                    Log.d("FirestoreError", "User document doesn't exist, checking google");
                    mEmail.setText(fUser.getEmail());
                    if(fUser.getDisplayName() != null || fUser.getDisplayName() != "") {
                        mFname.setText(fUser.getDisplayName());
                    } else {
                        String newName = fUser.getEmail();
                        if(newName.contains("@gmail.com")) {
                            newName.replace("@gmail.com", "");
                            mFname.setText(newName);
                        } else {
                            mFname.setText("User");
                        }
                    }
                    if(fUser.getPhotoUrl() != null) {
                        Picasso.get().load(fUser.getPhotoUrl()).into(imageView);
                    }
                }
            }).addOnFailureListener(e -> {
                // Failed with error code e
                Log.d("FirestoreError", "Error retrieving user document: " + e.getMessage());
            });
        }
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}