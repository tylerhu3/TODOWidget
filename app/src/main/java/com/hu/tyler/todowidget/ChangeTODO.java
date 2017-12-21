package com.hu.tyler.todowidget;
/*
* This class is created when any item is clicked on the TODOList Widget
* As a reminder to myself, anytime a activity is opened, onCreate method
* is called and everything in there will be executed, as you can see the
* buttons are initialized here.*/
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;

public class ChangeTODO extends Activity implements View.OnClickListener {
    public JSONArray jsonArray = null;
    public ArrayList<TodoItem> locList = null;
    public int positionToChange;
    public String todoItem = null;

    Button ok_btn, cancel_btn, xbutton, priorityButton, warpButton;
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

        Boolean fileExist = openJson();
        if (fileExist == false) {
            // Log.d("XVX", "json todolist was not read ");
            showToastMessage("JSON file was not read properly, App does not have permission, or file" +
                    "was deleted.");
            locList = loadJSONFromAsset();
        }
        //Connect .java file to .xml file
        setContentView(R.layout.activity_change_todo);

        setTitle("Change TODO"); // Set Title of Activity

        //Below connects the the buttons and textbox of this java to the xml file
        input = findViewById(R.id.editTODOInput);
        ok_btn = findViewById(R.id.ok_btn_id);
        cancel_btn = findViewById(R.id.cancel_btn_id);
        priorityButton = findViewById(R.id.priorityButton);
        warpButton = findViewById(R.id.bottomSide);
        xbutton = findViewById(R.id.xOut);

        //for every one of the buttons, have them listen for a click;
        //the xml file has a property under each of those items to tell it what method to go to
        warpButton.setOnClickListener(this);
        xbutton.setOnClickListener(this);
        ok_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
        priorityButton.setOnClickListener(this);

        //Below grabs the String that was typed into the editTextBox and the element number of that box
        todoItem = getIntent().getStringExtra(TodoWidget.EXTRA_WORD);
        positionToChange = getIntent().getIntExtra("EXTRA_INT", 0); //element number
        //int strike = getIntent().getIntExtra("strikeText", 0); //element number
        //String descriptionText = getIntent().getStringExtra("description");


