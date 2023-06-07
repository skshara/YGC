package com.jhc.ygc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jhc.ygc.databinding.ActivityMainBinding;

import org.w3c.dom.Text;

import java.util.Map;

public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    String fUserUid;
    TextView mEmail,mFname,mGrade,UserID;
    FirebaseFirestore db;
    DocumentReference userDocRef;
    ImageButton video,quiz,eBook,aBook;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     fAuth.getInstance();

     binding = ActivityMainBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        video = (ImageButton)findViewById(R.id.video);
        eBook = (ImageButton)findViewById(R.id.e_book);
        aBook = (ImageButton)findViewById(R.id.Audio);
        quiz = (ImageButton)findViewById(R.id.quiz);
        mEmail = binding.navView.getHeaderView(0).findViewById(R.id.textView4);
        mFname = binding.navView.getHeaderView(0).findViewById(R.id.textView2);
        mGrade = binding.navView.getHeaderView(0).findViewById(R.id.detail1);
        UserID = binding.navView.getHeaderView(0).findViewById(R.id.detail3);
        fUser = fAuth.getInstance().getCurrentUser();
        fUserUid = fUser.getUid();
        db = FirebaseFirestore.getInstance();
        userDocRef = db.collection("user").document(fUserUid);

        if (fUser != null) {
            if (fUser != null) {
                userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Map<String, Object> userData = documentSnapshot.getData();
                        String fname = (String) userData.get("fname");
                        String email = (String) userData.get("email");
                        String grade = (String) userData.get("grade");
                        // Access the user data and do whatever you need with it
                        mEmail.setText(email);
                        mFname.setText(fname);
                        mGrade.setText(grade);
                        UserID.setText(fUserUid);
                    } else {
                        // The document doesn't exist
                        Toast.makeText(getApplicationContext(), "The user information doesn't exist", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    // Failed with error code e
                    Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreError", "Error retrieving user document: " + e.getMessage());
                });
            } else {
                startActivity(new Intent(getApplicationContext(), login.class));
                finish();
            }
        } else {
            // Handle the case where the user is not authenticated
            // For example, redirect to the login screen
            startActivity(new Intent(getApplicationContext(), login.class));
            finish();
        }

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),VideoActivity.class));
            }
        });

        eBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),eBookActivity.class));
            }
        });

        aBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),AudiobookActivity.class));
            }
        });

        quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),QuizActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.log_out) {
            fAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),login.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}