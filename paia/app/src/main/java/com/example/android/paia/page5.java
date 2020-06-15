package com.example.android.paia;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class page5 extends AppCompatActivity {
    private TextView text,pleasewait;
    private String Jsondate = "";//e.g{server_response:[{SEND_updated_date:2019-04-11}]}
    private Button next;
    private Button back;
    private ProgressBar prograss;

    HashMap<String,String> compareIDs= new HashMap<>();
    ArrayList<DATA> ContactsData = new ArrayList<>();
    ArrayList<FilteredData> filteredDataArrayList = new ArrayList<>();
    JSONObject MyData = new JSONObject();
    private String[] date;

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page5);
        prograss = (ProgressBar) findViewById(R.id.ProgressBar);
        text = (TextView) findViewById(R.id.message);
        pleasewait=(TextView) findViewById(R.id.pleasewait);
        next = (Button) findViewById(R.id.next);
        back = (Button) findViewById(R.id.back);
        Jsondate = getIntent().getExtras().getString("date");
        Log.v("Json:",Jsondate);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pageIntent = new Intent(page5.this, LastPageActivity.class);
                pageIntent.putExtra("hashmap", compareIDs);
                pageIntent.putExtra("Acc_id",date[1]);
                startActivity(pageIntent);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pageIntent = new Intent(page5.this, page4.class);
                pageIntent.putExtra("page5", "true");
                startActivity(pageIntent);
            }
        });
        ////////////////////////////////////////////////////////////////////////////////////////////////

        date = JsonParseToString(Jsondate);

        Log.v("date: ",date[0]);
        Log.v("AccountId: ",date[1]);

      if ( date[0] !="") {
            // gmail found in database or new account
            // send data to server
            CollectCallLogsData();
            filteredData(date[0]);

            Log.v("Filtered data size: ", String.valueOf(filteredDataArrayList.size()));
            print();
            if (filteredDataArrayList.size() != 0) { //if filteredDataArrayList.size() == 0 means that was last installed yesterday
                FilterDataToJson();
                Log.v("JsonStructure: ", String.valueOf(MyData));
            }

          ConnectionToServer();
          new PrograssThread().execute();
        }


        else if(date[0]=="")

        {
            //the user pressed back to page 4 then the user got here again;
            next.setVisibility(View.VISIBLE);
            text.setText("Data is collected press next ");
            prograss.setProgress(100);
            pleasewait.setVisibility(View.INVISIBLE);


        }

    }

    private void print() {
        for(int size=0;size<filteredDataArrayList.size();size++){

            FilteredData fobj=filteredDataArrayList.get(size);
            Log.v("number",fobj.getNumber());
            Log.v("ID",fobj.getID());

        }

    }

    private void FilterDataToJson() {
        try {

            JSONArray filteredarray= new JSONArray();
            JSONObject temp=new JSONObject();
            temp.put("Acc_id",Integer.parseInt(date[1]));
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
    private void filteredData(String senddate) {

        //Variables &objects declerations

        Log.v("Senddate: ",senddate);

        Date today = new Date();// e.g Thu April 18 16:47:57 EET 2019
        DateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd");
        String ADay = senddate , calldate = "";// sent date from the server
        long diff = 0, diffcontact = 0;
        int diffDays = 0, diffDayscontact = 0;

        // Difference between today's date and the sent date from the server

        try {
            Date ADayobj = dateFormat.parse(ADay);//e.g Wed April 14 00:00:00 EET 2019
            diff = today.getTime() - ADayobj.getTime();
            diffDays = (int) (diff / (24 * 60 * 60 * 1000));// e.g 1,2,3...7,8 etc day(s)

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (diffDays > 7) {

            diffDays = 7;

        }

        //Filtering phase according to names and dates
        if (diffDays > 0 && diffDays <= 7) {


            for (int index = 0; index < ContactsData.size(); index++) {
                DATA dataobj = ContactsData.get(index);
                calldate = dataobj.getDate();
                try {
                    Date calldateobj = dateFormat.parse(calldate);
                    diffcontact = today.getTime() - calldateobj.getTime();// days difference for each call
                    diffDayscontact = (int) (diffcontact / (24 * 60 * 60 * 1000));// days difference for each call
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (dataobj.getName() != null && diffDayscontact <= diffDays && diffDayscontact != 0) {

                    filteredDataArrayList.add(new FilteredData(dataobj.getNumber(), dataobj.getDay(), dataobj.getTime(), dataobj.getID()));
                    compareIDs.put(dataobj.getNumber(),dataobj.getID());
                }

            }
        } else if (diffDays==0) {
            // diffDays==1 means that the database last updated was yesterday so no data needed to be collected
        }


    }




    private String [] JsonParseToString(String jsondate) {
        String date="";
        String AccountId="";
        String[] data = new String[2];
        try {
            JSONObject myjason= new JSONObject(jsondate);
            AccountId = myjason.getString("Acc_id");
            date = myjason.getString("last_updated");
            data[0] = date;
            data[1] = AccountId;

//            JSONArray server_response=myjason.getJSONArray("server_response");
//            JSONObject send_up_dated_date=server_response.getJSONObject(0);
//            date=send_up_dated_date.getString("SEND_updated_date");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

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
    private class PrograssThread extends AsyncTask<Void, Integer,String>


    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prograss.setMax(100);

        }

        @Override
        protected String doInBackground(Void... voids) {
            for(int i=0;i<100;i++){
                publishProgress(i);
                try{
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            prograss.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            next.setVisibility(View.VISIBLE);
            text.setText("Data is collected press next ");
            pleasewait.setVisibility(View.INVISIBLE);

        }
    }
        private void ConnectionToServer() {
        String server_url = "http://192.168.1.4:8080/api/call_logs/paia2.php";
        RequestQueue queue = Volley.newRequestQueue(page5.this);

            StringRequest request = new StringRequest(Request.Method.POST, server_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                         String returnvalue = response.toString();
                         Log.v("response_page5: ",returnvalue);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(page5.this, "Error!! Server not responding...", Toast.LENGTH_LONG).show();
                    Log.e("error: ", String.valueOf(error));

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("json", String.valueOf(MyData));
                    return params;

                }

            };
            queue.add(request);

        }




}


//        else if(date == "0-0-0" && date !=""){
//          // No gmail found in database or the updated_date in the database is today or yesterday
//          //here we send something to server
//
//          LocalDate localdate = LocalDate.now().minusDays(7);
//          String strdate= localdate.toString();
//          Toast.makeText(this, "strdate: "+strdate,Toast.LENGTH_LONG).show();
//          CollectCallLogsData();
//          filteredData(strdate);
//          Log.v("filtered size: ", String.valueOf(filteredDataArrayList.size()));
//          FilterDataToJson();
//          Log.v("JsonStructure2: ", String.valueOf(MyData));
//          new PrograssThread().execute();
//       }





