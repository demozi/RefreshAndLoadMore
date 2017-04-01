package com.demozi.refreshandloadmore.mvp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.demozi.refreshandloadmore.R;
import com.demozi.refreshandloadmore.base.PtrLoadMoreListAdapterAction;
import com.demozi.refreshandloadmore.lib.GridViewWithHeaderAndFooter;
import com.demozi.refreshandloadmore.lib.LoadMoreContainer;
import com.demozi.refreshandloadmore.lib.LoadMoreContainerBase;
import com.demozi.refreshandloadmore.lib.LoadMoreHandler;
import com.demozi.refreshandloadmore.mvp.view.IPtrLoadMoreView;
import com.demozi.refreshandloadmore.mvp.presenter.IPtrLoadMorePresenter;
import com.demozi.refreshandloadmore.views.AppLoadMoreFooterView;

import java.util.List;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * mvp模式下的下拉刷新&上拉加载更多
 * Created by wujian on 2017/3/31.
 */

public abstract class BaseMvpPtrLoadMoreFragment extends Fragment implements PtrHandler, LoadMoreHandler, IPtrLoadMoreView {

    protected PtrClassicFrameLayout mPtrFrameLayout;
    protected AbsListView mAbsListView;
    protected ListView mListView;
    protected GridViewWithHeaderAndFooter mGridView;
    protected LoadMoreContainerBase mLoadMoreContainer;
    protected View mEmptyView;

    protected BaseAdapter mListAdapter;
    protected PtrLoadMoreListAdapterAction mAdapterAction;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initMvp();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView;
        if (useGridView()) {
            fragmentView = inflater.inflate(R.layout.fragment_base_ptr_load_more_grid, container, false);
        } else {
            fragmentView = inflater.inflate(R.layout.fragment_base_ptr_load_more_list, container, false);
        }
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews();
        initViews();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
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

    /**
     * 满足以下其中一个条件可以在onActivityCreated时加载列表数据：
     * <ul>
     * <li>本Fragment不在ViewPager中/li>
     * <li>本Fragment在ViewPager中、没有加载过数据、对用户可见</li>
     * </ul>
     */
    protected void loadDataOnActivityCreated() {
        if (!isInViewPager()
                || (!getPresenter().hasLoadedData() && getUserVisibleHint())) {
            loadListData(true);
        }
    }

    /**
     * 满足以下所有条件可以在onResume时加载列表数据：
     * <ul>
     * <li>已标记需要每次页面对用户可见时加载列表数据</li>
     * <li>本Fragment不在ViewPager中，或在ViewPager中并且对用户可见</li>
     * </ul>
     */
    protected void loadDataOnResume() {
        if (needRefreshDataWhenVisibleToUserEveryTime() &&
                (!isInViewPager() || getUserVisibleHint())) {
            loadListData(true);
        }
    }

    protected void loadDataWhenVisibleToUser() {
        boolean isViewCreated = (getView() != null);
        if (isInViewPager() && isViewCreated) {
            if (needRefreshDataWhenVisibleToUserEveryTime()) {
                loadListData(true);
            } else if (!getPresenter().hasLoadedData()) {
                loadListData(true);
            }
        }
    }


    public void loadListData(boolean refresh) {
        getPresenter().loadListData(refresh);
    }


