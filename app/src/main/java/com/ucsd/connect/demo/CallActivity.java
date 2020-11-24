package com.ucsd.connect.demo;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.ucsd.connect.demo.R.id;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ktx.DatabaseKt;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class CallActivity extends AppCompatActivity {


    private String username = "";
    private String friendsUsername = "";
    private String friendsUid = "";
    private boolean isPeerConnected = false;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference userDbRef = firebaseDatabase.getReference("user");
    private FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    private boolean isAudio = true;
    private boolean isVideo = true;
    private String uniqueId = "";
    private WebView webView = (WebView) findViewById(R.id.webView);
    private RelativeLayout callLayout = (RelativeLayout) findViewById(id.callLayout);
    private TextView incomingCallTxt = (TextView) findViewById(id.incomingCallTxt);
    private RelativeLayout inputLayout = (RelativeLayout) findViewById(id.inputLayout);
    private LinearLayout callControlLayout = (LinearLayout) findViewById(id.callControlLayout);
    private boolean friendsNameValidity = false;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);



        CallActivity.this.setUsername(userDbRef.child(firebaseAuth.getUid()).child("userName").getKey());

        Button callBtn = (Button) findViewById(R.id.callBtn);
        ImageView toggleAudioBtn = (ImageView) findViewById(R.id.toggleAudioBtn);
        ImageView toggleVideoBtn = (ImageView) findViewById(R.id.toggleVideoBtn);




        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Create better friend calling method
                EditText friendNameEdit = (EditText) findViewById(id.friendNameEdit);
                CallActivity.this.setFriendsUsername(friendNameEdit.getText().toString());
                if (friendNameValid(friendsUsername) == true ) {
                    CallActivity.this.sendCallRequest();
                } else {
                    Toast.makeText(CallActivity.this, (CharSequence)"This user does not exist.", Toast.LENGTH_LONG).show();
                }
            }
        });
        toggleAudioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallActivity.this.setAudio(!CallActivity.this.isAudio());
                CallActivity.this.callJavascriptFunction("javascript:toggleAudio(\"" + CallActivity.this.isAudio() + "\")");
                toggleAudioBtn.setImageResource(CallActivity.this.isAudio() ? R.drawable.ic_baseline_mic_24 : R.drawable.ic_baseline_mic_off_24);
            }
        });
        toggleVideoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallActivity.this.setVideo(!CallActivity.this.isVideo());
                CallActivity.this.callJavascriptFunction("javascript:toggleVideo(\"" + CallActivity.this.isVideo() + "\")");
                toggleVideoBtn.setImageResource(CallActivity.this.isVideo() ? R.drawable.ic_baseline_videocam_24 : R.drawable.ic_baseline_videocam_off_24);
            }
        });

    }

    private boolean friendNameValid(String friendsUsername) {
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference();
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.child("user").getChildren()) {
                        String tempFriendUsername = (String) userSnapshot.child("userName").getValue();
                        if (tempFriendUsername.equals(friendsUsername)) {
                            CallActivity.this.friendsNameValidity = true;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return CallActivity.this.friendsNameValidity;
    }

    private String getFriendsUid(String friendsUsername) {
        DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference();
        mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.child("user").getChildren()) {
                        String tempFriendUsername = (String) userSnapshot.child("userName").getValue();
                        if (tempFriendUsername.equals(friendsUsername)) {
                            CallActivity.this.friendsUid = (String) userSnapshot.child("uid").getValue();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error){
            }
        });
        return CallActivity.this.friendsUid;
    }

    private void sendCallRequest() {
        if (!this.isPeerConnected) {
            Toast.makeText(CallActivity.this, (CharSequence)"You're not connected. Check your internet", Toast.LENGTH_LONG).show();
        } else {
            userDbRef.child(CallActivity.this.friendsUid).child("call").child("incoming").setValue(username);
            userDbRef.child(CallActivity.this.friendsUid).child("call").child("isAvailable").addValueEventListener((ValueEventListener)(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue().toString() == "true") {
                        listenforConnId();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            }));
        }
    }

    private void listenforConnId() {
        userDbRef.child(CallActivity.this.friendsUid).child("call").child("connId").addValueEventListener((ValueEventListener) (new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() == null) {
                    return;
                }
                CallActivity.this.switchToControls();
                CallActivity.this.callJavascriptFunction("javascript:startCall(\"" + snapshot.getValue() + "\")");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        }));
    }


    private void setupWebView() {

       webView.setWebChromeClient((WebChromeClient) new WebChromeClient() {
           public void onPermissionRequest(PermissionRequest request) {
               if (request != null) {
                   request.grant(request.getResources());
               }
           }
       });
       WebSettings webSettings  = webView.getSettings();
       webSettings.setJavaScriptEnabled(true);
       webSettings = webView.getSettings();
       webSettings.setMediaPlaybackRequiresUserGesture(false);
       webView.addJavascriptInterface(new Javascriptinterface(), "Android");
       this.loadVideoCall();
    }

    private void loadVideoCall() {

        String filePath = "file:android_asset/call.html";
        webView.loadUrl(filePath);
        webView.setWebViewClient((WebViewClient)(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                CallActivity.this.initializePeer();
            }
        }));
    }


    private void initializePeer() {

        uniqueId = getUniqueId();
        callJavascriptFunction("javascript:init(\"" + uniqueId + "\")");
        userDbRef.child(firebaseAuth.getUid()).child("call").child("incoming").addValueEventListener((ValueEventListener)(new ValueEventListener() {
            public void onCancelled(@NotNull DatabaseError error) {
                Intrinsics.checkParameterIsNotNull(error, "error");
            }

            public void onDataChange(@NotNull DataSnapshot snapshot) {
                Intrinsics.checkParameterIsNotNull(snapshot, "snapshot");
                Object snapshotValue = snapshot.getValue();
                if (!(snapshotValue instanceof String)) {
                    snapshotValue = null;
                }
                CallActivity.this.onCallRequest((String)snapshotValue);
            }
        }));
    }

    private final void onCallRequest(String caller) {
        if (caller != null) {
            callLayout.setVisibility(View.VISIBLE);
            incomingCallTxt.setText((CharSequence)(caller + " is calling..."));
            ImageView acceptBtn = (ImageView) findViewById(id.acceptBtn);
            ImageView rejectBtn = (ImageView) findViewById(id.rejectBtn);

            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userDbRef.child(firebaseAuth.getUid()).child("call").child("connId").setValue(uniqueId);
                    userDbRef.child(firebaseAuth.getUid()).child("call").child("isAvailable").setValue(true);

                    callLayout.setVisibility(View.GONE);
                    switchToControls();
                }
            });
            rejectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userDbRef.child(firebaseAuth.getUid()).child("call").child("incoming").setValue(null);
                }
            });

        } else {
            return;
        }
    }

    private void switchToControls() {
        inputLayout.setVisibility(View.GONE);
        callControlLayout.setVisibility(View.VISIBLE);
    }


    private String getUniqueId() {
        return currentFirebaseUser.getUid();
    }

    private void callJavascriptFunction(String functionString) {
        webView.post((Runnable)(new Runnable() {
            public final void run() {
                webView.evaluateJavascript(functionString, (ValueCallback)null);
            }

        }));
    }




    @NotNull
    public final String getUsername() {
        return this.username;
    }

    public final void setUsername(@NotNull String var1) {
        Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
        this.username = var1;
    }

    @NotNull
    public final String getFriendsUsername() {
        return this.friendsUsername;
    }

    public final void setFriendsUsername(@NotNull String var1) {
        Intrinsics.checkParameterIsNotNull(var1, "<set-?>");
        this.friendsUsername = var1;
    }

    public final boolean isPeerConnected() {
        return this.isPeerConnected;
    }

    public final void setPeerConnected(boolean var1) {
        this.isPeerConnected = var1;
    }

    public void onBackPressed() {
        this.finish();
    }

    protected void onDestroy() {
        this.userDbRef.child(this.username).setValue((Object)null);
        webView.loadUrl("about:blank");
        super.onDestroy();
    }

    public final boolean isAudio() {
        return this.isAudio;
    }

    public final void setAudio(boolean var1) {
        this.isAudio = var1;
    }

    public final boolean isVideo() {
        return this.isVideo;
    }

    public final void setVideo(boolean var1) {
        this.isVideo = var1;
    }

    public void onPeerConnected() {
        isPeerConnected = true;
    }
}
