package com.demozi.refreshandloadmore.mvp.ui;

import android.widget.BaseAdapter;

import com.demozi.refreshandloadmore.AppAdapter;
import com.demozi.refreshandloadmore.dagger.AppListModule;
import com.demozi.refreshandloadmore.dagger.component.DaggerAppListComponent;
import com.demozi.refreshandloadmore.mvp.presenter.IMvpAppListPresenter;
import com.demozi.refreshandloadmore.mvp.presenter.IPtrLoadMorePresenter;
import com.demozi.refreshandloadmore.mvp.view.IMvpAppListView;

import javax.inject.Inject;

/**
 * Created by wujian on 2017/3/31.
 */

public class MvpAppListFragment extends BaseMvpPtrLoadMoreFragment implements IMvpAppListView {


    AppAdapter mAdapter;

    @Inject
    IMvpAppListPresenter mPresenter;

    @Override
    protected void initMvp() {
        DaggerAppListComponent
                .builder()
                .appListModule(new AppListModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected BaseAdapter getListAdapter() {
        if (mAdapter == null) mAdapter = new AppAdapter();
        return mAdapter;
    }

    @Override
    protected IPtrLoadMorePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }
}
