package com.hu.tyler.todowidget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
//import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by tyler on 8/10/2017.
 * This
 */

public class TodoViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static Context ctxt;
    public ArrayList<TodoItem> todoItems = null;
    SavedPreferences savedPreferences;
    final String TODO_TITLE = "TodoTitle";
    final String TODO_STRIKE_TYPE = "TodoStrikeType";


    public TodoViewsFactory(Context ctxt, Intent intent) {
        this.ctxt = ctxt;
        //Toast.makeText(ctxt, "TodoViewsFactory Activated", //Toast.LENGTH_SHORT).show(); //DXD

    }

    @Override
    public void onCreate() {
        //Toast.makeText(ctxt, "onCreate Activated", //Toast.LENGTH_SHORT).show(); //DXD
        todoItems = LoadDataFromSavedPrefs();

    }

    @Override
    public void onDestroy() {
        //Toast.makeText(ctxt, "onDestroy Activated", //Toast.LENGTH_SHORT).show(); //DXD
    }

    @Override //Gets the size of the List
    public int getCount() {
        return (88);
    }

    @Override
    public RemoteViews getViewAt(int position) {


        //This sets the row type
        RemoteViews mView = new RemoteViews(ctxt.getPackageName(),
                R.layout.row);


        mView.setTextViewText(android.R.id.text1, todoItems.get(position).text);

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
        extras.putString(TodoWidget.EXTRA_WORD, todoItems.get(position).text);
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
        todoItems = LoadDataFromSavedPrefs();
        Log.d("Tyler", "DataSetChanged");
    }

    ArrayList<TodoItem> LoadDataFromSavedPrefs() {

        savedPreferences = SavedPreferences.getInstance(ctxt);
        ArrayList<TodoItem> todoItems = new ArrayList<>();
            for (int i = 0; i < 88; i++) {
                TodoItem x = new TodoItem(
                        savedPreferences.get(TODO_TITLE + i, ""),
                        savedPreferences.get(TODO_STRIKE_TYPE + i, 0)
                );
                todoItems.add(x);
            }
        return todoItems;

    }

}