    protected void findViews() {
        View view = getView();
        mPtrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_frame);
        mLoadMoreContainer = (LoadMoreContainerBase) view.findViewById(R.id.load_more_container);
        if (useGridView()) {
            mAbsListView = mGridView = (GridViewWithHeaderAndFooter) view.findViewById(R.id.grid_view);
        } else {
            mAbsListView = mListView = (ListView) view.findViewById(R.id.list_view);
        }
    }

    protected void initViews() {
        setupEmptyView();
        initPullToRefresh();
        initLoadMore();
        initListAdapter();
    }

    protected void setupEmptyView() {
        View loadingEmptyView = LayoutInflater.from(getActivity()).inflate(R.layout.general_empty_view, mAbsListView, false);
        mEmptyView = loadingEmptyView.findViewById(R.id.empty_view);

        mLoadMoreContainer.addView(loadingEmptyView);
        mAbsListView.setEmptyView(loadingEmptyView);
        mEmptyView.setVisibility(View.INVISIBLE);
    }

    protected void initPullToRefresh() {
        // 下拉刷新
        mPtrFrameLayout.setLoadingMinTime(1000);
        mPtrFrameLayout.disableWhenHorizontalMove(true);
        mPtrFrameLayout.setPtrHandler(this);
        PtrClassicDefaultHeader refreshHeaderView = new PtrClassicDefaultHeader(getActivity());
        mPtrFrameLayout.setHeaderView(refreshHeaderView);
        mPtrFrameLayout.addPtrUIHandler(refreshHeaderView);
    }

    protected void initLoadMore() {
        if (!enableLoadMoreFeature()) {
            return;
        }
        // 加载更多
        AppLoadMoreFooterView loadMoreView = new AppLoadMoreFooterView(getActivity());
        mLoadMoreContainer.setLoadMoreView(loadMoreView);
        mLoadMoreContainer.setLoadMoreUIHandler(loadMoreView);
        mLoadMoreContainer.setLoadMoreHandler(this);
        mLoadMoreContainer.setShowLoadingForFirstPage(false);
    }

    protected void initListAdapter() {
        mListAdapter = getListAdapter();
        mAbsListView.setAdapter(mListAdapter);
        mAdapterAction = (PtrLoadMoreListAdapterAction) mListAdapter;
    }


    protected abstract void initMvp();

    protected abstract BaseAdapter getListAdapter();

    protected boolean isInViewPager() {
        return false;
    }

    protected boolean needRefreshDataWhenVisibleToUserEveryTime() {
        return false;
    }

    protected boolean enableLoadMoreFeature() {
        return true;
    }

    protected boolean useGridView() {
        return false;
    }

    protected abstract IPtrLoadMorePresenter getPresenter();


    protected String getEmptyText() {
        return getString(R.string.no_data);
    }



    // >>>>>>>>>>>>>>> PtrHandler
    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, mAbsListView, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        loadListData(true);
    }
    // <<<<<<<<<<<<<<<<< PtrHandler

    // >>>>>>>>>>>>>>> LoadMoreHandler
    @Override
    public void onLoadMore(LoadMoreContainer loadMoreContainer) {
        loadListData(false);
    }
    // <<<<<<<<<<<<<<<<< LoadMoreHandler


    // >>>>>>>>>>>>>>> View interface
    @Override
    public void showNoNetworkError() {
//        super.showNoNetworkError();  BaseFragment
        if (!getPresenter().isLoadingFirstPage()) {
            loadMoreError(0, getString(R.string.no_network));
        }
    }

    @Override
    public void showWrongResponse(int code, String message) {
//        super.showWrongResponse(code, message);
        if (!getPresenter().isLoadingFirstPage()) {
            loadMoreError(code, message);
        }
    }

    @Override
    public void refreshComplete() {
        if (mPtrFrameLayout.isRefreshing()) {
            mPtrFrameLayout.refreshComplete();
        }
    }

    @Override
    public void loadMoreFinish(boolean hasMore) {
        mLoadMoreContainer.loadMoreFinish(mListAdapter.isEmpty(), hasMore);
    }

    @Override
    public void loadMoreError(int errorCode, String errorMessage) {
        mLoadMoreContainer.loadMoreError(errorCode, errorMessage);
    }

    @Override
    public void clearData() {
        mAdapterAction.clearData();
    }

    @Override
    public void appendData(List data) {
        mAdapterAction.appendData(data);
    }

    @Override
    public void showIsLoadingDataNow() {}

    @Override
    public boolean isPullToRefreshing() {
        return mPtrFrameLayout.isRefreshing();
    }

    @Override
    public void showEmptyView(boolean noNetwork) {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.VISIBLE);
            TextView emptyTv = (TextView) mEmptyView.findViewById(R.id.empty_text);
            if (emptyTv != null) {
                emptyTv.setText(noNetwork ? getString(R.string.no_network) : getEmptyText());
            }
        }
    }

    @Override
    public void hideEmptyView() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility(View.INVISIBLE);
        }
    }
    // <<<<<<<<<<<<<<<<< View interface
}
