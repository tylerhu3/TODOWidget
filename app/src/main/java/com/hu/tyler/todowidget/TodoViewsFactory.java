package com.hu.tyler.todowidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by tyler on 8/10/2017.
 * This
 */

public class TodoViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private int STORAGE_PERMISSION_CODE = 23;
    public JSONObject obj = null;
    public JSONArray jsonArray = null;
    private Context ctxt = null;
    private int appWidgetId;
    public ArrayList<TodoItem> todoItems = null;



    public TodoViewsFactory(Context ctxt, Intent intent) {
        this.ctxt = ctxt;
        Toast.makeText(ctxt, "TodoViewsFactory Activated", Toast.LENGTH_SHORT).show(); //DXD
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        if (!openJson()) {
            //Log.d("XVX", "Loading from asset bc openJson failed");
            todoItems = loadJSONFromAsset();
        }
    }


    @Override
    public void onCreate() {
        Toast.makeText(ctxt, "onCreate Activated", Toast.LENGTH_SHORT).show(); //DXD
    //onCreate of Widget
    }

    @Override
    public void onDestroy() {
        Toast.makeText(ctxt, "onDestroy Activated", Toast.LENGTH_SHORT).show(); //DXD
        // no-op
    }

    @Override //Gets the size of the List
    public int getCount() {
        return (todoItems.size());
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews mView = new RemoteViews(ctxt.getPackageName(),
                R.layout.row);

        mView.setTextViewText(android.R.id.text1, todoItems.get(position).item);
        if (todoItems.get(position).strike == 1) {
            // This is completed item
            mView.setInt(android.R.id.text1, "setPaintFlags", Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); //add strike thru text
            mView.setImageViewResource(R.id.checkboxPicture, R.drawable.rowimagecomplete2); //set checkbox picture
            // mView.setTextColor(android.R.id.text1,(Color.parseColor("#000000"))); //change text color
        } else if (todoItems.get(position).strike == 0) {
          //  mView.setViewVisibility(R.id.importantview, View.INVISIBLE);
            ///mView.setImageViewResource(android.R.id.text1,(Color.parseColor("#FFFFFF")));
            //Indicated as a normal item
            mView.setTextColor(android.R.id.text1, (Color.parseColor("#000000")));
            mView.setImageViewResource(R.id.checkboxPicture, R.drawable.rowimageempty);
            mView.setInt(android.R.id.text1, "setPaintFlags", Paint.HINTING_OFF | Paint.ANTI_ALIAS_FLAG);
        } else if (todoItems.get(position).strike == 2) {
            //TODO : this needs to be redone later, as of now, this will never be encounter
            ///This is indicated as important on the widget
            //mView.setViewVisibility(R.id.importantview, View.VISIBLE);
            mView.setTextColor(android.R.id.text1, (Color.parseColor("#1B4F72")));
            mView.setImageViewResource(R.id.checkboxPicture, R.drawable.importantstamp);
            //mView.setImageViewResource(android.R.id.text1,R.drawable.importanttext);
            mView.setInt(android.R.id.text1, "setPaintFlags", Paint.HINTING_OFF | Paint.ANTI_ALIAS_FLAG);
        }

        Intent i = new Intent();
        Bundle extras = new Bundle();
        extras.putString(TodoWidget.EXTRA_WORD, todoItems.get(position).item);
        extras.putString("description", todoItems.get(position).description);
        extras.putInt("strikeText", todoItems.get(position).strike);
        extras.putInt("EXTRA_INT", position);
        i.putExtras(extras);
        mView.setOnClickFillInIntent(android.R.id.text1, i);
        mView.setOnClickFillInIntent(R.id.checkboxPicture, i);
        return (mView);
    }


    @Override
    public RemoteViews getLoadingView() {
        return (null);
    }

    @Override
    public int getViewTypeCount() {
        return (1);
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public boolean hasStableIds() {
        return (true);
    }

    //If any changes happen, onDataSetChanged() will execute and reread the list
    @Override
    public void onDataSetChanged() {
        Log.d("XVX", "onDataSetChanged ran.");
        try {
            openJson();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("XVX", "json todolist.json was not read successfully");
            //  Log.d("XVX", "Loading from Assets");
            todoItems = loadJSONFromAsset();

        }
    }


    public boolean openJson() {
        try {
            File myFile = new File(Environment.getExternalStorageDirectory().getPath() + "/todolist.json");
            if (!myFile.exists()) /// without this, error pops up
                return false;
            myFile.createNewFile();
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader Reader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow = "", aBuffer = "";

            while ((aDataRow = Reader.readLine()) != null)
                aBuffer += aDataRow;

            Reader.close();

            obj = new JSONObject(aBuffer);
            //The JSON array from the json file is called Questions, bad name its actually just the list of TODOs
            jsonArray = obj.getJSONArray("Questions");

            todoItems = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                TodoItem x = new TodoItem(o.getString("title"), o.getInt("strike"), o.getString("extra"), o.getString("description"));
                todoItems.add(x);
            }
        } catch (Exception e) {
            // Log.d("XVX", "TodoViewsFactory::openJson execution Exception:: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public ArrayList<TodoItem> loadJSONFromAsset() {

        ArrayList<TodoItem> todoItems = new ArrayList<>();
        String json = null;
        try {
            AssetManager assetManager = ctxt.getAssets();
            InputStream is = assetManager.open("todolist.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray jsonArray = obj.getJSONArray("Questions");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                TodoItem x = new TodoItem(o.getString("title"), o.getInt("strike"), o.getString("extra"), o.getString("description"));
                todoItems.add(x);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return todoItems;
    }

}
