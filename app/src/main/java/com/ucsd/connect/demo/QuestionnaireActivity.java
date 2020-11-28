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
    private CheckBox question1, question2, question3, question4, question5, question6, question7, question8, question9, question10;
    private Button getAnswersButton;
    private ArrayList<String> results;
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
                    results.add("Question1");
                } else {
                    results.remove("Question1");
                }
            }
        });
        question2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question2.isChecked()) {
                    results.add("Question2");
                } else {
                    results.remove("Question2");
                }
            }
        });
        question3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question1.isChecked()) {
                    results.add("Question3");
                } else {
                    results.remove("Question3");
                }
            }
        });
        question4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question3.isChecked()) {
                    results.add("Question4");
                } else {
                    results.remove("Question4");
                }
            }
        });
        question5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question4.isChecked()) {
                    results.add("Question5");
                } else {
                    results.remove("Question5");
                }
            }
        });
        question6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question1.isChecked()) {
                    results.add("Question6");
                } else {
                    results.remove("Question6");
                }
            }
        });
        question7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question7.isChecked()) {
                    results.add("Question7");
                } else {
                    results.remove("Question7");
                }
            }
        });
        question8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question8.isChecked()) {
                    results.add("Question8");
                } else {
                    results.remove("Question8");
                }
            }
        });
        question9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question9.isChecked()) {
                    results.add("Question9");
                } else {
                    results.remove("Question9");
                }
            }
        });
        question10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (question10.isChecked()) {
                    results.add("Question10");
                } else {
                    results.remove("Question10");
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