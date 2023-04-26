package com.jhc.ygc;

import android.os.Bundle;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import com.jhc.ygc.databinding.ActivityMain2Binding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button signOut;
    FirebaseAuth fAuth;
    //FirebaseUser fUser;
    //DataSnapshot dataSnapshot;

    //protected void onStart() {
    // super.onStart();
    // String grade = getGrade();
    // if(grade==null) {
    // Go to register for now
    //fAuth.getInstance().signOut();
    //Toast.makeText(MainActivity.this, "Google sign up is not allowed. First Sign up with email", Toast.LENGTH_SHORT).show();
    //startActivity(new Intent(getApplicationContext(),login.class));
    //finish();
    //}
    //}
    private ActivityMain2Binding binding;
    private ImageSlider imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setContentView(R.layout.activity_main2);
        signOut = findViewById(R.id.signOut);
        //fAuth.getInstance();
        imageSlider= findViewById(R.id.image_slider);
        ArrayList<SlideModel> slideModels= new ArrayList<>(); // Create image list
        slideModels.add(new SlideModel("", ScaleTypes.FIT));
        slideModels.add(new SlideModel("", ScaleTypes.FIT));
        slideModels.add(new SlideModel("", ScaleTypes.FIT));
        slideModels.add(new SlideModel("", ScaleTypes.FIT));

        imageSlider.setImageList(slideModels,ScaleTypes.FIT);


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),login.class));
                finish();
            }
        });
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main2);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}