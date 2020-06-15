package com.example.android.paia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;


public class PermissionActivity extends AppCompatActivity {
    private final int PERMISSION_CODE=1;
    final String[] Permissions={Manifest.permission.READ_CONTACTS,Manifest.permission.READ_CALL_LOG};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        Button finish= (Button) findViewById(R.id.Permission);
        Button back=(Button) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(PermissionActivity.this,page3.class);
                startActivity(intent);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){

                    if(hasPermission(PermissionActivity.this,Permissions)==true){
                        Intent intent= new Intent(PermissionActivity.this,GmailActivity.class);
                        startActivity(intent);
                    }else{
                        ActivityCompat.requestPermissions(PermissionActivity.this, Permissions, PERMISSION_CODE);
                    }
                }
            }
        });
    }
    private boolean hasPermission(Context context, String []perimissions){
        for(String perm:perimissions){
            if(ActivityCompat.checkSelfPermission(context,perm)!=PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }
}
