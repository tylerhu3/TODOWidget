package com.hu.tyler.todowidget;



/**
 * Created by tyler on 8/11/2017.
 * For each item on the list of the widget, it's properties are characterized by this class
 */
public class TodoItem {


    public String text;
    public int strike; //0 means no strike, 1 means strike thru text, 2 means high priority


    TodoItem(String y, int x)
    {
        //Normal Constructor #2
        strike = x;
        text = y;
    }

    //This marks the string of the actual item
    public void setText(String text)
    {
        this.text = text;
    }

    //Should the item be cross out?
    //The below decides that, 0 = dont cross out, 1 = cross out.
    public void setStrike(int x)
    {
        strike = x;
    }

}
