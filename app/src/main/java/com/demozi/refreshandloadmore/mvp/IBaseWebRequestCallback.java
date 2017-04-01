package com.demozi.refreshandloadmore.mvp;

import retrofit2.Callback;

/**
 * Created by wujian on 2017/3/31.
 */

public interface IBaseWebRequestCallback<T>{

    void onSuccess(T result);

    boolean onFailed(int errorCode, String errorMessage, boolean noNetwork);

    void onLoadStart();

    void onLoadFinish();

}
