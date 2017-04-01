package com.demozi.refreshandloadmore.mvp.presenter;

/**
 * Created by wujian on 2017/3/31.
 */

public interface IPtrLoadMorePresenter {

    void loadListData(boolean refresh);

    boolean isLoadingFirstPage();

    boolean hasLoadedData();

}
