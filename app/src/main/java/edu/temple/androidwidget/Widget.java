package edu.temple.androidwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by karl on 12/3/15.
 */
public class Widget extends AppWidgetProvider {

    AppWidgetManager appWidgetManager;
    int[] appWidgetIds;
    Context context;

    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        this.context = context;
        this.appWidgetManager = appWidgetManager;
        this.appWidgetIds = appWidgetIds;

        Thread t = new Thread() {

            public void run() {

                URL stockQuoteUrl;

                try

                {

                    stockQuoteUrl = new URL("http://finance.yahoo.com/webservice/v1/symbols/GOOG/quote?format=json");

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    stockQuoteUrl.openStream()));

                    String response = "", tmpResponse;

                    tmpResponse = reader.readLine();
                    while (tmpResponse != null) {
                        response = response + tmpResponse;
                        tmpResponse = reader.readLine();
                    }

                    JSONObject stockObject = new JSONObject(response);
                    Message msg = Message.obtain();
                    msg.obj = stockObject;

                    stockResponseHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();



    }


    Handler stockResponseHandler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {

            String stockPrice = "GOOG: ";
            JSONObject stockObject = (JSONObject) msg.obj;

            try {
                stockPrice += stockObject.getJSONObject("list")
                        .getJSONArray("resources")
                        .getJSONObject(0)
                        .getJSONObject("resource")
                        .getJSONObject("fields")
                        .getString("price");
            } catch (Exception e) {
                e.printStackTrace();
            }

            updateViews(stockPrice);

            return false;
        }
    });


    /** Update each widget with the new stock price */
    private void updateViews(String stockPrice){
        Log.i("Stock price", stockPrice);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.stock_quote_layout);
        remoteViews.setTextViewText(R.id.stockQuoteTextView, stockPrice);
        for (int i = 0; i < appWidgetIds.length; i++) {
            final int appWidgetId = appWidgetIds[i];
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
    }
}
