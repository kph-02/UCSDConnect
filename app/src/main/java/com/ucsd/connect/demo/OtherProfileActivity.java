package com.ucsd.connect.demo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.ucsd.connect.demo.Chat.ChatObject;
import com.ucsd.connect.demo.User.UserProfile;

public class OtherProfileActivity extends AppCompatActivity {

    private ImageView profilePic;
    private TextView profileName, profileAge, profileEmail;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private String chatUid;
    private BottomNavigationView menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("test", "OtherProfile");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewprofile);
        chatUid = (String) getIntent().getSerializableExtra("chatUid");
        Log.d("test", chatUid);
        profilePic = findViewById(R.id.ivProfilePic);
        profileName = findViewById(R.id.tvProfileName);
        profileAge = findViewById(R.id.tvProfileAge);
        profileEmail = findViewById(R.id.tvProfileEmail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();

        DatabaseReference databaseReference = firebaseDatabase.getReference().child("user").child(chatUid);

        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.child(chatUid).child("Images/Profile Pic").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).fit().centerCrop().into(profilePic);
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserProfile userProfile = dataSnapshot.getValue(UserProfile.class);
                profileName.setText(userProfile.getUserName());
                profileAge.setText(userProfile.getUserAge());
                profileEmail.setText(userProfile.getUserEmail());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(OtherProfileActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

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
                        startActivity(new Intent(getApplicationContext(), SecondActivity.class));
                        break;
                }
                return false;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
