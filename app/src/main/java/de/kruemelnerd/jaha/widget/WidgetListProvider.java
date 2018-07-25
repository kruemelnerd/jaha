package de.kruemelnerd.jaha.widget;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;

import de.kruemelnerd.jaha.R;
import de.kruemelnerd.jaha.data.room.PaymentEntry;
import de.kruemelnerd.jaha.data.room.PaymentRepository;


public class WidgetListProvider implements RemoteViewsService.RemoteViewsFactory {
    private ArrayList listItemList = new ArrayList();
    private Context mContext = null;
    private int appWidgetId;
    private LifecycleRegistry mLifecycleRegistry;


    public WidgetListProvider(Context context, Intent intent) {
        this.mContext = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        populateListItem();
    }

    private void populateListItem() {
        final PaymentRepository repository = new PaymentRepository(mContext.getApplicationContext());

        final RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(), R.layout.jaha_widget_row);

        new LoaderPaymentEntries(repository, remoteView).execute();
    }

    private void fillData(List<PaymentEntry> paymentEntries) {
        final RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(), R.layout.jaha_widget_row);

        for (PaymentEntry entry : paymentEntries) {
            WidgetListItem listItem = new WidgetListItem();
            listItem.heading = entry.getName();
            listItem.content = String.valueOf(entry.getPrice() / 100);
            listItemList.add(listItem);

            remoteView.setTextViewText(R.id.heading, listItem.heading);
            remoteView.setTextViewText(R.id.content, listItem.content);
        }

        AppWidgetManager manager = AppWidgetManager.getInstance(this.mContext);
        int[] widgetIds = manager.getAppWidgetIds(new ComponentName(this.mContext, JahaWidget.class));
        JahaWidget.updateAppWidgets(this.mContext, manager, widgetIds);
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                mContext.getPackageName(), R.layout.jaha_widget_row);

        WidgetListItem listItem = (WidgetListItem) listItemList.get(position);
        remoteView.setTextViewText(R.id.heading, listItem.heading);
        remoteView.setTextViewText(R.id.content, listItem.content);

        return remoteView;
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    public class LoaderPaymentEntries extends AsyncTask<Void, Void, List<PaymentEntry>> {

        private PaymentRepository repository;
        private RemoteViews remoteView;

        public LoaderPaymentEntries(PaymentRepository repository, RemoteViews remoteView) {
            this.repository = repository;
            this.remoteView = remoteView;
        }


        @Override
        protected List<PaymentEntry> doInBackground(Void... voids) {
            return repository.getAllPaymentsSync();
        }

        @Override
        protected void onPostExecute(List<PaymentEntry> paymentEntries) {
            super.onPostExecute(paymentEntries);
            fillData(paymentEntries);
        }


    }
}
