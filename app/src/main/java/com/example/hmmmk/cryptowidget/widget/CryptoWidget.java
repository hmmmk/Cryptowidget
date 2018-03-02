package com.example.hmmmk.cryptowidget.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.hmmmk.cryptowidget.interfaces.OnResponseListener;

/**
 * Created by hmmmk___ on 21.02.2018.
 */

public class CryptoWidget extends AppWidgetProvider {

    private static final String TAG = "CRYPTO_WIDGET";

    private float currRate = 0;

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager,
                         final int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        ComponentName thisWidget = new ComponentName(context,
                CryptoWidget.class);
        final int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

        Intent intent = new Intent(context, CryptoRateService.class);
        context.startService(intent);
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

    public Number prepareRate(float rate) {
        Number res;

        if (rate <= 10)
            res = rate;
        else
            res = Math.round(rate);

        return res;
    }
}