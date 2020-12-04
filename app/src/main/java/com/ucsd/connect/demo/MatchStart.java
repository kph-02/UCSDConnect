package com.ucsd.connect.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ucsd.connect.demo.Chat.ChatObject;
import com.ucsd.connect.demo.User.UserProfile;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MatchStart extends AppCompatActivity {

    private List<String> myTraits;
    private ArrayList<String> similarTraits;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private BottomNavigationView menu;
    private UserProfile matchUser;
    private ArrayList<UserProfile> userList;
    private UserProfile currUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_start);

        currUser = new UserProfile();
        matchUser = new UserProfile();
        myTraits = new ArrayList<String>();
        similarTraits = new ArrayList<String>();
        userList = new ArrayList<UserProfile>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        getCurrUser();
        getPotMatch();

        Button matchBtn = (Button) findViewById(R.id.btnMatchStart);

        matchBtn.setOnClickListener(new View.OnClickListener() {

            /*
             * TODO: Start new Chat activity with current match.
             *
             */
            @Override
            public void onClick(View view) {
//                MatchActivity.similarTraits = similarTraits;
//                MatchActivity.matchUser = matchUser;
//                MatchActivity.currUser = currUser;
                Intent intent = new Intent(view.getContext(), MatchActivity.class);
                intent.putExtra("similarTraits", similarTraits);
                intent.putExtra("matchUser", matchUser);
                intent.putExtra("currUser", currUser);
                view.getContext().startActivity(intent);
            }
        });

        menu = (BottomNavigationView)findViewById(R.id.bottomNavigationView);
        menu.setSelectedItemId(R.id.matchMenu);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profileMenu:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        finish();
                        break;
                    case R.id.friendsMenu:
                        startActivity(new Intent(getApplicationContext(), ContactPageActivity.class));
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    public void getCurrUser() {
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("user").child(firebaseAuth.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (String trait: (List<String>) dataSnapshot.child("traits").getValue()) {
                    myTraits.add(trait);
                }
                currUser = new UserProfile(dataSnapshot.getValue(UserProfile.class));
                Log.d("test", "curr user set");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MatchStart.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNewMatch() {
        getPotMatch();
        int randInt = (int)(Math.random()*userList.size());
        matchUser = userList.get(randInt);
        compareTraits();
    }

    private void compareTraits() {
        similarTraits = new ArrayList<String>();
        if (myTraits != null && matchUser.getTraits() != null){
            for (String trait : myTraits) {
                for (String otrait: matchUser.getTraits()) {
                    if (trait.equals(otrait)) {
                        similarTraits.add(trait);
                    }
                }
            }
        }

    }

    private int scoreTraits(ArrayList<String> oTraits) {
        int c = 0;
        if(myTraits != null && oTraits != null) {
            for (String trait : myTraits) {
                for (String otrait: oTraits) {
                    if (trait.equals(otrait)) {
                        c++;
                    }
                }
            }
        }
        return c;
    }

    private void getPotMatch() {
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user");
        Query query = mUserDB.orderByChild("userEmail");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String  userEmail = "",
                            userName = "",
                            userAge = "";
                    ArrayList<String> chats = new ArrayList<String>();
                    for(DataSnapshot chatSnapshot: dataSnapshot.child(FirebaseAuth.getInstance().getUid()).child("chat").getChildren()){
                        chats.add(chatSnapshot.getKey());
                    }
                    Log.d("test", chats.toString());
                    for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                        ArrayList<String> potMatch = new ArrayList<String>();
                        if (childSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())) {
                            continue;
                        }
                        boolean repeat = false;
                        for(DataSnapshot chatSnapshot: childSnapshot.child("chat").getChildren()) {
                            for(String chat: chats) {
                                if(chatSnapshot.getKey().equals(chat)) {
                                    repeat = true;
                                    break;
                                }
                            }
                        }
                        if(repeat) {
                            continue;
                        }
                        if (childSnapshot.child("userEmail").getValue() != null)
                            userEmail = childSnapshot.child("userEmail").getValue().toString();
                        if (childSnapshot.child("userName").getValue() != null)
                            userName = childSnapshot.child("userName").getValue().toString();
                        if (childSnapshot.child("userAge").getValue() != null)
                            userAge = childSnapshot.child("userAge").getValue().toString();

                        for (DataSnapshot trait : childSnapshot.child("traits").getChildren()) {
                            potMatch.add((String) trait.getValue());
                        }
                        if (scoreTraits(potMatch) > 3) {
                            UserProfile mUser = new UserProfile(childSnapshot.getKey(), userAge, userEmail, userName, potMatch);
                            userList.add(mUser);
                        }
                    }
                    getNewMatch();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}