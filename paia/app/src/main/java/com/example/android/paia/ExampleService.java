package com.example.android.paia;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import android.support.v4.app.JobIntentService;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import android.content.Intent;

import static android.os.IBinder.TWEET_TRANSACTION;

public class ExampleService extends Service {

    Rating rating = new Rating();
    String Acc_id2 ;
    int Acc_id=1;
    String last_day="";
    private Handler mHandler = new Handler();
    private Looper looper;
    private ServiceHandler serviceHandler;
    JSONObject MyData = new JSONObject();
    ArrayList<DATA> ContactsData = new ArrayList<>();
    ArrayList<FilteredData> filteredDataArrayList = new ArrayList<>();
    HashMap<String, String> compareIDs = new HashMap<>();
    boolean contact_is_found_in_hash = false;
    ArrayList<ArrayList> parsedData= new ArrayList<>();
    TemproryFuctions temproryFuctions= new TemproryFuctions();
    HashMap<Integer, Integer> frequency = new HashMap<>();
    HashMap<Integer, Float> general_rating = new HashMap<>();
    public static DecimalFormat df2 = new DecimalFormat("#.##");

    RequestQueue requestQueue;


    private static final String TAG ="ExampleService";
    @Override
    public void onCreate() {

        Log.v(TAG,"onCreate");

        HandlerThread thread = new HandlerThread("Service", Process.THREAD_PRIORITY_BACKGROUND);

        thread.start();

        looper = thread.getLooper();
        serviceHandler = new ServiceHandler(looper);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        HashMap<String, String> temprory= new HashMap<>();
        Acc_id2 = intent.getStringExtra("Acc_id");
        Acc_id = Integer.parseInt(Acc_id2);
        Log.v("Accid: ", String.valueOf(Acc_id));

        compareIDs =(HashMap<String, String>) intent.getSerializableExtra("compareid");
        Log.v("CompareIDS Size", String.valueOf(compareIDs.size()));
        Log.v(TAG,"onStart");

        Toast.makeText(ExampleService.this,"service started",Toast.LENGTH_LONG).show();

        frequency=(HashMap<Integer, Integer>) intent.getSerializableExtra("frequency");
        Log.v("Frequency12:", String.valueOf(frequency.size()));

        general_rating =(HashMap<Integer, Float>) intent.getSerializableExtra("generalratings");
        Log.v("GeneralRatings12:", String.valueOf(general_rating.size()));

        Message msg = serviceHandler.obtainMessage();
        Log.e("Message:", String.valueOf(msg));
        msg.arg1 = startId;
        serviceHandler.sendMessage(msg);


        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        Log.v(TAG,"onDestroy");
        Toast.makeText(this, "Service done",Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        Log.v(TAG,"onBind");
        return null;
    }

    private class MyTimer extends TimerTask {

         int debug=0;
        @RequiresApi(api = Build.VERSION_CODES.O)
        public void run() {

            Log.e("debug: ", String.valueOf(debug));
            debug++;

            CollectCallLogsData();
            filteredData();
            Log.e("CollectCallLogs-Size:", String.valueOf(ContactsData.size()));
            Log.e("Filtered-Size:", String.valueOf(filteredDataArrayList.size()));
            Log.e("CompareIDSHash:", String.valueOf(compareIDs.size()));
            TemproryFuctions.compareIDs=compareIDs;
            // Add Json Parsing Data Here
            FilterDataToJson();
            Log.e("MYDATA:", String.valueOf(MyData));
            // Send Data to Server
            ConnectionToServersend();

            // Calculate general rating

              calculate();
            // empty the filteredArraylist after sending the data
            filteredDataArrayList.clear();
            Log.e("Filtered-Size Clear:", String.valueOf(filteredDataArrayList.size()));


            // recieve from server


            mHandler.postDelayed(new Runnable(){
                @Override
                public void run() {

                    rating.ConnectionToServer(Acc_id);
                    try {
                        parsedData = rating.parseData();
                        Log.e("parsedData ","parsedDataaaaaaaaa"+parsedData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    temproryFuctions.parsedData=parsedData;
                    Log.e("temproryFuctions ","temproryFuctions has been called");
                    Log.e("ParsedData: ", String.valueOf(temproryFuctions.parsedData.size()));


                }
            },180000);




        }
    }
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void calculate() {

        //set variables to begin calculating general ratings

        int sum=0;
        int current_id = 0;
        int count = 0;

        //count frequency per contact

        Log.v("Filtered Size2:", String.valueOf(filteredDataArrayList.size()));
        for (FilteredData x : filteredDataArrayList) {

            Log.v("x2:", String.valueOf(x));
            current_id = Integer.parseInt(x.getID());
            Log.v("ci2:", String.valueOf(current_id));
            if (frequency.containsKey(current_id)) {

                count = frequency.get(current_id);
                Log.v("count2:", String.valueOf(count));
                frequency.replace(current_id, ++count);

            } else {
                frequency.put(current_id, 1);
                Log.v("current_id2:", String.valueOf(current_id));
            }

        }
        if (frequency.size() != 0) {
            for (int x : frequency.keySet()) {

                sum += frequency.get(x);
                //size++;
            }
            Log.v("Sum2: ", String.valueOf(sum));
            Log.v("Frequency size2: ", String.valueOf(frequency.size()));

            //Log.v("Size:", String.valueOf(size));

            float mean = sum / frequency.size();
            Log.v("Mean2: ", String.valueOf(mean));
            //calculate general ratings
            double rating = 0;
            for (int x : frequency.keySet()) {
                rating = frequency.get(x) / mean;
                rating = 1 / (1 + Math.pow(Math.E, (-1 * rating)));
                rating = Double.parseDouble(df2.format(rating));
                general_rating.put(x, (float) rating);
            }
            TemproryFuctions.general_rating=general_rating;

            Log.v("General_rating2 Class:", String.valueOf(general_rating.size()));
            Log.v("General_rating2 Temp:", String.valueOf(TemproryFuctions.general_rating.size()));
        }
    }
    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper){
            super(looper);
        }

        @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
        @Override
        public void handleMessage(Message msg) {




            // this part we collect data , convert it to json and send it to server

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY,11);
            today.set(Calendar.MINUTE, 13);
            today.set(Calendar.SECOND, 00);

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
            String day = dateFormat.format(cal.getTime());

            if(day == last_day) return;

            last_day = day;
            Timer timer = new Timer();
            MyTimer tn = new MyTimer();
            timer.schedule(tn,today.getTime(), TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)); // period: 1 day
        }
    }


    private void FilterDataToJson() {
        try {
            JSONArray filteredarray= new JSONArray();
            JSONObject temp=new JSONObject();
            temp.put("Acc_id",Acc_id);
            filteredarray.put(temp);

            for(int size=0;size<filteredDataArrayList.size();size++){

                FilteredData fobj=filteredDataArrayList.get(size);
                JSONObject temp2=new JSONObject();
                temp2.put("ID",fobj.getID());
                temp2.put("number",fobj.getNumber());
                temp2.put("time",fobj.getTime());
                temp2.put("Day",fobj.getDay());
                filteredarray.put(temp2);
            }
            MyData.put("CallLogs",filteredarray);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void filteredData() {

        //Variables &objects declerations

        Date today = new Date();// e.g Thu April 18 16:47:57 EET 2019
        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
        String calldate = "";

        // if Hash map(compareIDs) ==0 means the last_updated was yesterday and no data is stored there
        if (compareIDs.size() == 0) {
            for (int index = 0; index < ContactsData.size(); index++) {
                DATA dataobj = ContactsData.get(index);
                calldate = dataobj.getDate();
                long diffcontact = 0;
                int diffDayscontact = 0;

                try {
                    Date calldateobj = dateFormat.parse(calldate);
                    diffcontact = today.getTime() - calldateobj.getTime();// days difference for each call in long
                    diffDayscontact = (int) (diffcontact / (24 * 60 * 60 * 1000));// days difference for each call in int

                    if (dataobj.getName() != null && diffDayscontact == 1) {
                        //to send it to the server
                        filteredDataArrayList.add(new FilteredData(dataobj.getNumber(), dataobj.getDay(), dataobj.getTime(), dataobj.getID()));
                        compareIDs.put(dataobj.getNumber(), dataobj.getID());
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            // it means the hash map is not empty
        } else {
            for (int index = 0; index < ContactsData.size(); index++) {
                DATA dataobj = ContactsData.get(index);
                calldate = dataobj.getDate();
                long diffcontact = 0;
                int diffDayscontact = 0;

                try {
                    Date calldateobj = dateFormat.parse(calldate);
                    diffcontact = today.getTime() - calldateobj.getTime();// days difference for each call
                    diffDayscontact = (int) (diffcontact / (24 * 60 * 60 * 1000));// days difference for each call
                    if (dataobj.getName() != null && diffDayscontact == 1) {

                        for (String Key : compareIDs.keySet()) {
                            if (Key == dataobj.getNumber()) {
                                //to send it to the server
                                filteredDataArrayList.add(new FilteredData(Key, dataobj.getDay(), dataobj.getTime(), compareIDs.get(Key)));
                                contact_is_found_in_hash = true;
                            }
                        }
                        // that means the contact not found in the hash map
                        if (contact_is_found_in_hash == false) {

                            //to send it to the server
                            filteredDataArrayList.add(new FilteredData(dataobj.getNumber(), dataobj.getDay(), dataobj.getTime(), dataobj.getID()));
                            compareIDs.put(dataobj.getNumber(), dataobj.getID());
                        }

                    }
                }  catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    private void CollectCallLogsData() {

        String day = "", time = "", calenderdate = "";
        String[] EnglishConversion = new String[2];
        String[] callprojection = {CallLog.Calls.CACHED_NAME, CallLog.Calls.NUMBER, CallLog.Calls.DATE};
        Cursor cursorcall = getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, callprojection, null, null, null);

        while (cursorcall.moveToNext()) {
            //Extracting Coulmns
            String Name = cursorcall.getString(cursorcall.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String Number = cursorcall.getString(cursorcall.getColumnIndex(CallLog.Calls.NUMBER));
            Long calldate = Long.parseLong(cursorcall.getString(cursorcall.getColumnIndex(CallLog.Calls.DATE)));


            // Converting Date
            Date date = new Date(calldate);
            DateFormat dateobjectday = new SimpleDateFormat("EEE");
            DateFormat dateobjecttime = new SimpleDateFormat("HH");
            DateFormat dateobjectcalender = new SimpleDateFormat("yyyy-MM-dd");
            day = dateobjectday.format(date);
            calenderdate = dateobjectcalender.format(date);
            time = dateobjecttime.format(date);
            EnglishConversion[0] = day;
            EnglishConversion[1] = time;
            EnglishConversion = CheckifArabic(EnglishConversion);
            // Check whether the day and time in Arabic type or not
            day = EnglishConversion[0];
            time = EnglishConversion[1];


            // get the contact ID whose number=Numbervariable
            String ContactID = returnID(Number);

            // put all the pervious data/variables in the DATA object
            DATA dataobj = new DATA(Name, Number, day, time, calenderdate, ContactID);


            //Add to the ArrayList
            ContactsData.add(dataobj);

            // add the phone as a key and ID as a value to compare with later


        }
    }

    // is called in the CollectCallLogsData() to get the id for each callee in the call
    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    private String returnID(String phonenumber) {
        String ID = "";

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phonenumber));
        String[] columns = {ContactsContract.PhoneLookup.CONTACT_ID};
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, columns, null, null, null);


        if (cursor != null) {
            while (cursor.moveToNext()) {

                ID = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.CONTACT_ID));

            }

        } else {
            return null;
        }
        return ID;
    }

    // Here I am checking if the day is in Arabic Language

    public static String[] CheckifArabic(String[] Englishconversion) {
        String checkday = Englishconversion[0]; // holds the day in the CollectCallLogsData();
        String checktime = Englishconversion[1]; //  holds the time in the CollectCallLogsData()
        String[] translated = new String[2];
        boolean checkd = false;
        boolean checkt = false;

        // Check if the day in Arabic language

        for (char charac : Englishconversion[0].toCharArray()) {
            if (Character.UnicodeBlock.of(charac) == Character.UnicodeBlock.ARABIC) {
                checkd = true;
                break;

            }
        }
        if (checkd == true) {
            checkday = TranslatetDay(Englishconversion[0]);
        }

        // Check if the time in the Arabic language

        for (char charac : Englishconversion[1].toCharArray()) {
            if (Character.UnicodeBlock.of(charac) == Character.UnicodeBlock.ARABIC) {
                checkt = true;
                break;

            }
        }
        if (checkt == true) {
            checktime = Translatettime(Englishconversion[1]);
        }

        translated[0] = checkday;
        translated[1] = checktime;
        return translated;
    }

    public static String TranslatetDay(String checkday) {

        String returnday = "";
        switch (checkday) {
            case "السبت":
                returnday = "Sat";
                break;
            case "الأحد":
                returnday = "Sun";
                break;
            case "الاثنين":
                returnday = "Mon";
                break;
            case "الثلاثاء":
                returnday = "Tue";
                break;
            case "الأربعاء":
                returnday = "Wed";
                break;
            case "الخميس":
                returnday = "Thu";
                break;
            case "الجمعة":
                returnday = "Fri";
                break;

        }
        return returnday;
    }
    public static String Translatettime(String checktime) {

        String returntime = "";
        switch (checktime) {
            case "٠١":
                returntime = "01";
                break;
            case "٠٢":
                returntime = "02";
                break;
            case "٠٣":
                returntime = "03";
                break;
            case "٠٤":
                returntime = "04";
                break;
            case "٠٥":
                returntime = "05";
                break;
            case "٠٦":
                returntime = "06";
                break;
            case "٠٧":
                returntime = "07";
                break;
            case "٠٨":
                returntime = "08";
                break;
            case "٠٩":
                returntime = "09";
                break;
            case "١٠":
                returntime = "10";
                break;
            case "١١":
                returntime = "11";
                break;
            case "١٢":
                returntime = "12";
                break;
            case "١٣":
                returntime = "13";
                break;
            case "١٤":
                returntime = "14";
                break;
            case "١٥":
                returntime = "15";
                break;
            case "١٦":
                returntime = "16";
                break;
            case "١٧":
                returntime = "17";
                break;
            case "١٨":
                returntime = "18";
                break;
            case "١٩":
                returntime = "19";
                break;
            case "٢٠":
                returntime = "20";
                break;
            case "٢١":
                returntime = "21";
                break;
            case "٢٢":
                returntime = "22";
                break;
            case "٢٣":
                returntime = "23";
                break;
            case "٠٠":
                returntime = "00";
                break;


        }
        return returntime;
    }



    private void ConnectionToServersend() {
        String server_url = "http://192.168.1.4:8080/api/call_logs/paia2.php";

        StringRequest request = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String returnvalue = response.toString();
                        Log.e("response_example: ","returnerd value  "+returnvalue);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error: ", "Connection to server send error  " + String.valueOf(error));
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("json", String.valueOf(MyData));
                return params;

            }

        };
        MySingleton.getInstance(this).addToRequestQueue(request);
        Log.e("ConnectionToServerSend", "Connection to server seeend has finished!!!!!!!!!!!");
        //requestQueue.add(request);

    }


}