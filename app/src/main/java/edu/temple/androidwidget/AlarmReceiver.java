package edu.temple.androidwidget;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("Alarm status", "Triggered");
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Intent widgetIntent = new Intent(context, Widget.class);
        widgetIntent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        int appWidgetIds[] = appWidgetManager.getAppWidgetIds(new ComponentName(context.getPackageName(), Widget.class.getName()));
        widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.sendBroadcast(widgetIntent);
    }

}
