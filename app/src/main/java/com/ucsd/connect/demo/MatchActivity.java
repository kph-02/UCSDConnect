package com.ucsd.connect.demo;
        ;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

        import android.app.Activity;
        import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

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

import com.ucsd.connect.demo.R;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MatchActivity extends AppCompatActivity {

    BubblePicker picker;

    private String matchName;
    private String[] matchTraits;
    private String[] myTraits;
    private ArrayList<String> similarTraits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        //TODO: (For brandon, thanks im dumb lol) Set myTraits to current user's questionare answers.
        //this.myTraits =

        getNewMatch();
        TextView matchNameText = (TextView) findViewById(R.id.match_name);
        matchNameText.setText(this.matchName);
        setBubblePicker();

        Button findTritonsBtn = (Button) findViewById(R.id.find_tritons_button);

        findTritonsBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                getNewMatch();
                matchNameText.setText(matchName);
                setBubblePicker();
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

            }
        });
    }




    /*
    * TODO: Implement firebase stuff.
    * This method should first designate a random user in firebase user database
    * It then should set matchName instance variable to the username of the random match.
    * It then should set matchTraits instance variable to all the traits in the firebase database.
    *
     */
    private void getNewMatch() {

    }

    private void compareTraits() {

        int myTraitsLength = this.myTraits.length;
        int matchTraitsLength = this.matchTraits.length;
        for (int i = 0; i < myTraitsLength; i++) {
            for (int j = 0; i < matchTraitsLength; i++) {
                if(myTraits[i].equals(matchTraits[j])) {
                    similarTraits.add(myTraits[i]);
                }
            }
        }
    }

    private void setBubblePicker() {
        compareTraits();
        String[] traits = (String[]) this.similarTraits.toArray();
        //traits = getResources().getStringArray(R.array.matchTraits);
        final TypedArray colors = getResources().obtainTypedArray(R.array.colors);
        final TypedArray images = getResources().obtainTypedArray(R.array.images);
        picker =findViewById(R.id.picker);
        picker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return traits.length;
            }

            @NotNull
            @Override
            public PickerItem getItem(int position) {
                PickerItem item = new PickerItem();
                item.setTitle(traits[position]);
                item.setGradient(new BubbleGradient(colors.getColor((position * 2) % 8, 0),
                        colors.getColor((position * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
                //item.setTypeface(mediumTypeface);
                item.setTextColor(ContextCompat.getColor(MatchActivity.this, android.R.color.white));
                item.setBackgroundImage(ContextCompat.getDrawable(MatchActivity.this, images.getResourceId(position, 0)));
                return item;
            }
        });

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