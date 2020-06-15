package com.example.android.paia;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/*This activity won't be reached by the code,
 it is just a prototype for changing/doing an action(change the background button)
 within time for later use in the application*/

public class AppActivity extends AppCompatActivity {

    Button changedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        changedButton= (Button) findViewById(R.id.changedbutton);
        changedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent =new Intent(AppActivity.this,DataActivity.class);
//                startActivity(intent);

            }
        });
        //Create the timer object which will run the desired operation on a schedule or at a given time
        Timer timer = new Timer();

        //Create a task which the timer will execute.  This should be an implementation of the TimerTask interface.
        //I have created an inner class below which fits the bill.
        MyTimer mt = new MyTimer();

        //We schedule the timer task to run after 1000 ms and continue to run every 1000 ms.
        timer.schedule(mt, 5000, 5000);
    }
    //An inner class which is an implementation of the TImerTask interface to be used by the Timer.
    class MyTimer extends TimerTask {

        public void run() {

            //This runs in a background thread.
            //We cannot call the UI from this thread, so we must call the main UI thread and pass a runnable
            runOnUiThread(new Runnable() {

                public void run() {
                    Random rand = new Random();
                    //The random generator creates values between [0,256) for use as RGB values used below to create a random color
                    //We call the RelativeLayout object and we change the color.  The first parameter in argb() is the alpha.
                    changedButton.setBackgroundColor(Color.argb(255, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256)));
                }
            });
        }

    }
}