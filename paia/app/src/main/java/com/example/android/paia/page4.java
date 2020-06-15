package com.example.android.paia;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.ContactsContract;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.ReferenceQueue;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class page4 extends AppCompatActivity {

    //if server response not working : slow internet or xampp is closed or didn't change the ip address
    private String gmail = "",page5="";
    private Button next;
    private Button back;
    private ProgressBar prograss;
    private TextView text;
    private TextView pleasewait;
    private String returnvalue ="";

    RequestQueue requestQueue;
    Handler mHandler = new Handler();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.page4);

        prograss = (ProgressBar) findViewById(R.id.ProgressBar);
        text = (TextView) findViewById(R.id.message);
        pleasewait=(TextView) findViewById(R.id.pleasewait);
        gmail = getIntent().getExtras().getString("Gmail");
        page5=getIntent().getExtras().getString("page5");

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // the code for next and back buttons' actions (for now next is invisble to the user)
        next = (Button) findViewById(R.id.next);
        back = (Button) findViewById(R.id.back);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pageIntent = new Intent(page4.this, page5.class);
                pageIntent.putExtra("date",returnvalue);
                startActivity(pageIntent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pageIntent = new Intent(page4.this, PermissionActivity.class);
                startActivity(pageIntent);
            }
        });
        if(gmail !=null) {
            requestQueue = Volley.newRequestQueue(this);

            final ConnectionToServer2 conn_2=new ConnectionToServer2();
            conn_2.execute();


            mHandler.postDelayed(new Runnable(){
                    @Override
                    public void run() {

                        ConnectionToServer();

                    }
                },180000);

        } else if(gmail==null ){
            next.setVisibility(View.VISIBLE);
            text.setText("Connected to Server Press next");
            prograss.setProgress(100);
            pleasewait.setVisibility(View.INVISIBLE);
        }else if(page5=="true"){
            next.setVisibility(View.VISIBLE);
            text.setText("Connected to Server Press next");
            prograss.setProgress(100);
            pleasewait.setVisibility(View.INVISIBLE);
        }



    }

    private void ConnectionToServer() {
        String server_url = "http://192.168.1.4:8080/api/account/paia.php";//PAIA/gmail.php
        StringRequest request = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        returnvalue = response.toString();
                        Log.v("response: ",returnvalue);
                        //Toast.makeText(page4.this,"Finish",Toast.LENGTH_SHORT).show();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(page4.this, "Error!! Server not responding...", Toast.LENGTH_LONG).show();
                Log.e("error: ", String.valueOf(error));

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gmail", gmail);
                return params;

            }

        };
        requestQueue.add(request);

    }

    private class ConnectionToServer2 extends AsyncTask<Void, Integer,String>


    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prograss.setMax(100);
            Toast.makeText(page4.this,"Note: This Process will take a minute or two maximum to be finished"+"\n"+"Please Donot close your Program",Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            for(int i=0;i<100;i++){
                publishProgress(i);
                try{
                    Thread.sleep(2000);
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
            text.setText("Connected to Server :D"+"\n"+"Press next");
            pleasewait.setVisibility(View.INVISIBLE);

        }
    }

}