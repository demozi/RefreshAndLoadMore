package com.demozi.refreshandloadmore.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.demozi.refreshandloadmore.entity.SimpleResult;
import com.demozi.refreshandloadmore.views.AppLoadMoreFooterView;
import com.demozi.refreshandloadmore.R;
import com.demozi.refreshandloadmore.lib.GridViewWithHeaderAndFooter;
import com.demozi.refreshandloadmore.lib.LoadMoreContainer;
import com.demozi.refreshandloadmore.lib.LoadMoreContainerBase;
import com.demozi.refreshandloadmore.lib.LoadMoreHandler;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 下拉刷新&上拉加载更多
 *
 * Created by wujian on 2017/3/29.
 */

public abstract class BasePtrLoadMoreFragment<T extends SimpleResult> extends Fragment implements PtrHandler, LoadMoreHandler{

    protected PtrClassicFrameLayout mPtrFrameLayout;
    protected AbsListView mAbsListView;
    protected ListView mListView;
    protected GridViewWithHeaderAndFooter mGridView;
    protected LoadMoreContainerBase mLoadMoreContainer;
    protected View mEmptyView;

    protected BaseAdapter mListAdapter;
    protected PtrLoadMoreListAdapterAction mAdapterAction;
    protected int mPage = 1;
    protected boolean mIsLoadingList = false;
    protected boolean mIsRefreshListOfLatestLoading = true;
    protected boolean mHasLoadedData = false;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View fragmentView;
        if (useGridView()) {
            fragmentView = inflater.inflate(R.layout.fragment_base_ptr_load_more_grid, container, false);
        }else {
            fragmentView = inflater.inflate(R.layout.fragment_base_ptr_load_more_list, container, false);
        }

        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews();
        initViews();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadDataOnActivityCreated();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataOnResume();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            loadDataWhenVisibleToUser();
        }
    }

    protected void loadDataOnActivityCreated() {
        if (!isInViewPager() || (isInViewPager() && !mHasLoadedData && getUserVisibleHint())) {
            loadListData(true);
        }
    }

    protected void loadDataOnResume() {
        if (needRefreshDataWhenVisibleToUserEveryTime()) {
            if (!isInViewPager() || (isInViewPager() && getUserVisibleHint())) {
                loadListData(true);
            }
        }
    }

    protected void loadDataWhenVisibleToUser() {
        boolean isViewCreated = (isResumed() || getView() != null);
        if (isInViewPager() && isViewCreated) {
            if (needRefreshDataWhenVisibleToUserEveryTime() || !mHasLoadedData) {
                loadListData(true);
            }
        }
    }


    /*************  配置信息（子类重写更改配置） start *****************/
    protected boolean useGridView() {
        return false;
    }

    protected boolean enableLoadMoreFeature() {
        return true;
    }

    protected boolean isInViewPager() {
        return false;
    }

    protected boolean needRefreshDataWhenVisibleToUserEveryTime() {
        return false;
    }

    protected boolean enableShowLoadingViewWhenRefreshList() {
        return true;
    }

    /*************  配置信息 end *****************/


    private void findViews() {
        View view = getView();
        mPtrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_frame);
        mLoadMoreContainer = (LoadMoreContainerBase) view.findViewById(R.id.load_more_container);
        if (useGridView()) {
            mAbsListView = mGridView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.grid_view);
        }else {
            mAbsListView = mListView = (ListView) view.findViewById(R.id.list_view);
        }
    }

    private void initViews() {
        setupEmptyView();
        initPullToRefresh();
        initLoadMore();
        initListAdapter();
    }

    private void setupEmptyView() {
        View loadingEmptyView = LayoutInflater.from(getContext()).inflate(R.layout.general_empty_view, mAbsListView, false);
        mEmptyView = loadingEmptyView.findViewById(R.id.empty_view);
        mLoadMoreContainer.addView(loadingEmptyView);
        mAbsListView.setEmptyView(loadingEmptyView);
        mEmptyView.setVisibility(View.INVISIBLE);
    }

    private void initPullToRefresh() {
        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.setPtrHandler(this);
        PtrClassicDefaultHeader refreshHeaderView = new PtrClassicDefaultHeader(getContext());
        mPtrFrameLayout.setHeaderView(refreshHeaderView);
        mPtrFrameLayout.addPtrUIHandler(refreshHeaderView);
        mPtrFrameLayout.disableWhenHorizontalMove(true);
    }

    private void initLoadMore() {
        if (!enableLoadMoreFeature()) return;
        AppLoadMoreFooterView loadMoreFooterView = new AppLoadMoreFooterView(getContext());
        mLoadMoreContainer.setLoadMoreView(loadMoreFooterView);
        mLoadMoreContainer.setLoadMoreUIHandler(loadMoreFooterView);
        mLoadMoreContainer.setLoadMoreHandler(this);
        mLoadMoreContainer.setShowLoadingForFirstPage(false);
    }

    private void initListAdapter() {
        mListAdapter = getListAdapter();
        mAbsListView.setAdapter(mListAdapter);
        mAdapterAction = (PtrLoadMoreListAdapterAction) mListAdapter;
    }


    protected void loadListData(boolean refresh) {
        if (mIsLoadingList) return;
        if (refresh) {
            mPage = 1;
            if (!mPtrFrameLayout.isRefreshing()) {
                if (enableShowLoadingViewWhenRefreshList()) {
//                    showLoadingView();
                    // TODO: 2017/3/29 显示加载框 
                }
                if (mEmptyView != null) {
                    mEmptyView.setVisibility(View.INVISIBLE);
                }
            }
        }
        mIsLoadingList = true;
        mHasLoadedData = true;
        mIsRefreshListOfLatestLoading = refresh;
        invokeListWebAPI();
    }

    protected abstract BaseAdapter getListAdapter();

    /************************* 通用业务数据处理 *******************************/

    protected abstract void invokeListWebAPI();

    protected abstract boolean parseHasListData(T t);

    protected abstract List<?> parseListData(T t);


    protected Callback<T> provideCallBack() {

        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {

                if (isDetached()) return;

                if (response.isSuccessful()) {

                    boolean hasData = parseHasListData(response.body());
                    onListDataRequestSuccess(hasData);
                    if (hasData){
                        mPage++;
                        mAdapterAction.appendData(parseListData(response.body()));
                    }
                }else {
                    onListDataRequestFailure(500, "error msg");
                }
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                if (isDetached()) return;
                onListDataRequestFailure(500, t.toString());
            }
        };
    }

    protected void onListDataRequestSuccess(boolean hasData) {

        mIsLoadingList = false;

        if (enableShowLoadingViewWhenRefreshList()) {
            // TODO: 2017/3/29  hideLoadingView()
        }

        // TODO: 2017/3/29 代码有待优化
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
            TextView emptyTv = (TextView) mEmptyView.findViewById(R.id.empty_text);
            if (emptyTv != null) {
                emptyTv.setText(getString(R.string.no_data));
            }
        }

        if (mPage == 1) {
            mAdapterAction.clearData();
        }

        if (mPtrFrameLayout.isRefreshing()) {
            mPtrFrameLayout.refreshComplete();
        }
        mLoadMoreContainer.loadMoreFinish(mListAdapter.isEmpty(), hasData);
    }

    protected void onListDataRequestFailure(int statusCode, String responseBody) {
        mIsLoadingList = false;

        if (enableShowLoadingViewWhenRefreshList()) {
            // TODO: 2017/3/29  hideLoadingView()
        }

        // TODO: 2017/3/29 代码有待优化
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
            TextView emptyTv = (TextView) mEmptyView.findViewById(R.id.empty_text);
            if (emptyTv != null) {
                emptyTv.setText(getString(R.string.no_network));
            }
        }

        if (mPtrFrameLayout.isRefreshing()) {
            mPtrFrameLayout.refreshComplete();
        }

        if (mIsRefreshListOfLatestLoading) {
            mLoadMoreContainer.loadMoreFinish(mListAdapter.isEmpty(), true);
        }else {
            mLoadMoreContainer.loadMoreError(statusCode,responseBody);
        }
    }



    /******************************* PtrHandler Callback*******************************/
    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mAbsListView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        loadListData(true);
    }

    @Override
    public void onLoadMore(LoadMoreContainer loadMoreContainer) {
        loadListData(false);
    }
}
