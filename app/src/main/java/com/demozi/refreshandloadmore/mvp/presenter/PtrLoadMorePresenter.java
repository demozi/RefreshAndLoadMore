package com.demozi.refreshandloadmore.mvp.presenter;

import com.demozi.refreshandloadmore.entity.SimpleResult;
import com.demozi.refreshandloadmore.mvp.PtrLoadMoreCallback;
import com.demozi.refreshandloadmore.mvp.model.IPtrLoadMoreModel;
import com.demozi.refreshandloadmore.mvp.view.IPtrLoadMoreView;
import com.demozi.refreshandloadmore.mvp.presenter.IPtrLoadMorePresenter;

/**
 * Created by wujian on 2017/3/31.
 */

public class PtrLoadMorePresenter<T extends SimpleResult> implements IPtrLoadMorePresenter {


    IPtrLoadMoreView mBaseView;
    IPtrLoadMoreModel<T> mBaseModel;

    int mPage = 1;
    boolean mIsLoadingList = false;
    boolean mIsLoadingFirstPage = true;
    boolean mHasLoadedData = false;

    public PtrLoadMorePresenter(IPtrLoadMoreView view, IPtrLoadMoreModel<T> model) {
        this.mBaseView = view;
        this.mBaseModel = model;
    }


    @Override
    public void loadListData(boolean refresh) {

        if (mIsLoadingList) {
            mBaseView.showLoadingView();
            return;
        }

        if (refresh) {
            mPage = 1;
        }
        mIsLoadingList = true;
        mHasLoadedData = true;
        mIsLoadingFirstPage = refresh;
        mBaseModel.loadListData(mPage, providePtrLoadMoreCallback());
    }

    @Override
    public boolean isLoadingFirstPage() {
        return mIsLoadingFirstPage;
    }

    @Override
    public boolean hasLoadedData() {
        return mHasLoadedData;
    }

    public void setIsLoadingList(boolean loading) {
        mIsLoadingList = loading;
    }

    public void increasePage() {
        mPage++;
    }


    public IPtrLoadMoreView getBaseView() {
        return mBaseView;
    }

    public IPtrLoadMoreModel<T> getBaseModel() {
        return mBaseModel;
    }

    PtrLoadMoreCallback<T> providePtrLoadMoreCallback() {
        return new PtrLoadMoreCallback<>(this);
    }
}
