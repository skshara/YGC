package com.jhc.ygc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class detailupdate extends AppCompatActivity {

    public boolean isGradeChecked = false;
    public boolean isContentChecked = false;
    public boolean isSubjectChecked = false;
    Button login,upload;
    public TextInputEditText fName,fLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatialupdate);
        login = (Button) findViewById(R.id.back);
        upload = (Button) findViewById(R.id.upload);
        fName = (TextInputEditText) findViewById(R.id.Title_det);
        fLink = (TextInputEditText) findViewById(R.id.link);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),login.class));
                finish();
            }
        });


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Handle the selected item
                if(position == 0) {
                    isContentChecked = false;
                } else {
                    isGradeChecked = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected
                isContentChecked = false;
            }
        });

        Spinner spinner1 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.grade_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                // Handle the selected item
                if(position == 0) {
                    isGradeChecked = false;
                } else {
                    isGradeChecked = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected
                isGradeChecked = false;
            }
        });

        Spinner spinner3 = findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.subject_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(adapter3);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(position==0) {
                    isSubjectChecked = false;
                } else {
                    isSubjectChecked = true;
                }
                // Handle the selected item
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle the case where nothing is selected
                isSubjectChecked = false;
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isGradeChecked&&isContentChecked&&isSubjectChecked) {
                    if(fName.getText()!=null&&fLink.getText()!=null) {
                        String name = fName.getText().toString().trim();
                        String link = fLink.getText().toString().trim();
                        String content = spinner.getSelectedItem().toString();
                        String grade = spinner1.getSelectedItem().toString();
                        String subject = spinner3.getSelectedItem().toString();
                        // Do something
                        // TODO Firebase Adding like in profile screen
                    } else {
                        Toast.makeText(detailupdate.this, "Fill all information", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        
        
    }

}
