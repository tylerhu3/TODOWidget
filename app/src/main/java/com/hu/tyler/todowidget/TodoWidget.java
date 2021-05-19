package com.hu.tyler.todowidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class TodoWidget extends AppWidgetProvider {

    public static String EXTRA_WORD =
            "com.hu.tyler.todowidget.WORD";
    //Any DXD comment refers to the possible fix for disappearance of widget after launcher reset
//    private static final String TAG = TodoWidget.class.getSimpleName(); //DXD


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "On Received Activated", Toast.LENGTH_SHORT).show(); //DXD

        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        ComponentName widgetComponent = new ComponentName(context, TodoWidget.class);
        int[] appWidgetIds = widgetManager.getAppWidgetIds(widgetComponent);
        onUpdate(context, widgetManager, appWidgetIds);
        super.onReceive(context, intent);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Toast.makeText(context, "updateAppWidget activated", Toast.LENGTH_SHORT).show(); //DXD

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.todo_widget);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Toast.makeText(context, "onUpdate activated", Toast.LENGTH_SHORT).show(); //DXD
//        final ApplicationInfo info = context.getApplicationInfo(); //DXD
//        final String tag = info != null ? info.name : TAG; //DXD

        for (int widgetId : appWidgetIds) {
            RemoteViews mView = initViews(context, appWidgetManager, widgetId);

/////////////////////////// Handle Clicks
            Intent clickIntent = new Intent(context, ChangeTODO.class);
            PendingIntent clickPI = PendingIntent
                    .getActivity(context, 0,
                            clickIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            mView.setPendingIntentTemplate(R.id.words, clickPI);

            appWidgetManager.updateAppWidget(widgetId, mView);
        }


        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews initViews(Context context,
                                  AppWidgetManager widgetManager, int widgetId) {
        Toast.makeText(context, "initViews activated", Toast.LENGTH_SHORT).show(); //DXD
        RemoteViews mView = new RemoteViews(context.getPackageName(),
                R.layout.todo_widget);

        Intent intent = new Intent(context, TodoService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        //mView.setRemoteAdapter(R.id.words, new Intent(context, TodoService.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            //views.setRemoteAdapter(R.id.widget_list_view, serviceIntent);
            mView.setRemoteAdapter(R.id.words, new Intent(context, TodoService.class));// For Android Above 4.0
           // Log.d("XVX", "This version of android is ABOVE 4.0");
        } else {
            // views.setRemoteAdapter(widgetId, R.id.widget_list_view, serviceIntent);
            mView.setRemoteAdapter(widgetId, R.id.words, intent);  //For android BELOW 4.0
           // Log.d("XVX", "This version of android is BELOW 4.0");
        }
        return mView;
    }


    /*
    * Not actually sure if the code in onEnabled is required anymore because I made a separate class
    * called PhoneBootCompleteReciever to executed a update if the phone were to restart.
    * I will have to come back to this on a later day and figure this out. In the mean time, this is here
    * to simply update the widget when the phone reboots*/
    @Override
    public void onEnabled(Context context) {

        Toast.makeText(context, "onEnabled activated", Toast.LENGTH_SHORT).show(); //DXD
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        ComponentName widgetComponent = new ComponentName(context, TodoWidget.class);
        int[] appWidgetIds = widgetManager.getAppWidgetIds(widgetComponent);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, widgetManager, appWidgetId);  //This updates the widget after
                                                                    //restart.
        }
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Toast.makeText(context, "onRestored Activated", Toast.LENGTH_SHORT).show(); //DXD
    }
}

