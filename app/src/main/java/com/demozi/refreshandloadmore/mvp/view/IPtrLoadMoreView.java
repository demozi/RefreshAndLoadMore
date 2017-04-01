package com.demozi.refreshandloadmore.mvp.view;

import java.util.List;

/**
 * Created by wujian on 2017/3/31.
 */

public interface IPtrLoadMoreView extends IBaseViewWithLoading, IBaseViewWithWebRequest {


    void refreshComplete();

    void loadMoreFinish(boolean hasMore);

    void loadMoreError(int errorCode, String errorMessage);

    void clearData();

    void appendData(List<?> data);

    void showIsLoadingDataNow();

    boolean isPullToRefreshing();

    void hideEmptyView();

    void showEmptyView(boolean noNetwork);

}
