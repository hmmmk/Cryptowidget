package com.example.hmmmk.cryptowidget.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.hmmmk.cryptowidget.R;
import com.example.hmmmk.cryptowidget.Utills;
import com.example.hmmmk.cryptowidget.interfaces.OnResponseListener;

import java.text.DecimalFormat;

/**
 * Created by hmmmk___ on 24.02.2018.
 */

public class CryptoRateService extends Service implements OnResponseListener {

    private double currentRate = 0;
    private double pRate = 0;
    private double ppRate = 0;

    private final static String TAG = "CRYPTO_SERVICE";

    private CheckRateThread checkRateThread = new CheckRateThread();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "SERVICE STARTED");

        checkRateThread.setListener(this);

        if (!checkRateThread.isAlive()) {
            checkRateThread.start();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        checkRateThread.stopSelf();
        Log.d(TAG, "SERVICE DESTROYED");
    }

    @Override
    public void onResponse(ResponseModel model) {

        /*AppWidgetManager widgetManager = AppWidgetManager.getInstance(getApplicationContext());

        ComponentName thisWidget = new ComponentName(getApplicationContext(),
                CryptoWidget.class);
        int[] allWidgetIds = widgetManager.getAppWidgetIds(thisWidget);

        RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(),
                R.layout.widget_crypto_rate);

        views.setTextViewText(R.id.mrate_tv, createSpannable(model));

        widgetManager.updateAppWidget(allWidgetIds, views);*/

        createSpannable(model);
    }

    public SpannableString createSpannable(ResponseModel model) {
        DecimalFormat format = new DecimalFormat(Utills.DECIMAL_ACCURACCY_2);

        double currentRate = Double.valueOf(model.getPriceUsd());
        double pRate = currentRate +
                numFromPercent(currentRate, Double.valueOf(model.getPercentChange24h()));
        double ppRate = currentRate +
                numFromPercent(currentRate, Double.valueOf(model.getPercentChange7d()));

        AppWidgetManager widgetManager = AppWidgetManager.getInstance(getApplicationContext());

        ComponentName thisWidget = new ComponentName(getApplicationContext(),
                CryptoWidget.class);
        int[] allWidgetIds = widgetManager.getAppWidgetIds(thisWidget);

        RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(),
                R.layout.widget_crypto_rate);

        views.setTextViewText(R.id.mrate_tv, String.valueOf(format.format(currentRate)) + "$");
        views.setTextViewText(R.id.hrate_tv, String.valueOf(format.format(pRate)) + "$");
        views.setTextViewText(R.id.drate_tv, String.valueOf(format.format(ppRate)) + "$");

        widgetManager.updateAppWidget(allWidgetIds, views);

        return null;
    }

    public double numFromPercent(double number, double percent) {
        return number * percent / 100;
    }
}
