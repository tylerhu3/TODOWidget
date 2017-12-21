package com.hu.tyler.todowidget;



/**
 * Created by tyler on 8/11/2017.
 * For each item on the list of the widget, it's properties are characterized by this class
 */
public class TodoItem {

    public String item;
    public int strike;
    public String extra;
    public String description;
    TodoItem()
    {
        //Default Constuctor
        //Log.d("XXX", "TodoItem Default Constructor Executed");
    }

    TodoItem(String item, int x,  String extra)
    {
        //Normal Constructor
        strike = x;
        this.item = item;
        this.extra = extra;
    }

    TodoItem(String y, int x,  String extra, String description)
    {
        //Normal Constructor #2
        strike = x;
        item = y;
        this.extra = extra;
        this.description = description;
    }

    //This marks the string of the actual item
    public void setItem(String item)
    {
        this.item = item;
    }

    //Should the item be cross out?
    //The below decides that, 0 = dont cross out, 1 = cross out.
    public void setStrike(int x)
    {
        strike = x;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }
}
