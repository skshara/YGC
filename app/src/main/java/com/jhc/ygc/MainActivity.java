package com.jhc.ygc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.ktx.Firebase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button signOut;
    FirebaseAuth fAuth;
    //FirebaseUser fUser;
    //DataSnapshot dataSnapshot;

    //@Override
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signOut = findViewById(R.id.signOut);

        //fAuth.getInstance();

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),login.class));
                finish();
            }
        });



    }

    //private String getGrade() {
        //fUser = fAuth.getInstance().getCurrentUser();
        //String UserID =fUser.getUid().toString();
        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        //String gradeFromDB = dataSnapshot.child(UserID).child("grade").getValue(String.class);
        //return gradeFromDB;
    //}
}