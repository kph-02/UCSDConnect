package com.ucsd.connect.demo;

import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ucsd.connect.demo.User.UserListAdapter;
import com.ucsd.connect.demo.User.UserProfile;
import com.ucsd.connect.demo.User.UserListAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FindUserActivity extends AppCompatActivity {

    private RecyclerView mUserList;
    private RecyclerView.Adapter mUserListAdapter;
    private RecyclerView.LayoutManager mUserListLayoutManager;
    private BottomNavigationView menu;

    ArrayList<UserProfile> userList, contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        contactList= new ArrayList<>();
        userList= new ArrayList<>();

        Button mCreate = findViewById(R.id.create);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createChat();
                startActivity(new Intent(getApplicationContext(), ContactPageActivity.class));
            }
        });

        initializeRecyclerView();
        getContactList();

        menu = (BottomNavigationView)findViewById(R.id.bottomNavigationView);
        menu.setSelectedItemId(R.id.friendsMenu);
        menu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profileMenu:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        break;
                    case R.id.matchMenu:
                        startActivity(new Intent(getApplicationContext(), MatchActivity.class));
                        break;
                }
                return false;
            }
        });
    }

    private void createChat(){
        String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

        DatabaseReference chatInfoDb = FirebaseDatabase.getInstance().getReference().child("chat").child(key).child("info");
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("user");

        HashMap newChatMap = new HashMap();
        newChatMap.put("id", key);
        newChatMap.put("users/" + FirebaseAuth.getInstance().getUid(), true);

        Boolean validChat = false;
        for(UserProfile mUser : userList){
            if(mUser.getSelected()){
                validChat = true;
                newChatMap.put("users/" + mUser.getUid(), true);
                userDb.child(mUser.getUid()).child("chat").child(key).setValue(true);
            }
        }

        if(validChat){
            chatInfoDb.updateChildren(newChatMap);
            userDb.child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(true);
        }

    }

    private void getContactList(){

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
                        if (childSnapshot.getKey().equals(FirebaseAuth.getInstance().getUid())) {
                            continue;
                        }
                        boolean repeat = false;
                        for(DataSnapshot chatSnapshot: childSnapshot.child("chat").getChildren()) {
                            for(String chat: chats) {
                                if(chatSnapshot.getKey().equals(chat)) {
                                    repeat = true;
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

                        UserProfile mUser = new UserProfile(childSnapshot.getKey(), userAge, userName, userEmail);

                        userList.add(mUser);
                        mUserListAdapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getUserDetails(UserProfile mContact) {

    }


    private void initializeRecyclerView() {
        mUserList= findViewById(R.id.userList);
        mUserList.setNestedScrollingEnabled(false);
        mUserList.setHasFixedSize(false);
        mUserListLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayout.VERTICAL, false);
        mUserList.setLayoutManager(mUserListLayoutManager);
        mUserListAdapter = new UserListAdapter(userList);
        mUserList.setAdapter(mUserListAdapter);
    }
}