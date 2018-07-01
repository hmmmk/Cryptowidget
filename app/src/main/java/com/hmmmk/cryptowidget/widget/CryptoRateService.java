package com.hmmmk.cryptowidget.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.widget.RemoteViews;

import com.hmmmk.cryptowidget.R;
import com.hmmmk.cryptowidget.common.Utils;
import com.hmmmk.cryptowidget.interfaces.OnResponseListener;

import java.text.DecimalFormat;

/**
 * Created by hmmmk___ on 24.02.2018.
 */

public class CryptoRateService extends Service implements OnResponseListener {

    private final static String TAG = "CRYPTO_SERVICE";

    private CheckRateThread checkRateThread = new CheckRateThread();

    private ResponseModel localModel;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "SERVICE STARTED");

        checkRateThread.setListener(this);

        if (!checkRateThread.isAlive())
            checkRateThread.start();

        if (localModel != null)
            onResponse(localModel);

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
        localModel = model;

        AppWidgetManager widgetManager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName thisWidget = new ComponentName(getApplicationContext(),
                CryptoWidget.class);
        int[] allWidgetIds = widgetManager.getAppWidgetIds(thisWidget);

        RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(),
                R.layout.widget_crypto_rate);

        SpannableString[] rates = createSpannable(model);

        views.setTextViewText(R.id.mrate_tv, rates[0]);
        views.setTextViewText(R.id.hrate_tv, rates[1]);
        views.setTextViewText(R.id.drate_tv, rates[2]);

        widgetManager.updateAppWidget(allWidgetIds, views);

        createSpannable(model);
    }

    //TODO: Simplify
    public SpannableString[] createSpannable(ResponseModel model) {
        DecimalFormat format = new DecimalFormat(Utils.DECIMAL_ACCURACY_2);

        double percentChange = Double.valueOf(model.getPercentChange1h());
        double p_percentChange = Double.valueOf(model.getPercentChange24h());
        double pp_percentChange = Double.valueOf(model.getPercentChange7d());

        double currentRate = Double.valueOf(model.getPriceUsd());
        double pRate = currentRate -
                Utils.numFromPercent(currentRate, p_percentChange);
        double ppRate = currentRate -
                Utils.numFromPercent(currentRate, pp_percentChange);

        int color = percentChange > 0 ? Color.GREEN : Color.RED;
        int pColor = p_percentChange > 0 ? Color.GREEN : Color.RED;
        int ppColor = pp_percentChange > 0 ? Color.GREEN : Color.RED;

        StringBuilder percentChangeStr =
                new StringBuilder("(" + String.valueOf(Math.abs(percentChange)) + "%)");
        StringBuilder p_percentChangeStr =
                new StringBuilder("(" + String.valueOf(Math.abs(p_percentChange)) + "%)");
        StringBuilder pp_percentChangeStr =
                new StringBuilder("(" + String.valueOf(Math.abs(pp_percentChange)) + "%)");

        String rateString = format.format(currentRate) + "$";
        String pRateString = format.format(pRate) + "$";
        String ppRateString = format.format(ppRate) + "$";

        SpannableString currentRateStringBuilder =
                new SpannableString(rateString +
                        "\n" +
                        percentChangeStr +
                        getApplicationContext().getString(R.string.minutes));
        SpannableString pRateStringBuilder =
                new SpannableString(pRateString +
                        "\n" +
                        p_percentChangeStr +
                        getApplicationContext().getString(R.string.hours));
        SpannableString ppRateStringBuilder =
                new SpannableString(ppRateString +
                        "\n" +
                        pp_percentChangeStr +
                        getApplicationContext().getString(R.string.days));

        //Setting spans to lines

        currentRateStringBuilder.setSpan(new RelativeSizeSpan(0.7f),
                rateString.length(),
                rateString.length() + percentChangeStr.length() + 1,
                0);

        pRateStringBuilder.setSpan(new RelativeSizeSpan(0.7f),
                pRateString.length(),
                pRateString.length() + p_percentChangeStr.length() + 1,
                0);

        ppRateStringBuilder.setSpan(new RelativeSizeSpan(0.7f),
                ppRateString.length(),
                ppRateString.length() + pp_percentChangeStr.length() + 1,
                0);

        currentRateStringBuilder.setSpan(new ForegroundColorSpan(color),
                rateString.length(),
                rateString.length() + percentChangeStr.length() + 1,
                0);

        pRateStringBuilder.setSpan(new ForegroundColorSpan(pColor),
                pRateString.length(),
                pRateString.length() + p_percentChangeStr.length() + 1,
                0);

        ppRateStringBuilder.setSpan(new ForegroundColorSpan(ppColor),
                ppRateString.length(),
                ppRateString.length() + pp_percentChangeStr.length() + 1,
                0);

        currentRateStringBuilder.setSpan(new RelativeSizeSpan(0.5f),
                currentRateStringBuilder.length() - getApplicationContext().getString(R.string.minutes).length(),
                currentRateStringBuilder.length(),
                0);

        pRateStringBuilder.setSpan(new RelativeSizeSpan(0.5f),
                pRateStringBuilder.length() - getApplicationContext().getString(R.string.hours).length(),
                pRateStringBuilder.length(),
                0);

        ppRateStringBuilder.setSpan(new RelativeSizeSpan(0.5f),
                ppRateStringBuilder.length() - getApplicationContext().getString(R.string.days).length(),
                ppRateStringBuilder.length(),
                0);

        return new SpannableString [] {
                currentRateStringBuilder,
                pRateStringBuilder,
                ppRateStringBuilder
        };
    }
}
