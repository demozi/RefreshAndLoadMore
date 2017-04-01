package com.demozi.refreshandloadmore.views;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.demozi.refreshandloadmore.R;
import com.demozi.refreshandloadmore.lib.LoadMoreContainer;
import com.demozi.refreshandloadmore.lib.LoadMoreUIHandler;

/**
 * Created by wujian on 2017/3/28.
 */

public class AppLoadMoreFooterView extends RelativeLayout implements LoadMoreUIHandler {


    private ProgressBar mProgressBar;
    private TextView mTextView;

    public AppLoadMoreFooterView(Context context) {
        super(context);
        initView();
    }

    public AppLoadMoreFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AppLoadMoreFooterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.app_loader_more_foot_view, this);
        mProgressBar = (ProgressBar) findViewById(R.id.loading_progress);
        mTextView = (TextView) findViewById(R.id.loading_text);
        setVisibility(INVISIBLE);
    }

    @Override
    public void onLoading(LoadMoreContainer container) {
        setVisibility(VISIBLE);
        mProgressBar.setVisibility(VISIBLE);
        mTextView.setText(getResources().getString(R.string.cube_views_load_more_loading));
    }

    @Override
    public void onLoadFinish(LoadMoreContainer container, boolean empty, boolean hasMore) {
        if (!hasMore) {
           if (empty) {
               mTextView.setText(getResources().getString(R.string.cube_views_load_more_loaded_empty));
           }else {
               mTextView.setText(getResources().getString(R.string.cube_views_load_more_loaded_no_more));
           }
        }else {
            setVisibility(INVISIBLE);
        }
    }

    @Override
    public void onWaitToLoadMore(LoadMoreContainer container) {
        setVisibility(VISIBLE);
        mProgressBar.setVisibility(INVISIBLE);
        mTextView.setText(getResources().getString(R.string.cube_views_load_more_click_to_load_more));
    }

    @Override
    public void onLoadError(LoadMoreContainer container, int errorCode, String errorMessage) {
        setVisibility(VISIBLE);
        mProgressBar.setVisibility(INVISIBLE);

        if (TextUtils.isEmpty(errorMessage)) {
            mTextView.setText(getResources().getString(R.string.cube_views_load_more_error));
        }else {
            mTextView.setText(errorMessage);
        }
    }
}
