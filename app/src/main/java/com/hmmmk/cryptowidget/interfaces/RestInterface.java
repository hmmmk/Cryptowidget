package com.hmmmk.cryptowidget.interfaces;

import com.hmmmk.cryptowidget.widget.ResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hmmmk___ on 24.02.2018.
 */

public interface RestInterface {

    @GET("{currency}/")
    Call<List<ResponseModel>> geCurrency(@Path("currency") String currency);
}
