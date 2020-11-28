package com.ucsd.connect.demo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.ucsd.connect.demo.R.id;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.ktx.FirebaseKt;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.Nullable;



public class StartCall extends AppCompatActivity{

    private final String[] permissions = new String[]{"android.permission.CAMERA", "android.permission.RECORD_AUDIO"};
    private final int requestCode = 1;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_call);
        if (!this.isPermissionGranted()) {
            this.askPermissions();
        }
        Button loginBtn = (Button) findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener((OnClickListener)(new OnClickListener() {
            public final void onClick(View it) {
                EditText usernameEdit = (EditText) findViewById(R.id.usernameEdit);
                String username = usernameEdit.getText().toString();
                Intent intent = new Intent(StartCall.this, CallActivity.class);
                startActivity(intent);
            }
        }));
    }

    private boolean isPermissionGranted() {
        for(String permission : permissions) {
            if(ActivityCompat.checkSelfPermission((Context)this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private final void askPermissions() {
        ActivityCompat.requestPermissions((Activity)this, this.permissions, this.requestCode);
    }





}
