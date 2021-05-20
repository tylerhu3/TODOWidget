package com.hu.tyler.todowidget;
/*
* This class is created when any item is clicked on the TODOList Widget
* As a reminder to myself, anytime a activity is opened, onCreate method
* is called and everything in there will be executed, as you can see the
* buttons are initialized here.*/
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class ChangeTODO extends Activity implements View.OnClickListener {

    ArrayList<TodoItem> locList = null;
    int positionToChange;
    String todoItem = null;
    SavedPreferences savedPreferences;
    final String TODO_TITLE = "TodoTitle";
    final String TODO_STRIKE_TYPE = "TodoStrikeType";

    Button ok_btn, cancel_btn, xbutton, upButton, downButton;
    EditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*The try and catch block simple makes sure that the main activity is close,
        * when I first develop this activity it was meant to be a activity with a transparent background
        * hence why I called this thing Transparency, bad naming convention on me but this was a simple first
        * time project I made and wasn't made to expand much further and thus I left it at that*/
        try {
            MainActivity.mainActivty01.finish(); /*closes the main activity other wise when this
            window launches the background will be the main activity*/
        } catch (Exception e) {
            e.printStackTrace();
        }

        savedPreferences = SavedPreferences.getInstance(getApplicationContext());
        locList = LoadDataFromSavedPrefs();
        //Connect .java file to .xml file
        setContentView(R.layout.activity_change_todo);

        setTitle("Change TODO"); // Set Title of Activity

        //Below connects the the buttons and textbox of this java to the xml file
        input = findViewById(R.id.editTODOInput);
        ok_btn = findViewById(R.id.ok_btn_id);
        cancel_btn = findViewById(R.id.cancel_btn_id);
        upButton = findViewById(R.id.upButton);
        downButton = findViewById(R.id.downButton);
        xbutton = findViewById(R.id.xOut);

        //for every one of the buttons, have them listen for a click;
        //the xml file has a property under each of those items to tell it what method to go to
        downButton.setOnClickListener(this);
        xbutton.setOnClickListener(this);
        ok_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
        upButton.setOnClickListener(this);

        //Below grabs the String that was typed into the editTextBox and the element number of that box
        todoItem = getIntent().getStringExtra(TodoWidget.EXTRA_WORD);
        positionToChange = getIntent().getIntExtra("EXTRA_INT", 0); //element number

        /*Dealing with the EditBox Box: I want to highlight everything then open the force open
         the soft keyboard. Also "android:windowSoftInputMode="stateVisible" ", must be added to
         this activity of the AndroidManifest.xml for soft keyboard to pop up.
        * */
        input.setText(todoItem);
        input.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    //This method handles all the clicks
    public void onClick(View v) {

        //Depending on what was clicked, this switch will guide what happens
        //outJson(), updateWidget(), and finish() is on every case except the CANCEL button, which
        //is why I didn't just put the three at the bottom of this method rather than having
        //it on every case besides CANCEL.
        switch (v.getId()) {

            case R.id.ok_btn_id: //OK BUTTON
                //showToastMessage("Ok Button Clicked; word : " + todoItem + " @ # " + positionToChange);
                locList.get(positionToChange).setText(input.getText().toString());
                outputToSavePrefs(positionToChange);
                updateWidget();
                finish();
                break;

            case R.id.xOut: //COMPLETED BUTTON
                //showToastMessage("Ok Button Clicked; word : " + todoItem + " @ # " + positionToChange);
                locList.get(positionToChange).setText(input.getText().toString());
                int x = locList.get(positionToChange).strike;
                if (x == 0 || x == 2) {
                    locList.get(positionToChange).setStrike(1);
                } else {
                    locList.get(positionToChange).setStrike(0);
                }
                outputToSavePrefs(positionToChange);
                updateWidget();
                finish();
                break;

            case R.id.cancel_btn_id: //CANCEL BUTTON
                // showToastMessage("Cancel Button Clicked");
                finish();
                break;
            case R.id.upButton: //ARROW UP BUTTON
                if(positionToChange == 0)
                {
                    showToastMessage("Highest Position");
                    break;
                }
                Collections.swap(locList,positionToChange, (positionToChange - 1));
                outputToSavePrefs(positionToChange);
                outputToSavePrefs(positionToChange - 1);
                positionToChange--;
                 // output the edits into the JSON text file
                updateWidget(); //update the widget
                break;
            case R.id.deleteButton: //DELETE BUTTON
                //showToastMessage("Delete button Clicked");
                locList.remove(positionToChange);
                TodoItem xx = new TodoItem("", 0);
                locList.add(xx);
                outputAllToSavedPref();
                updateWidget();
                finish();
                break;

            case R.id.downButton: // ARROW DOWN BUTTON

                if(positionToChange == locList.size()-1)
                {
                    showToastMessage("Lowest Position");
                    break;
                }
                Collections.swap(locList,positionToChange, (positionToChange + 1));
                outputToSavePrefs(positionToChange);
                outputToSavePrefs(positionToChange + 1);
                positionToChange++;
                updateWidget();
                break;
        }
    }

    public void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                new ComponentName(this, TodoWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewTodo);
    }

    void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
                .show();
    }

    /*Below is a basic override of the Back button, with this here, hitting the back button would
    * get you to the previous Transparency Activity open with the last thing you editted on display
    * with this the back button gets overrided and calls the goBackHome() method*/
    @Override
    public void onBackPressed() { //this functions overrides what happens when back button is pressed.
        goBackHome();
    }

    public void goBackHome()  /// go back to home screen
    {
//        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//        homeIntent.addCategory(Intent.CATEGORY_HOME);
//        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(homeIntent);
        finish();
    }

    /*When the class is first executed, it looks for a JSON file and if it doesn't exist, it takes the original json I have
    * put into this project and the method below simple reads that data and puts it into arraylist call "loclist"*/
    public ArrayList<TodoItem> LoadDataFromSavedPrefs() { ///if json file doesn't exist, read from apk.
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

    /*This method updates the json file held in the SD card, for the future it's probably a good idea
* to change the director to something else besides /sdcard; apparently not all phones have the sdcard
* directory as /sdcard, so using the Environment class would be a better idea
* Reminder: A JSON array must be put into a JSON object before it can be put into a external file*/
    void outputToSavePrefs(int i) { //outputs and overwrites current json file in sdcard
        savedPreferences.put(TODO_TITLE + i, locList.get(i).text);
        savedPreferences.put(TODO_STRIKE_TYPE + i, locList.get(i).strike);
    }

    void outputAllToSavedPref() {
        savedPreferences = SavedPreferences.getInstance(getApplicationContext());
        for (int i = 0; i < 88; i++) {
            savedPreferences.put(TODO_TITLE + i, locList.get(i).text);
            savedPreferences.put(TODO_STRIKE_TYPE + i, locList.get(i).strike);
        }
    }

}