        /*Dealing with the EditBox Box: I want to highlight everything then open the force open
         the soft keyboard. Also "android:windowSoftInputMode="stateVisible" ", must be added to
         this activity of the AndroidManifest.xml for soft keyboard to pop up.
        * */
        input.setText(todoItem);
        input.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        imm.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
    }

    public void swap(TodoItem x, TodoItem y)
    {
        TodoItem temp = x;
        x = y;
        y = temp;
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
                locList.get(positionToChange).setItem(input.getText().toString());
                outJson();
                updateWidget();
                finish();
                break;

            case R.id.xOut: //COMPLETED BUTTON
                //showToastMessage("Ok Button Clicked; word : " + todoItem + " @ # " + positionToChange);
                locList.get(positionToChange).setItem(input.getText().toString());
                int x = locList.get(positionToChange).strike;
                if (x == 0 || x == 2) {
                    locList.get(positionToChange).setStrike(1);
                } else {
                    locList.get(positionToChange).setStrike(0);
                }
                outJson();
                updateWidget();
                finish();
                break;

            case R.id.cancel_btn_id: //CANCEL BUTTON
                // showToastMessage("Cancel Button Clicked");
                finish();
                break;
            case R.id.priorityButton: //ARROW UP BUTTON
                TodoItem xx2; // create a new item call xx2; then put that item on the very top
                // of the list
                //Decides what happen based on what was the previous strike value of the item
//                if (locList.get(positionToChange).strike == 0) {
//                    xx2 = new TodoItem(input.getText().toString(), 2, "", "");
//                    //showToastMessage("strike == 0");
//                    locList.remove(positionToChange); // this aint happening
//                    locList.add(0, xx2);
//                } else if (locList.get(positionToChange).strike == 1) {
//                    //showToastMessage("strike == 1");
//                    xx2 = new TodoItem(input.getText().toString(), 2, "", "");
//                    locList.remove(positionToChange); // this aint happening
//                    locList.add(0, xx2);
//                } else if (locList.get(positionToChange).strike == 2) {
//                    //showToastMessage("strike == 2");
//                    //xx2 = new TodoItem(input.getText().toString(), 0, "", "");
//                    locList.get(positionToChange).setStrike(0);
//                }

                if(positionToChange == 0)
                {
                    showToastMessage("Lowest Position");
                    break;
                }
                Collections.swap(locList,positionToChange, (positionToChange - 1));
                positionToChange--;
                outJson(); // output the edits into the JSON text file
                updateWidget(); //update the widget
//                finish(); // Close the activity
                break;
            case R.id.deleteButton: //DELETE BUTTON
                //showToastMessage("Delete button Clicked");
                locList.remove(positionToChange);
                TodoItem xx = new TodoItem("", 0, "", "");
                locList.add(xx);
                outJson();
                updateWidget();
                finish();
                break;

            case R.id.bottomSide: // ARROW DOWN BUTTON
                //showToastMessage("Warp button Clicked");
                //This case takes a selected item and moves it to the bottom of the list
                //TodoItem newItem = new TodoItem(locList.get(positionToChange).item, locList.get(positionToChange).strike, "", "");
                //locList.add(newItem);
//                for (int i = locList.size() - 1; i >= 0; i--) {
//                    if (locList.get(i).item.equals("")) {
//                        //Log.d("XLoop", "Position: " + i + " is free");
//                        locList.get(i).item = locList.get(positionToChange).item;
//                        locList.get(i).strike = locList.get(positionToChange).strike;
//                        break;
//
//                    }
//                }
//                locList.remove(positionToChange);
                if(positionToChange == locList.size()-1)
                {
                    showToastMessage("Highest Position");
                    break;
                }
                Collections.swap(locList,positionToChange, (positionToChange + 1));
                positionToChange++;
                outJson();
                updateWidget();
//                finish();
                break;
        }
    }

    public void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                new ComponentName(this, TodoWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.words);
    }

    void showToastMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG)
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
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
        finish();
    }

    /*When the class is first executed, it looks for a JSON file and if it doesn't exist, it takes the original json I have
    * put into this project and the method below simple reads that data and puts it into arraylist call "loclist"*/
    public ArrayList<TodoItem> loadJSONFromAsset() { ///if json file doesn't exist, read from apk.

        //  Log.d("XVX", "Transparency::loadJSONFromAsset execution");
        ArrayList<TodoItem> locList = new ArrayList<>();
        String json = null;
        try {
            AssetManager assetManager = this.getAssets();
            InputStream is = assetManager.open("todolist.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            //return null;
        }
        try {
            JSONObject obj = new JSONObject(json);
            JSONArray jsonArray = obj.getJSONArray("Questions");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                TodoItem x = new TodoItem(o.getString("title"), o.getInt("strike"), o.getString("extra"), o.getString("description"));

                locList.add(x);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return locList;
    }

    /*This method updates the json file held in the SD card, for the future it's probably a good idea
* to change the director to something else besides /sdcard; apparently not all phones have the sdcard
* directory as /sdcard, so using the Environment class would be a better idea
* Reminder: A JSON array must be put into a JSON object before it can be put into a external file*/
    public void outJson() { //outputs and overwrites current json file in sdcard
        //Log.d("XVX", "outJson execution");
        JSONArray mJSONArray = new JSONArray();
        JSONObject toOutFile = new JSONObject();
        for (int i = 0; i < locList.size(); i++) {
            try {
                JSONObject xxx = new JSONObject();
                xxx.put("title", locList.get(i).item);
                xxx.put("strike", locList.get(i).strike);
                xxx.put("extra", locList.get(i).extra);
                xxx.put("description", locList.get(i).description);
                mJSONArray.put(xxx);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        try {
            toOutFile.put("Questions", mJSONArray); // this is where json array is put into jsonObject
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            Writer output = null;
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/todolist.json");
            output = new BufferedWriter(new FileWriter(file));
            output.write(toOutFile.toString());
            output.close();
//            Toast.makeText(getApplicationContext(), "Composition saved", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            //   Toast.makeText(getBaseContext(), "Widget requires permission!", Toast.LENGTH_SHORT).show();
        }

    }

    /*Not much to say, just opens the todolist.json file and then grabs all the text from it
    * and puts it in the obj jsonobject made a static from up atop, I believe it doesn't need to be
    * a static, should change in future revisions but will leave it for now.*/
    public boolean openJson() { //opens the json file if there exists one in SD card
        try {
            // Log.d("XVX", "openJson execution");
            File myFile = new File(Environment.getExternalStorageDirectory().getPath() + "/todolist.json");
            //Log.d("XVX", "did myFile open successfully :" + myFile.exists());
            if (!myFile.exists())
                return false;
            myFile.createNewFile();
            FileInputStream fIn = new FileInputStream(myFile);
            BufferedReader Reader = new BufferedReader(new InputStreamReader(fIn));
            String aDataRow = "", aBuffer = "";

            while ((aDataRow = Reader.readLine()) != null)
                aBuffer += aDataRow;

            Reader.close();

            JSONObject obj = new JSONObject(aBuffer);
            jsonArray = obj.getJSONArray("Questions");

            locList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject o = jsonArray.getJSONObject(i);
                TodoItem x = new TodoItem(o.getString("title"), o.getInt("strike"), o.getString("extra"), o.getString("description"));
                locList.add(x);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

}
