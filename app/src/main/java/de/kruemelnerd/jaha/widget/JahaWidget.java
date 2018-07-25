package de.kruemelnerd.jaha.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import de.kruemelnerd.jaha.R;
import de.kruemelnerd.jaha.viewModel.PaymentViewModel;

/**
 * Implementation of App Widget functionality.
 */
public class JahaWidget extends AppWidgetProvider {

    private final String TAG = getClass().getSimpleName();
    private PaymentViewModel mPaymentViewModel;
    private Context mContext;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.w(TAG, "onUpdate method called");
        this.mContext = context;
        updateAppWidgets(context, appWidgetManager, appWidgetIds);
    }

    static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_listView);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.w(TAG, "onEnabled method called");

        mContext = context;


    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        //which layout to show on widget
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.jaha_widget);

        //RemoteViews Service needed to provide adapter for ListView
        Intent svcIntent = new Intent(context, WidgetService.class);
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(appWidgetId, R.id.widget_listView, svcIntent);
        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.widget_listView, R.id.widget_empty_view);

        return remoteViews;
    }





}

