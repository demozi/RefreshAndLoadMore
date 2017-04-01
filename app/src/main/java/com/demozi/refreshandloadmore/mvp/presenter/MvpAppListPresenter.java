package com.demozi.refreshandloadmore.mvp.presenter;

import com.demozi.refreshandloadmore.entity.StoreItem;
import com.demozi.refreshandloadmore.mvp.model.IMvpAppListModel;
import com.demozi.refreshandloadmore.mvp.model.IPtrLoadMoreModel;
import com.demozi.refreshandloadmore.mvp.view.IMvpAppListView;
import com.demozi.refreshandloadmore.mvp.view.IPtrLoadMoreView;

/**
 * Created by wujian on 2017/3/31.
 */

public class MvpAppListPresenter extends PtrLoadMorePresenter<StoreItem> implements IMvpAppListPresenter {


    IMvpAppListView mView;
    IMvpAppListModel mModel;

    public MvpAppListPresenter(IPtrLoadMoreView view, IPtrLoadMoreModel model) {
        super(view, model);
    }


}
