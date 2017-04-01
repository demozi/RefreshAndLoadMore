package com.demozi.refreshandloadmore.mvp.view;


public interface IBaseViewWithWebRequest {
    void showNoNetworkError();

    void showWrongResponse(int code, String message);
}