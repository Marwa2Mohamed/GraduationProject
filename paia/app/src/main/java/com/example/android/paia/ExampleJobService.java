package com.example.android.paia;

import android.app.Activity;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ExampleJobService extends JobService {

    Rating rating= new Rating();
    int Acc_id = 1, period = 0;
    TemproryFuctions temproryFuctions = new TemproryFuctions();
    private static final String TAG = "ExampleJobService";
    private boolean jobCancelled = false;
    HashMap<Integer, Float> general_rating = new HashMap<>();
    ArrayList<ArrayList> parsedData= new ArrayList<>();
    ArrayList<Integer> displayed= new ArrayList<>();
    HashMap<Integer, Float> temp_rating = new HashMap<Integer, Float>();

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.e(TAG, "Job started");
        doBackgroundWork(jobParameters);

        return true;
    }

    private void doBackgroundWork(final JobParameters jobParameters){
        new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {

                if(jobCancelled)
                {
                    return;
                }
///  error general_rating.size and parsedData.size=0

                general_rating= temproryFuctions.general_rating;
                parsedData = temproryFuctions.parsedData;
                Log.v("ServiceGeneral_Rating: ", String.valueOf(general_rating.size()));
                Log.v("ServiceParse_Rating: ", String.valueOf(parsedData.size()));
                //check current hour
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh");
                int hour = Integer.parseInt( dateFormat.format(cal.getTime()) );
                //reset period to 0 when a new day starts

                    if(hour == 00 ||hour == 01){
                        period = 0;
                    }else {
                        temp_rating = rating.newRatings(parsedData.get(period++), general_rating);
                        TemproryFuctions.temp_rating=temp_rating;
                        Log.v("Job Temp_rating:", String.valueOf(TemproryFuctions.temp_rating));
                        if (displayed.isEmpty()) {
                            int rank = 0;
                            for (int id : temp_rating.keySet()) {
                                displayed.add(id);
                                if (rank++ < 5) break;
                            }
                            TemproryFuctions.displayed=displayed;
                            Log.v("Job Display:", String.valueOf(TemproryFuctions.displayed));
                        }
                    }

                jobFinished(jobParameters,false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {

        Log.e(TAG,"Job has been cancelled!");
        jobCancelled = true;
        return true;
    }
}
