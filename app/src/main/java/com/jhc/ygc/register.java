package com.jhc.ygc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    Button backtoLogin;
    ProgressBar progressBar2;
    TextInputEditText mFullName, mEmail, mPassword, mPass2, mGrade;

    Button mRegisterBtn;

    FirebaseFirestore fstore;

    String userID;

    public FirebaseAuth fAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final String TAG = "TAG";
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        backtoLogin = findViewById(R.id.back);
        mFullName = findViewById(R.id.name);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.pwd);
        mPass2 = findViewById(R.id.repwd);
        mGrade = findViewById(R.id.grd);
        mRegisterBtn = findViewById(R.id.regbtn);
        progressBar2 = findViewById(R.id.progressBar2);

        fAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        backtoLogin.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), login.class));
            finish();
        });

        mRegisterBtn.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(mEmail.getText())&&!TextUtils.isEmpty(mPassword.getText())&&!TextUtils.isEmpty(mPass2.getText())&&!TextUtils.isEmpty(mFullName.getText())&&!TextUtils.isEmpty(mGrade.getText())) {
                progressBar2.setVisibility(View.VISIBLE);
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String password2 = mPass2.getText().toString().trim();
                final String fullname = mFullName.getText().toString().trim();
                final String grade = mGrade.getText().toString().trim();
                int Grade = Integer.parseInt(grade);


                if (TextUtils.isEmpty(email)) {
                    mEmail.setError("Email is required");
                    progressBar2.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(fullname)) {
                    mFullName.setError("Name is required");
                    progressBar2.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    mPassword.setError("Password is required");
                    progressBar2.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(grade)) {
                    mGrade.setError("Type your grade");
                    progressBar2.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(password2)) {
                    mPassword.setError("Type your password again");
                    progressBar2.setVisibility(View.GONE);
                    return;
                }

                if (!password.equals(password2)) {
                    mPassword.setError("The passwords don't match");
                    progressBar2.setVisibility(View.GONE);
                    return;
                }

                if (password.length() < 8) {
                    mPassword.setError("The password length should be 8 or more than 8");
                    progressBar2.setVisibility(View.GONE);
                    return;
                }

                if (Grade < 6 || Grade > 11) {
                    mGrade.setError("Enter valid grade");
                    progressBar2.setVisibility(View.GONE);
                    return;
                }


                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser fuser = fAuth.getCurrentUser();
                        fuser.sendEmailVerification().addOnSuccessListener(unused -> {
                            progressBar2.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Register Successful. You'll have to verify email on next sign-in", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(e -> {
                            Log.d(TAG, "Onfailure: Email Not Sent" + e.getMessage());
                            progressBar2.setVisibility(View.GONE);
                        });
                        Toast.makeText(getApplicationContext(), "User created", Toast.LENGTH_SHORT).show();
                        userID = fAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = fstore.collection("user").document(userID);
                        Map<String, Object> user = new HashMap<>();
                        user.put("fname", fullname);
                        user.put("email", email);
                        user.put("grade", grade);
                        documentReference.set(user).addOnSuccessListener(unused -> {
                            Log.d(TAG, "onsuccess: user profile is created for" + userID);
                            progressBar2.setVisibility(View.GONE);
                        }).addOnFailureListener(e -> {
                            Log.d(TAG, "onFailure: " + e);
                            progressBar2.setVisibility(View.GONE);
                        });
                        fAuth.signOut();
                    } else {
                        Toast.makeText(register.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar2.setVisibility(View.GONE);
                    }
                });
            }
        });
    }
}