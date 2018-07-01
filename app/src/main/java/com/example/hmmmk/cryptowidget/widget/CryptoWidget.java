package com.example.hmmmk.cryptowidget.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.hmmmk.cryptowidget.R;
import com.example.hmmmk.cryptowidget.mvp.widgetactivity.WidgetActivity;

/**
 * Created by hmmmk___ on 21.02.2018.
 */

public class CryptoWidget extends AppWidgetProvider {

    private static final String TAG = "CRYPTO_WIDGET";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        Intent intent = new Intent(context, CryptoRateService.class);
        context.startService(intent);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager,
                         final int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_crypto_rate);
        // When we click the widget, we want to open our main activity.
        Intent launchActivity = new Intent(context, WidgetActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, launchActivity,
                0);
        remoteViews.setOnClickPendingIntent(R.id.widget_container, pendingIntent);;
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);

        Intent intent = new Intent(context, CryptoRateService.class);
        context.stopService(intent);
    }
}