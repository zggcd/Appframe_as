package com.asiainfo.appframe.presenter;

import com.asiainfo.appframe.model.LoginModel;
import com.asiainfo.appframe.net.retrofit.response.ResponseTransformer;
import com.asiainfo.appframe.net.retrofit.schedulers.BaseSchedulerProvider;
import com.asiainfo.appframe.v.V;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class LoginPresenter {

    private LoginModel model;
    private BaseSchedulerProvider schedulerProvider;
    private CompositeDisposable mDisposable;

    public LoginPresenter(LoginModel model, V v, BaseSchedulerProvider schedulerProvider){
        this.model = model;
        this.schedulerProvider = schedulerProvider;

        mDisposable = new CompositeDisposable();
    }

    public void getAuthCode(String phone_num, String app_id){

        Disposable disposable = model.getAuthCode(phone_num, app_id)
                .compose(ResponseTransformer.handleResult())
                .compose(schedulerProvider.applySchedulers())
                .subscribe(str -> {
                    //处理数据，直接获得string
                }, throwable -> {
                    //处理异常
                });
        mDisposable.add(disposable);

    }

    public void despose(){
        mDisposable.dispose();
    }

}
