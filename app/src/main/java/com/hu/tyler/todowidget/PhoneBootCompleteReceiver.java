/**
 * 9/7/2017 - I notice this might not be necessary anymore but we can look at it as a alternative
 * for updating the app after restart, in future revisions, delete this and the code listed below
 * in the AndroidManifest.xml
 *
 This class runs whenever the phone has been rebooted via the AndroidManifest.xml:

 <receiver android:name=".PhoneBootCompleteReceiver"
 android:enabled="true"
 android:exported="false">
 <intent-filter>
 <action android:name="android.intent.action.BOOT_COMPLETED" />
 </intent-filter>
 </receiver>

 So basically the onReceive is called and the code inside is executed once again
 updating the WIDGET
 */
package com.hu.tyler.todowidget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by tyler on 8/15/2017.
 */

public class PhoneBootCompleteReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

            try {
               // Log.d("XVX","PhoneBootCompleteReceiver::onReceive execution");
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                int appWidgetIds[] = appWidgetManager.getAppWidgetIds(
                        new ComponentName(context, TodoWidget.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewTodo);
            } catch (Exception e) {
                e.printStackTrace();
            }

    }
}
