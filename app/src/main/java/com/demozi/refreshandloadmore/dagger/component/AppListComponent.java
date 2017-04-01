package com.demozi.refreshandloadmore.dagger.component;

import com.demozi.refreshandloadmore.dagger.AppListModule;
import com.demozi.refreshandloadmore.dagger.scope.ActivityScope;
import com.demozi.refreshandloadmore.mvp.ui.MvpAppListFragment;

import dagger.Component;

/**
 * Created by wujian on 2017/4/1.
 */
@ActivityScope
@Component(modules = AppListModule.class)
public interface AppListComponent {

    void inject(MvpAppListFragment fragment);

}
