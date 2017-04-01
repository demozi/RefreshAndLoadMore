package com.demozi.refreshandloadmore.mvp;

import com.demozi.refreshandloadmore.entity.SimpleResult;
import com.demozi.refreshandloadmore.mvp.model.IPtrLoadMoreModel;
import com.demozi.refreshandloadmore.mvp.presenter.PtrLoadMorePresenter;
import com.demozi.refreshandloadmore.mvp.view.IPtrLoadMoreView;

import java.util.List;

/**
 * Created by wujian on 2017/3/31.
 */

public class PtrLoadMoreCallback<T extends SimpleResult> extends BaseWebRequestCallback<T>{

    PtrLoadMorePresenter<T> mPresenter;

    public PtrLoadMoreCallback(PtrLoadMorePresenter<T> presenter) {
        super(presenter.getBaseView());
        this.mPresenter = presenter;
    }

    protected IPtrLoadMoreView getView() {
        return mPresenter.getBaseView();
    }

    protected IPtrLoadMoreModel<T> getModel() {
        return mPresenter.getBaseModel();
    }

    @Override
    public void onLoadStart() {
        if (mPresenter.isLoadingFirstPage() && !getView().isPullToRefreshing()) {
            getView().showLoadingView();
            getView().hideEmptyView();
        }
    }

    @Override
    public void onSuccess(T result) {

        if (mPresenter.isLoadingFirstPage()) {
            getView().clearData();
        }

        List<?> dataList = getModel().parseListData(result);
        boolean hasData = dataList != null && !dataList.isEmpty();
        if (hasData) {
            mPresenter.increasePage();
            getView().appendData(dataList);
        }
        getView().showEmptyView(false);
        getView().loadMoreFinish(hasData);
    }

    @Override
    public boolean onFailed(int errorCode, String errorMessage, boolean noNetwork) {
        super.onFailed(errorCode, errorMessage, noNetwork);

        getView().showEmptyView(noNetwork);
        if (mPresenter.isLoadingFirstPage()) {
            getView().loadMoreFinish(true);
        }
        if (!super.onFailed(errorCode, errorMessage, noNetwork)) {
            if (!mPresenter.isLoadingFirstPage()) {
                getView().loadMoreError(errorCode, errorMessage);
            }
        }
        return true;
    }

    @Override
    public void onLoadFinish() {
        mPresenter.setIsLoadingList(false);
        getView().hideLoadingView();
        getView().refreshComplete();
    }

}
