package com.example.android.paia;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.RemoteViews;

import java.util.ArrayList;
import java.util.HashMap;

public class papa extends AppWidgetProvider {

    private static Context appContext;
    static ArrayList<Integer> displayed= new ArrayList<>();
    static HashMap<Integer, Float> ratings = new HashMap<Integer, Float>();
    static HashMap<String, String> compareIDs = new HashMap<String, String>();
    static HashMap<Integer, Integer> phoneBook = new HashMap<Integer, Integer>();
    //static HashMap<String, String>  IDNAME = new HashMap<String, String>();
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        displayed = TemproryFuctions.displayed;
        ratings = TemproryFuctions.temp_rating;
        compareIDs=TemproryFuctions.compareIDs;
        appContext = context.getApplicationContext();
        String ID="";

        HashMap<Integer,Integer> swap = Rating.replace(displayed,ratings);

        CharSequence widgetText = "jkkj";
        CharSequence widgetDate= "jhkhjkhn";
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.paiawidget);
        views.setTextViewText(R.id.textClock, widgetText);
        views.setTextViewText(R.id.textData, widgetDate);
         // get the current id and contact name
         //IDNAME = returnID();
         //new display array

        for( String x : compareIDs.keySet()){
            int phone = Integer.parseInt(x);
            String key = compareIDs.get(x);
            int id = Integer.parseInt(key);
            phoneBook.put(id,phone);
        }

        for (int x=0; x<5; x++){
            if(swap.keySet().contains(displayed.get(x))){
                displayed.set(x,swap.get(displayed.get(x)));
            }
        }
        //button 1
        int phone1 = phoneBook.get(displayed.get(0));
        ID = returnID(String.valueOf(phone1));
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
                String.valueOf(ID));
        intent.setData(uri);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        String name1 =returnName(Integer.toString(phone1));
        views.setTextViewText(R.id.contact_1, name1);
        views.setOnClickPendingIntent(R.id.contact_1, pendingIntent);

        //button 2
        int phone2 = phoneBook.get(displayed.get(1));
        ID = returnID(String.valueOf(phone2));
        Intent intent2 = new Intent(Intent.ACTION_VIEW);
        Uri uri2 = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
                String.valueOf(ID));
        intent2.setData(uri2);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);
        views.setOnClickPendingIntent(R.id.contact_2, pendingIntent2);
        String name2 =returnName(String.valueOf(phone2));
        views.setTextViewText(R.id.contact_1, name2);

        //button 3
        int phone3 = phoneBook.get(displayed.get(2));
        ID = returnID(String.valueOf(phone3));
        Intent intent3 = new Intent(Intent.ACTION_VIEW);
        Uri uri3 = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
                String.valueOf(ID));
        intent3.setData(uri3);
        PendingIntent pendingIntent3 = PendingIntent.getActivity(context, 0, intent3, 0);
        String name3 =returnName(String.valueOf(phone3));
        views.setOnClickPendingIntent(R.id.contact_3, pendingIntent3);
        views.setTextViewText(R.id.contact_1, name3);

        //button 4
        int phone4 = phoneBook.get(displayed.get(3));
        ID = returnID(Integer.toString(phone4));
        Intent intent4 = new Intent(Intent.ACTION_VIEW);
        Uri uri4 = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
                String.valueOf(ID));
        intent4.setData(uri4);
        PendingIntent pendingIntent4 = PendingIntent.getActivity(context, 0, intent4, 0);
        views.setOnClickPendingIntent(R.id.contact_4, pendingIntent4);
        String name4 =returnName(String.valueOf(phone4));
        views.setTextViewText(R.id.contact_1, name4);

        //button 5
        int phone5 = phoneBook.get(displayed.get(4));
        ID = returnID(Integer.toString(phone5));
        Intent intent5 = new Intent(Intent.ACTION_VIEW);
        Uri uri5 = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI,
                String.valueOf(ID));
        intent5.setData(uri5);
        PendingIntent pendingIntent5 = PendingIntent.getActivity(context, 0, intent5, 0);
        String name5 =returnName(String.valueOf(phone5));
        views.setTextViewText(R.id.contact_1, name5);
        views.setOnClickPendingIntent(R.id.contact_5, pendingIntent5);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public void click(View view){

    }
    public static String returnID(String phonenumber) {
        String ID = "";
        //HashMap<String,String> IDNAME=new HashMap<>();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phonenumber));
        String[] columns = {ContactsContract.PhoneLookup.CONTACT_ID};
        Cursor cursor = appContext.getContentResolver().query(uri, columns, null, null, null);


        if (cursor != null) {
            while (cursor.moveToNext()) {

                ID = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.CONTACT_ID));
                //NAME = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                //IDNAME.put(ID,NAME);

            }

        } else {
            return null;
        }
        return ID;
    }

    public static String returnName(String phonenumber) {
        String NAME="";
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phonenumber));
        String[] columns = {ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cursor = appContext.getContentResolver().query(uri, columns, null, null, null);


        if (cursor != null) {
            while (cursor.moveToNext()) {

                //ID = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.CONTACT_ID));
                NAME = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                //IDNAME.put(ID,NAME);

            }

        } else {
            return null;
        }
        return NAME;
    }


}

