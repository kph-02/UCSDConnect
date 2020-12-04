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

    private BottomNavigationView menu;
    private ChatObject matchChat;
//    public static ArrayList<String> similarTraits;
//    public static UserProfile currUser;
//    public static UserProfile matchUser;
    private ArrayList<String> similarTraits;
    private UserProfile currUser;
    private UserProfile matchUser;
    private TextView matchNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        currUser = (UserProfile) getIntent().getSerializableExtra("currUser");
        matchUser = (UserProfile) getIntent().getSerializableExtra("matchUser");
        similarTraits = (ArrayList<String>) getIntent().getSerializableExtra("similarTraits");

        Log.d("test", currUser.getUid());
        Log.d("test", matchUser.getUid());
        Log.d("test", Integer.toString(similarTraits.size()));

        matchNameText = findViewById(R.id.match_name);
        picker = findViewById(R.id.picker);

        matchNameText.setText(matchUser.getUserName());

        setBubblePicker();
        Button findTritonsBtn = (Button) findViewById(R.id.find_tritons_button);

        findTritonsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MatchStart.class);
                view.getContext().startActivity(intent);
                finish();
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
                finish();
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

    private void createChat(){
        String key = FirebaseDatabase.getInstance().getReference().child("chat").push().getKey();

        DatabaseReference chatInfoDb = FirebaseDatabase.getInstance().getReference().child("chat").child(key).child("info");
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("user");

        HashMap newChatMap = new HashMap();
        newChatMap.put("id", key);
        newChatMap.put("users/" + FirebaseAuth.getInstance().getUid(), true);


        newChatMap.put("users/" + matchUser.getUid(), true);
        userDb.child(matchUser.getUid()).child("chat").child(key).setValue(true);
        Log.d("test", matchUser.getUid());

        chatInfoDb.updateChildren(newChatMap);
        userDb.child(FirebaseAuth.getInstance().getUid()).child("chat").child(key).setValue(true);

        matchChat = new ChatObject(key, currUser, new ArrayList<UserProfile>(Arrays.asList(currUser, matchUser)));
        matchChat.setOtherUser(matchUser);

    }

    private void setBubblePicker() {
        final TypedArray colors = getResources().obtainTypedArray(R.array.colors);
        final TypedArray images = getResources().obtainTypedArray(R.array.images);


        picker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return similarTraits.size();
                // return similarTraits.size();
            }

            @NotNull
            @Override
            public PickerItem getItem(int position) {
                PickerItem item = new PickerItem();
                item.setTitle(similarTraits.get(position));
                //item.setTitle(allTraits[position]);
                item.setGradient(new BubbleGradient(colors.getColor((position * 2) % 8, 0),
                        colors.getColor((position * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
                //item.setTypeface(mediumTypeface);
                item.setTextColor(ContextCompat.getColor(MatchActivity.this, android.R.color.white));
                item.setBackgroundImage(ContextCompat.getDrawable(MatchActivity.this, images.getResourceId(position, 0)));
                return item;
            }
        });
        Log.d("test", "PAIN");

        colors.recycle();
        images.recycle();

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