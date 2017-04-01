package com.demozi.refreshandloadmore.dagger;

import com.demozi.refreshandloadmore.dagger.scope.ActivityScope;
import com.demozi.refreshandloadmore.mvp.model.IMvpAppListModel;
import com.demozi.refreshandloadmore.mvp.presenter.IMvpAppListPresenter;
import com.demozi.refreshandloadmore.mvp.view.IMvpAppListView;
import com.demozi.refreshandloadmore.mvp.model.MvpAppListModel;
import com.demozi.refreshandloadmore.mvp.presenter.MvpAppListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wujian on 2017/4/1.
 */

@Module
public class AppListModule {

    IMvpAppListView mView;

    public AppListModule(IMvpAppListView view) {
        this.mView = view;
    }

    @Provides
    @ActivityScope
    public IMvpAppListModel provideModel() {
        return new MvpAppListModel();
    }

    @Provides
    @ActivityScope
    public IMvpAppListPresenter providePresenter(IMvpAppListModel model) {
        return new MvpAppListPresenter(mView, model);
    }
}
