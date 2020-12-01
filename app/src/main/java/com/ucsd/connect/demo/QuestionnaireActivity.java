package com.ucsd.connect.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import java.util.ArrayList;

public class QuestionnaireActivity extends AppCompatActivity {

    private Button question1, question2, question3, question4, question5, question6, question7, question8, question9, question10;
    private Button getAnswersButton;
    private ArrayList<String> results;
    private String stringResults;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire);
        question1 = findViewById(R.id.question_one);
//        question2 = findViewById(R.id.question_two);
//        question3 = findViewById(R.id.question_three);
//        question4 = findViewById(R.id.question_four);
//        question5 = findViewById(R.id.question_five);
//        question6 = findViewById(R.id.question_six);
//        question7 = findViewById(R.id.question_seven);
//        question8 = findViewById(R.id.question_eight);
//        question9 = findViewById(R.id.question_nine);
//        question10 = findViewById(R.id.question_ten);
        getAnswersButton = findViewById(R.id.questionnaire_result);
        stringResults = "";
        results = new ArrayList<>();
        question1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question1.isClickable()) {
                    results.add("Raccoons");
                    question1.setBackgroundColor(111111);
                    question1.setText("Yes!");
                } else {
                    results.remove("Raccoons");
                    question1.setBackgroundColor(003667);
                    question1.setText("Do you like raccoons?");
                }
            }
        });
        question2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question2.isClickable()) {
                    results.add("Bad Taste-Buds");
                } else {
                    results.remove("Bad Taste-Buds");
                }
            }
        });
        question3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question3.isClickable()) {
                    results.add("Religious");
                } else {
                    results.remove("Religious");
                }
            }
        });
        question4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question4.isClickable()) {
                    results.add("Fitness");
                } else {
                    results.remove("Fitness");
                }
            }
        });
        question5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question5.isClickable()) {
                    results.add("Gamer");
                } else {
                    results.remove("Gamer");
                }
            }
        });
        question6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question6.isClickable()) {
                    results.add("Weeb");
                } else {
                    results.remove("Weeb");
                }
            }
        });
        question7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question7.isClickable()) {
                    results.add("Boba-holic");
                } else {
                    results.remove("Boba-holic");
                }
            }
        });
        question8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question8.isClickable()) {
                    results.add("IEEE");
                } else {
                    results.remove("IEEE");
                }
            }
        });
        question9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (question9.isClickable()) {

                    results.add("STEM");
                } else {
                    results.remove("STEM");
                }
            }
        });
        question10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question10.isClickable()) {
                    results.add("Depressed");
                } else {
                    results.remove("Depressed");
                }
            }
        });
        getAnswersButton.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v){
                StringBuilder builder = new StringBuilder();
               for(String s:results){
               builder.append(s).append(" ");
               }
               stringResults= builder.toString();
                Toast.makeText(QuestionnaireActivity.this, "Thanks for completing the survey", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });


    }
}