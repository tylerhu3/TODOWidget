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
//import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class TodoWidget extends AppWidgetProvider {

    public static String EXTRA_WORD =
            "com.hu.tyler.todowidget.WORD";


    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "On Received Executed", //Toast.LENGTH_SHORT).show(); //DXD

        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        ComponentName widgetComponent = new ComponentName(context, TodoWidget.class);
        int[] appWidgetIds = widgetManager.getAppWidgetIds(widgetComponent);
        onUpdate(context, widgetManager, appWidgetIds);
    }

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        //Toast.makeText(context, "updateAppWidget Executed", //Toast.LENGTH_SHORT).show(); //DXD

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.todo_widget);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        //Toast.makeText(context, "onUpdate Executed", //Toast.LENGTH_SHORT).show(); //DXD
        for (int widgetId : appWidgetIds) {
            RemoteViews mView = initViews(context, appWidgetManager, widgetId);
            Intent clickIntent = new Intent(context, ChangeTODO.class);
            PendingIntent clickPI = PendingIntent
                    .getActivity(context, 0,
                            clickIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            mView.setPendingIntentTemplate(R.id.listViewTodo, clickPI);
            appWidgetManager.updateAppWidget(widgetId, mView);
        }

    }

    private RemoteViews initViews(Context context,
                                  AppWidgetManager widgetManager, int widgetId) {
        //Toast.makeText(context, "initViews Executed", //Toast.LENGTH_SHORT).show(); //DXD
        RemoteViews mView = new RemoteViews(context.getPackageName(),
                R.layout.todo_widget);

        Intent intent = new Intent(context, TodoService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);

        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        //mView.setRemoteAdapter(R.id.words, new Intent(context, TodoService.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            //views.setRemoteAdapter(R.id.widget_list_view, serviceIntent);
            mView.setRemoteAdapter(R.id.listViewTodo, intent);// For Android Above 4.0
           // Log.d("XVX", "This version of android is ABOVE 4.0");
        } else {
            // views.setRemoteAdapter(widgetId, R.id.widget_list_view, serviceIntent);
            mView.setRemoteAdapter(widgetId, R.id.listViewTodo, intent);  //For android BELOW 4.0
           // Log.d("XVX", "This version of android is BELOW 4.0");
        }
        return mView;
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        //Toast.makeText(context, "onDisabled Executed", //Toast.LENGTH_SHORT).show(); //DXD
    }
}

