package com.example.android.randomrhyme;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;

public class PoemWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPrefs = context.getSharedPreferences("poem_to_widget", 0);
        CharSequence poemText = sharedPrefs.getString("poem_text", "It is empty! Add a poem to populate.");
        CharSequence author = sharedPrefs.getString("author", " ");
        CharSequence title = sharedPrefs.getString("title", " ");

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.poem_widget);
        views.setTextViewText(R.id.widget_poem, poemText);
        views.setTextViewText(R.id.widget_author, author);
        views.setTextViewText(R.id.widget_title, title);

        Intent intent = new Intent(context, FavoritePoemsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.widget_linearLayout, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

}
