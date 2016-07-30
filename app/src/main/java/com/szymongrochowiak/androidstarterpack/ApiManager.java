package com.szymongrochowiak.androidstarterpack;

import android.support.annotation.NonNull;

import com.szymongrochowiak.androidstarterpack.models.Berry;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Szymon Grochowiak
 */
public class ApiManager {

    private ApiInterface mApiInterface;

    public ApiManager(@NonNull ApiInterface apiInterface) {
        mApiInterface = apiInterface;
    }

    public Observable<Berry> getBerry(int id) {
        return mApiInterface.getBerry(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}