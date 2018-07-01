package com.example.hmmmk.cryptowidget.mvp.base;

public interface MvpPresenter<T extends MvpView> {

    void attachView(T view);

    void detachView();

    T getView();
}
