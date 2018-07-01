package com.example.hmmmk.cryptowidget.widget;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.hmmmk.cryptowidget.common.ExtendedApplication;
import com.example.hmmmk.cryptowidget.interfaces.OnResponseListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hmmmk___ on 26.02.2018.
 */

public class CheckRateThread extends Thread {

    boolean stopFlag = false;

    private final static String TAG = "CRYPTO_THREAD";

    private OnResponseListener listener;

    @Override
    public void run() {
        Log.d(TAG, "THREAD STARTED");

        do {
            Log.d(TAG, "GETTING DATA FROM SERVER");

            ExtendedApplication.getAnInterface().geCurrency("bitcoin")
                    .enqueue(new Callback<List<ResponseModel>>() {
                        @Override
                        public void onResponse(@NonNull Call<List<ResponseModel>> call,
                                               @NonNull Response<List<ResponseModel>> response) {

                            try {
                                if (response.body() != null) {
                                    Log.d(TAG, "RESPONSE: " + Double.valueOf(response.body()
                                            .get(0).getPriceUsd()));

                                    if (listener != null)
                                        listener.onResponse(response.body().get(0));
                                }
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<List<ResponseModel>> call,
                                              @NonNull Throwable t) {

                        }
                    });
            try {
                //TODO: Reduce sleep time
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!stopFlag);

        Log.d(TAG, "THREAD STOPPED");

    }

    void stopSelf() {
        Log.d(TAG, "REQUEST THREAD STOP");

        stopFlag = true;
    }

    public void setListener(OnResponseListener listener) {
        this.listener = listener;
    }
}
