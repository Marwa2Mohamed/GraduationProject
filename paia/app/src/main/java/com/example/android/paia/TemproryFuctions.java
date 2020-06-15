package com.example.android.paia;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class TemproryFuctions {

    public static HashMap<Integer, Float> general_rating = new HashMap<>();
    public static ArrayList<ArrayList> parsedData= new ArrayList<>();
    public  static  HashMap<String, String> compareIDs = new HashMap<String, String>();
    public static  ArrayList<Integer> displayed= new ArrayList<>();
    public static HashMap<Integer, Float> temp_rating = new HashMap<Integer, Float>();


    // set functions
    public void setgeneralratings(HashMap<Integer, Float> general_rating){
        this.general_rating=general_rating;
        Log.v("General rating: ", String.valueOf(this.general_rating.size()));
    }

    public void setParsedData(ArrayList<ArrayList> parsedData){
        this.parsedData=parsedData;
        Log.v("Temp Parse:", String.valueOf(this.parsedData.size()));
    }

    public void setCompareIDs(HashMap<String, String> compareIDs) { this.compareIDs = compareIDs;}
    public void setDisplayed(ArrayList<Integer> displayed) {
        this.displayed = displayed;
        Log.v("Display: ", String.valueOf(this.displayed.size()));
    }

    public void setTemp_rating(HashMap<Integer, Float> temp_rating) {
        this.temp_rating = temp_rating;
        Log.v("TempRating: ", String.valueOf(this.temp_rating.size()));

    }

    // get functions
    public HashMap<Integer, Float> getGeneral_rating(){
        return general_rating;
    }
    public ArrayList<ArrayList> getParsedData(){
        return parsedData;
    }
    public  HashMap<String, String> getcompareIDs(){return compareIDs;}
    public  ArrayList<Integer> getdisplayed(){return  displayed;}
}
