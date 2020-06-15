package com.example.android.paia;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.text.DecimalFormat;


public class LastPageActivity extends AppCompatActivity {

      ArrayList<DATA> ContactsData = new ArrayList<>();
      ArrayList<FilteredData> filteredDataArrayList = new ArrayList<>();
      boolean contact_is_found_in_hash = false;
      HashMap<String, String> compareIDs = new HashMap<String, String>();
      HashMap<String, String> temprory = new HashMap<String, String>();
      HashMap<Integer, Float> general_rating = new HashMap<>();
      public static DecimalFormat df2 = new DecimalFormat("#.##");
      ArrayList<ArrayList> parsedData= new ArrayList<>();
      TemproryFuctions temproryFuctions= new TemproryFuctions();
      HashMap<Integer, Integer> frequency = new HashMap<>();
      String Acc_id;
      Handler mHandler = new Handler();
      Rating ratingobj = new Rating();
      int sum = 0,size=0;
      public JSONObject returnedobject;
      RequestQueue requestQueue;
      JSONArray cast;
      private static final String TAG = "MainActivit";

      String back;

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_page);

        temprory = (HashMap<String, String>) getIntent().getExtras().get("hashmap");
        Acc_id = (String) getIntent().getExtras().get("Acc_id");
        Log.v("Acc_id", String.valueOf(Acc_id));
        Log.v("Temprory Size", String.valueOf(temprory.size()));
        Button finish = (Button) findViewById(R.id.finish);

        //Button back = (Button) findViewById(R.id.back);


        // if the user didn't press back
        if (temprory.size() != 0) {
            compareIDs = temprory;
        }
        Log.v("CompareIDs", String.valueOf(compareIDs.size()));


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
            }
        });

        CollectCallLogsData();
        filteredData();

        //set variables to begin calculating general ratings

        int current_id = 0;
        int count = 0;
        //count frequency per contact
        Log.v("Filtered Size:", String.valueOf(filteredDataArrayList.size()));
        for(FilteredData x: filteredDataArrayList){

                Log.v("x:", String.valueOf(x));
                current_id = Integer.parseInt(x.getID());
                Log.v("ci:", String.valueOf(current_id));
                if (frequency.containsKey(current_id)){

                    count = frequency.get(current_id);
                    Log.v("count:", String.valueOf(count));
                    frequency.replace(current_id, ++count);

                }else{
                    frequency.put(current_id, 1);
                    Log.v("current_id:", String.valueOf(current_id));
                }

        }
       // calculate mean frequency
       if(frequency.size()!=0) {
           for (int x : frequency.keySet()) {

               sum += frequency.get(x);
               //size++;
           }
           Log.v("Sum: ", String.valueOf(sum));
           Log.v("Frequency size: ", String.valueOf(frequency.size()));
           //Log.v("Size:", String.valueOf(size));

           float mean = sum / frequency.size();
           Log.v("Mean: ", String.valueOf(mean));
           //calculate general ratings
        double rating = 0;
        for( int x: frequency.keySet()){
            rating = frequency.get(x)/mean;
            rating = 1/( 1 + Math.pow(Math.E,(-1*rating)));
            rating = Double.parseDouble(df2.format(rating));
            general_rating.put(x, (float)rating);
        }
        TemproryFuctions.general_rating=general_rating;

        Log.v("General rating class:", String.valueOf(general_rating.size()));
        Log.v("General rating Temp", String.valueOf(TemproryFuctions.general_rating.size()));
        TemproryFuctions.compareIDs=compareIDs;

       }

//       //have an error here
        requestQueue = Volley.newRequestQueue(this);
                    mHandler.postDelayed(new Runnable(){
                @Override
                public void run() {


                    ConnectionToServer(Integer.parseInt(Acc_id));
                    Log.e("Conncetion success","connection  successssssssssssss");

//                    try {
//                        parsedData = parseData(returnedobject);
//                        Log.e("Parsssed Data: ", String.valueOf(parsedData.size()));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    TemproryFuctions.parsedData=parsedData;
//                    Log.e("parsedData size:", String.valueOf(parsedData.size()));

                }
            },180000);
        //////////

//        Log.v("parsedData class:", String.valueOf(parsedData.size()));
//        Log.v("parsedData Temp:", String.valueOf(TemproryFuctions.parsedData.size()));
//        ComponentName componentName= new ComponentName(this,ExampleJobService.class);
//        JobInfo info = new JobInfo.Builder(123,componentName)
//                .setPersisted(true)
//                .setPeriodic(15 * 60 * 1000)
//                .build();
//
//        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
//        int resultcode = scheduler.schedule(info);
//
//        if(resultcode == JobScheduler.RESULT_SUCCESS){
//            Log.e(TAG,"JOB SCHEDULED ");
//        }else{
//            Log.e(TAG,"JOB SCHEDULED FAILED");
//        }
//
//
//        // 12 Am Part
//        Intent serviceIntent = new Intent(this, ExampleService.class);
//        serviceIntent.putExtra("Acc_id",Acc_id);
//        serviceIntent.putExtra("compareid",compareIDs);
//        serviceIntent.putExtra("generalratings",general_rating);
//        serviceIntent.putExtra("frequency",frequency);
//        ContextCompat.startForegroundService(this, serviceIntent);




    }

    public void ConnectionToServer(final int Acc_id){


        String url = "http://192.168.1.4:8080/api/predict/read.php";
        StringRequest request= new StringRequest(Request.Method.POST,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Responsee22222222222", "response "+response);
                back = response;
                Log.e("Backkkkkkkkkkkk", "back "+back);
                try {
                    returnedobject = new JSONObject(back);
                    Log.e("returnedobject", "returnedobjectttttttt2 "+returnedobject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("ConnectionToServer", "Connection to server has finished!!!!!!!!!!!");

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Display error message whenever an error occurs
                Log.e("Error!!","Connection To server error  "+error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("Acc_id", String.valueOf(Acc_id));
                return param;
            }
        };

        MySingleton.getInstance(this).addToRequestQueue(request);

        //requestQueue.add(request);
    }

    public ArrayList<ArrayList> parseData(JSONObject returnedobject) throws JSONException {

        Log.v("returnedobject", String.valueOf(returnedobject));
        Log.e("ParseData function", "I'm in parse data function");
        ArrayList<ArrayList> table = new ArrayList();
        JSONObject temp = new JSONObject();
        JSONObject json = returnedobject;
        try {
            cast = json.getJSONArray("records");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int x=0; x<cast.length(); x++ ){
            temp = cast.getJSONObject(x);
            JSONArray names = temp.names();
            ArrayList<Float> row = new ArrayList();
            for(int y=0; y<names.length(); y++ ){
                row.add( Float.parseFloat( temp.getString(names.getString(y))) );
            }
            table.add(row);
        }
        return table;
    }

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
                    diffcontact = today.getTime() - calldateobj.getTime();// days difference for each call
                    diffDayscontact = (int) (diffcontact / (24 * 60 * 60 * 1000));// days difference for each call

                    if (dataobj.getName() != null && diffDayscontact == 0) {
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
                    if (dataobj.getName() != null && diffDayscontact == 0) {

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

}