package com.example.android.paia;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;



//////////////////////////////////// 2 functions for calc rating table/////////////////////

public class Rating extends Application {

   //String json= "{\"records\":[{\"day\":\"1\",\"period\":\"9\",\"contact_1\":\"0.8\",\"contact_2\":\"0.3\",\"contact_3\":\"0.4\",\"contact_4\":\"0.1\",\"contact_5\":\"0.7\",\"contact_6\":\"0.2\",\"contact_7\":\"0.1\",\"contact_8\":\"0.2\",\"contact_9\":\"0.5\",\"contact_10\":\"0.6\"},{\"day\":\"1\",\"period\":\"10\",\"contact_1\":\"0.8\",\"contact_2\":\"0.5\",\"contact_3\":\"0.33\",\"contact_4\":\"0.1\",\"contact_5\":\"0.7\",\"contact_6\":\"0.6\",\"contact_7\":\"0.2\",\"contact_8\":\"0.4\",\"contact_9\":\"0.9\",\"contact_10\":\"0.1\"}]}";
    public JSONObject returnedobject;
    //RequestQueue requestQueue;
    String back;
//    {
//        try {
//            returnedobject = new JSONObject(json);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

    JSONArray cast;
    //public JSONObject getReturnedobject(){return returnedobject;}

    @RequiresApi(api = Build.VERSION_CODES.N)
    public  HashMap<Integer, Float> newRatings(ArrayList<Float> newRatings, HashMap<Integer, Float> general) {
        //calculates new ratings, returns new hashmap (general ratings)
        int index = -1;
        float rating=0;

        for (int id : general.keySet()) {
                index++;
                Log.v("Index: ", String.valueOf(index));
                rating= newRatings.get(index);
                Log.v("Ratings: ", String.valueOf(rating));
            if (rating > general.get(id)) {
                general.replace(id, rating);
            }
        }
        sortHashMapByValues(general); //Why?
        return general;
    }

    public static LinkedHashMap<Integer, Float> sortHashMapByValues(
            HashMap<Integer, Float> passedMap) {

        List<Integer> mapKeys = new ArrayList<>(passedMap.keySet());
        List<Float> mapValues = new ArrayList<>(passedMap.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);

        LinkedHashMap<Integer, Float> sortedMap
                = new LinkedHashMap<>();

        Iterator<Float> valueIt = mapValues.iterator();
        while (valueIt.hasNext()) {
            Float val = valueIt.next();
            Iterator<Integer> keyIt = mapKeys.iterator();

            while (keyIt.hasNext()) {
                Integer key = keyIt.next();
                Float comp1 = passedMap.get(key);
                Float comp2 = val;

                if (comp1.equals(comp2)) {
                    keyIt.remove();
                    sortedMap.put(key, val);
                    break;
                }
            }
        }
        return sortedMap;
    }


    //////////////////////////function used to update the widget/////////////////////////

    public static HashMap<Integer,Integer> replace(ArrayList<Integer> displayed, HashMap<Integer, Float> ratings) {
        ArrayList<Integer> remove = new ArrayList<>();
        ArrayList<Integer> add = new ArrayList<>();
        HashMap<Integer,Integer> replaceMap = new HashMap();
        int index = -1;
        for (int id : ratings.keySet()) {
            index++;
            if (index > 4) {
                break; //we need only the top 5
            }
            if (!displayed.contains(id)) {
                add.add(id);
            }
        }
        for (int t = 0; t < add.size(); t++) {
            int low = -1;
            for (int i : displayed) {
                if (low == -1) {
                    low = i;
                    continue;
                }
                if (ratings.get(i) < ratings.get(low) && !remove.contains(i)) {
                    low = i;
                }
            }
            remove.add(low);
        }
        for(int x=0; x<add.size(); x++){
            replaceMap.put(remove.get(x), add.get(x));
        }
        return replaceMap;
    }


    ////////////////////functions for connecting and parsing json/////////////////////////
    public void ConnectionToServer(final int Acc_id){


        String url = "http://192.168.1.4:8080/api/predict/read.php";
        StringRequest request= new StringRequest(Request.Method.POST,url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Responseeeee", "response "+response);
                back = response;
                Log.e("Backkkkkkkkkkkk", "back "+back);
                try {
                    returnedobject = new JSONObject(back);
                    Log.e("returnedobject", "returnedobjectttttttt "+returnedobject);
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


    public ArrayList<ArrayList> parseData() throws JSONException {

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

    public void setReturnedobject(JSONObject returnedobject){
        this.returnedobject=returnedobject;
        Log.v("Returned Object: ", String.valueOf(this.returnedobject));
    }


}
