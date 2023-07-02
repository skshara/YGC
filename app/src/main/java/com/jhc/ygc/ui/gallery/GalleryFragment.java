package com.jhc.ygc.ui.gallery;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.jhc.ygc.edit_info;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.Picasso;
import java.util.Map;

public class GalleryFragment extends Fragment {

private FragmentGalleryBinding binding;

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    String fUserUid;
    TextView mEmail,mFname,mGrade;
    Button editBtn;
    ImageButton backBtn;
    ImageView imageView;
    FirebaseFirestore db;
    DocumentReference userDocRef;
    String email,grade,fname;
    public OkHttpClient client;
    public static Integer points;
    @SuppressWarnings("deprecation")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

    binding = FragmentGalleryBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        mEmail = root.findViewById(R.id.textView4);
        mFname = root.findViewById(R.id.textView3);
        mGrade = root.findViewById(R.id.detail1);
        imageView = root.findViewById(R.id.imageView4);
        editBtn = root.findViewById(R.id.back);
        backBtn = root.findViewById(R.id.imageButton);
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        client = new OkHttpClient();
        fUserUid = "123";
        points = null;
        final ProgressDialog progress = ProgressDialog.show(getActivity(),"","Loading...",true);

        if (fUser != null) {
            fUserUid = fUser.getUid();
            userDocRef = db.collection("user").document(fUserUid);
            userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Map<String, Object> userData = documentSnapshot.getData();
                    if(userData!=null) {
                        fname = (String) userData.get("fname");
                        email = (String) userData.get("email");
                        grade = (String) userData.get("grade");
                        if(fUser.getPhotoUrl() != null) {
                            Picasso.get().load(fUser.getPhotoUrl()).into(imageView);
                        }
                        // Access the user data and do whatever you need with it
                        if(TextUtils.isEmpty(email)) {
                            if(!TextUtils.isEmpty(fUser.getEmail())){
                                mEmail.setText(fUser.getEmail());
                            } else {
                                mEmail.setText("");
                            }
                        } else {
                            mEmail.setText(email);
                        }
                        if(TextUtils.isEmpty(fname)) {
                            if(!TextUtils.isEmpty(fUser.getDisplayName())){
                                mFname.setText(fUser.getDisplayName());
                            } else {
                                mFname.setText("");
                            }
                        } else {
                            mFname.setText(fname);
                        }
                        mGrade.setText(grade);
                        progress.dismiss();
                    } else {
                        Log.d("FireStoreError","UserData is null, Checking google");
                        mEmail.setText(fUser.getEmail());
                        if(fUser.getDisplayName() != null || !fUser.getDisplayName().equals("")) {
                            mFname.setText(fUser.getDisplayName());
                        }
                        if(fUser.getPhotoUrl() != null) {
                            Picasso.get().load(fUser.getPhotoUrl()).into(imageView);
                        }
                        progress.dismiss();
                    }
                } else {
                    // The document doesn't exist
                    Log.d("FirestoreError", "User document doesn't exist, checking google");
                    mEmail.setText(fUser.getEmail());
                    if(fUser.getDisplayName() != null || !fUser.getDisplayName().equals("")) {
                        mFname.setText(fUser.getDisplayName());
                    } else {
                        mFname.setText("User");
                    }
                    if(fUser.getPhotoUrl() != null) {
                        Picasso.get().load(fUser.getPhotoUrl()).into(imageView);
                    }
                    progress.dismiss();
                }
            }).addOnFailureListener(e -> {
                // Failed with error code e
                Log.d("FirestoreError", "Error retrieving user document: " + e.getMessage());
                progress.dismiss();
            });
        }

        backBtn.setOnClickListener(view -> super.getActivity().onBackPressed());
        editBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), edit_info.class);
            intent.putExtra("grade", grade);
            intent.putExtra("email", email);
            intent.putExtra("fname",fname);

            startActivity(intent);
        });


        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}