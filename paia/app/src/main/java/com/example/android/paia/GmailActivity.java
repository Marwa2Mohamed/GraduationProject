package com.example.android.paia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class GmailActivity extends AppCompatActivity {
    private GoogleSignInAccount account;
    private GoogleSignInOptions gso;
    private GoogleSignInClient googlesigninclient;
    private static final int RC_SIGN_IN = 1;
    private  String Gmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmail);

         gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googlesigninclient = GoogleSignIn.getClient(this, gso);
        SignInButton signInButton = findViewById(R.id.gmail);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    signIn();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }

    // to pop up the emails'option dialoug of google
    private void signIn() {
        Intent signInIntent = googlesigninclient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        }

    // to recieve the chosen email by the user

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    // The GoogleSignInAccount object(task) contains information about the signed-in user, such as the user's name.
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {

             account = completedTask.getResult(ApiException.class);
             Gmail=account.getEmail();
             updateUI(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.

            Toast.makeText(this,"signInResult:failed code=" + e.getStatusCode(),Toast.LENGTH_LONG);

        }

    }
 private void updateUI(GoogleSignInAccount account){
        if(account!=null){
        Intent intent=new Intent(this,page4.class);
        intent.putExtra("Gmail",Gmail);
        startActivity(intent);}
 }

 private void updateUIwithGmail(GoogleSignInAccount account){
     Intent intent=new Intent(this,page4.class);
     intent.putExtra("Gmail",Gmail);
     startActivity(intent);
 }
}
