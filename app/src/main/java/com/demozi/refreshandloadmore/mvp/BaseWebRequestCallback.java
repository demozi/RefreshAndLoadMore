package com.demozi.refreshandloadmore.mvp;

import android.text.TextUtils;

import com.demozi.refreshandloadmore.mvp.view.IBaseViewWithWebRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by wujian on 2017/3/31.
 */

public abstract class BaseWebRequestCallback<T> implements IBaseWebRequestCallback<T> , Callback<T>{


    protected IBaseViewWithWebRequest mBaseView;

    public BaseWebRequestCallback(IBaseViewWithWebRequest baseView) {
        this.mBaseView = baseView;
    }

    @Override
    public boolean onFailed(int errorCode, String errorMessage, boolean noNetwork) {
        return handleCommonError(errorCode, errorMessage, noNetwork);
    }

    public boolean handleCommonError(int errorCode, String errorMessage, boolean noNetwork) {
        if (noNetwork) {
            mBaseView.showNoNetworkError();
            return true;
        } else if (!TextUtils.isEmpty(errorMessage)) {
            mBaseView.showWrongResponse(errorCode, errorMessage);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onSuccess(response.body());
        }else{
            onFailed(500, "error msg", false);
        }
        onLoadFinish();
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailed(500, "error msg", true);
        onLoadFinish();
    }
}
