package com.example.android.paia;


public class DATA {

  private String name="";
  private String number="";
  private String day="";
  private String time="";
  private String date="";
  private String ID="";

  public DATA(String name,String number,String day,String time,String date,String ID){
    this.name=name;
    this.number=number;
    this.day=day;
    this.time=time;
    this.date=date;
    this.ID=ID;
  }

  public String getName(){
    return name;
  }

  public String getNumber(){
    return number;
  }

  public String getDay() {
    return day;
  }

  public String getTime() {
    return time;
  }

  public String getDate() {
    return date;
  }

  public String getID() {
    return ID;
  }
}