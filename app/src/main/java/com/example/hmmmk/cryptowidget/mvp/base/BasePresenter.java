package com.example.hmmmk.cryptowidget.mvp.base;

public class BasePresenter<T extends MvpView> implements MvpPresenter<T> {

    private T view;

    @Override
    public void attachView(T view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public T getView() {
        return view;
    }
}
