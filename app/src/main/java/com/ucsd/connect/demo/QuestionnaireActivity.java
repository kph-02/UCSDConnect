package com.ucsd.connect.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionnaireActivity extends AppCompatActivity {
    private CheckBox question1, question2, question3, question4, question5, question6, question7, question8, question9, question10;
    private Button getAnswersButton;
    private List<String> results;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    private String stringResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        question1 = findViewById(R.id.question_one);
        question2 = findViewById(R.id.question_two);
        question3 = findViewById(R.id.question_three);
        question4 = findViewById(R.id.question_four);
        question5 = findViewById(R.id.question_five);
        question6 = findViewById(R.id.question_six);
        question7 = findViewById(R.id.question_seven);
        question8 = findViewById(R.id.question_eight);
        question9 = findViewById(R.id.question_nine);
        question10 = findViewById(R.id.question_ten);
        getAnswersButton = findViewById(R.id.questionnaire_result);
        stringResults = "";
        results = new ArrayList<>();
        question1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question1.isChecked()) {
                    results.add("Raccoons");
                } else {
                    results.remove("Raccoons");
                }
            }
        });
        question2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question2.isChecked()) {
                    results.add("Bad Taste-Buds");
                } else {
                    results.remove("Bad Taste-Buds");
                }
            }
        });
        question3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question1.isChecked()) {
                    results.add("Religious");
                } else {
                    results.remove("Religious");
                }
            }
        });
        question4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question3.isChecked()) {
                    results.add("Fitness");
                } else {
                    results.remove("Fitness");
                }
            }
        });
        question5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question4.isChecked()) {
                    results.add("Gamer");
                } else {
                    results.remove("Gamer");
                }
            }
        });
        question6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question1.isChecked()) {
                    results.add("Weeb");
                } else {
                    results.remove("Weeb");
                }
            }
        });
        question7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question7.isChecked()) {
                    results.add("Boba-holic");
                } else {
                    results.remove("Boba-holic");
                }
            }
        });
        question8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question8.isChecked()) {
                    results.add("IEEE");
                } else {
                    results.remove("IEEE");
                }
            }
        });
        question9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (question8.isChecked()) {

                    results.add("STEM");
                } else {
                    results.remove("STEM");
                }
            }
        });
        question10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question10.isChecked()) {
                    results.add("Depressed");
                } else {
                    results.remove("Depressed");
                }
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        getAnswersButton.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v){
                final DatabaseReference databaseReference = firebaseDatabase.getReference().child("user").child(firebaseAuth.getUid()).child("traits");
                databaseReference.setValue(results);
                firebaseAuth.signOut();
                Toast.makeText(QuestionnaireActivity.this, "Thanks for completing the survey", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });


    }
}