package com.example.android.paia;

public class FilteredData {

      private   String number="";
      private   String day="";
      private   String time="";
      private   String ID="";

    public FilteredData(String number,String day, String time,String ID){
       // this.name=name;
        this.number=number;
        this.day=day;
        this.time=time;
        this.ID=ID;


    }
//    public String getName(){
//        return name;
//    }

    public String getNumber(){
        return number;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public String getID() {
        return ID;
    }




}
