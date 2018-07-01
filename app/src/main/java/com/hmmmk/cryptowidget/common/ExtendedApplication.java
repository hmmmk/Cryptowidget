package com.hmmmk.cryptowidget.common;

import android.app.Application;

import com.hmmmk.cryptowidget.interfaces.RestInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hmmmk___ on 24.02.2018.
 */

public class ExtendedApplication extends Application {

    private Retrofit retrofit;
    private static RestInterface anInterface;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.coinmarketcap.com/v1/ticker/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        anInterface = retrofit.create(RestInterface.class);
    }

    public static RestInterface getAnInterface() {
        return anInterface;
    }
}
