package com.jhc.ygc.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jhc.ygc.MainActivity;
import com.jhc.ygc.R;
import com.jhc.ygc.databinding.FragmentGalleryBinding;
import com.jhc.ygc.edit_info;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

public class GalleryFragment extends Fragment {

private FragmentGalleryBinding binding;

    FirebaseAuth fAuth;
    FirebaseUser fUser;
    String fUserUid;
    TextView mEmail,mFname,mGrade,UserID,mPoints;
    Button editBtn;
    ImageView imageView;
    FirebaseFirestore db;
    DocumentReference userDocRef;
    public OkHttpClient client;
    public static Integer points;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        GalleryViewModel galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);

    binding = FragmentGalleryBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textGallery;
        mEmail = root.findViewById(R.id.textView4);
        mFname = root.findViewById(R.id.textView3);
        mGrade = root.findViewById(R.id.detail1);
        mPoints = root.findViewById(R.id.detail2);
        UserID = root.findViewById(R.id.detail3);
        imageView = root.findViewById(R.id.imageView4);
        editBtn = root.findViewById(R.id.back);
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        client = new OkHttpClient();
        fUserUid = "123";
        points = null;

        if (fUser != null) {
            getPointsForCurrentUser();
            if(points!=null) {
                String point = points.toString().trim();
                mPoints.setText(point);
            }
            fUserUid = fUser.getUid();
            userDocRef = db.collection("user").document(fUserUid);
            userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Map<String, Object> userData = documentSnapshot.getData();
                    if(userData!=null) {
                        String fname = (String) userData.get("fname");
                        String email = (String) userData.get("email");
                        String grade = (String) userData.get("grade");
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
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), edit_info.class);
                startActivity(intent);
            }
        });


        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }
    public void getPointsForCurrentUser() {
        // Get the current Firebase user

        // Obtain the user ID
        String userId = fUser.getUid();

        // Construct the API endpoint URL
        String url = getString(R.string.H5P_BASE_URL) + "/points?user_id=" + userId;

        // Create the request and make the API call
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Api-Key", getString(R.string.API_KEY))
                .addHeader("Api-Secret", getString(R.string.API_SECRET))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                // Handle failure
                e.printStackTrace();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                // Handle the API response
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        points = jsonObject.getInt("points");
                        // Process the points data
                        // You can perform any desired operations with the points value

                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Handle JSON parsing error
                    }
                } else {
                    // Handle API error response
                }
            }
        });
    }
@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}