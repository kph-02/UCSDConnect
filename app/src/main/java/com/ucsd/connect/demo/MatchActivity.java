package com.ucsd.connect.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

        import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
        import android.widget.Button;
import android.widget.ImageView;
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
import com.ucsd.connect.demo.User.UserListAdapter;
import com.ucsd.connect.demo.User.UserProfile;
import com.ucsd.connect.demo.User.UserListAdapter;

import com.ucsd.connect.demo.R;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MatchActivity extends AppCompatActivity {

    BubblePicker picker;

    private String matchName;
    private String matchUid;
    private List<String> matchTraits;
    private List<String> myTraits;
    private ArrayList<String> similarTraits;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private BottomNavigationView menu;
    private ChatObject matchChat;
    private UserProfile currUser;
    private UserProfile matchUser;
    private TextView matchNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        myTraits = new ArrayList<String>();
        matchTraits = new ArrayList<String>();
        similarTraits = new ArrayList<String>();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("user").child(firebaseAuth.getUid());

        Log.d("test", "hi");
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
                Toast.makeText(MatchActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        Log.d("test", myTraits.toString());

        getNewMatch();
        matchNameText = (TextView) findViewById(R.id.match_name);
        matchNameText.setText(this.matchName);
        picker = findViewById(R.id.picker);
        setBubblePicker();
        Button findTritonsBtn = (Button) findViewById(R.id.find_tritons_button);

        findTritonsBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                getNewMatch();
            }
        });

        Button connectBtn = (Button) findViewById(R.id.find_Connect);

        connectBtn.setOnClickListener(new View.OnClickListener() {

            /*
             * TODO: Start new Chat activity with current match.
             *
             */
            @Override
            public void onClick(View view) {
                createChat();
                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                intent.putExtra("chatObject", matchChat);
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
                        break;
                    case R.id.friendsMenu:
                        startActivity(new Intent(getApplicationContext(), ContactPageActivity.class));
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


        newChatMap.put("users/" + matchUid, true);
        userDb.child(matchUid).child("chat").child(key).setValue(true);



        chatInfoDb.updateChildren(newChatMap);
        userDb.child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(true);

        matchChat = new ChatObject(key, currUser, (ArrayList<UserProfile>) Arrays.asList(currUser, matchUser));

    }


    /*
    * TODO: Implement firebase stuff.
    * This method should first designate a random user in firebase user database
    * It then should set matchName instance variable to the username of the random match.
    * It then should set matchTraits instance variable to all the traits in the firebase database.
    *
     */
    private void getNewMatch() {
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user");
        mUserDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int randInt = (int)(Math.random()*((double)dataSnapshot.getChildrenCount()));
                    int count = 0;
                    Log.d("test", Integer.toString(count));
                    for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                        Log.d("test", Integer.toString(count));
                        if (count == randInt) {
                            matchNameText.setText((String) userSnapshot.child("userName").getValue());
                            matchUid = (String) userSnapshot.child("uid").getValue();
                            for (DataSnapshot trait : userSnapshot.child("traits").getChildren()) {
                                matchTraits.add((String) trait.getValue());
                            }
                            matchUser = new UserProfile(userSnapshot.getValue(UserProfile.class));
                            break;
                        }
                        count++;
                    }
                }
                setBubblePicker();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void compareTraits() {
        if (myTraits != null && matchTraits != null){
            Log.d("test", "comparing");
            int myTraitsLength = this.myTraits.size();
            int matchTraitsLength = this.matchTraits.size();
            for (int i = 0; i < myTraitsLength; i++) {
                for (int j = 0; j < matchTraitsLength; j++) {
                    if(myTraits.get(i).equals(matchTraits.get(j))) {
                        similarTraits.add(myTraits.get(i));
                    }
                }
            }
        }

    }

    private void setBubblePicker() {
        compareTraits();
        //String[] traits = similarTraits.toArray(new String[similarTraits.size()]);
        //String[] traits = getResources().getStringArray(R.array.matchTraits);
        //String[] traits = {"Question1","Question2","Question3","Question4","Question5","Question6","Question7","Question8","Question9","Question10" };
        //Log.d("test", Arrays.toString(traits));
        final TypedArray colors = getResources().obtainTypedArray(R.array.colors);
        final TypedArray images = getResources().obtainTypedArray(R.array.images);


        picker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return similarTraits.size();
            }

            @NotNull
            @Override
            public PickerItem getItem(int position) {
                PickerItem item = new PickerItem();
                item.setTitle(similarTraits.get(position));
                item.setGradient(new BubbleGradient(colors.getColor((position * 2) % 8, 0),
                        colors.getColor((position * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
                //item.setTypeface(mediumTypeface);
                item.setTextColor(ContextCompat.getColor(MatchActivity.this, android.R.color.white));
                item.setBackgroundImage(ContextCompat.getDrawable(MatchActivity.this, images.getResourceId(position, 0)));
                return item;
            }
        });
        Log.d("test", "PAIN");

        picker.setCenterImmediately(true);
        picker.setBubbleSize(100);

        picker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem item) {

            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem item) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        picker.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        picker.onPause();
    }
}