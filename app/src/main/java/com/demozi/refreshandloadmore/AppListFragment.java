package com.demozi.refreshandloadmore;

import android.widget.BaseAdapter;

import com.demozi.refreshandloadmore.base.BasePtrLoadMoreFragment;
import com.demozi.refreshandloadmore.entity.StoreItem;
import com.demozi.refreshandloadmore.web.ApiRequestService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wujian on 2017/3/30.
 */

public class AppListFragment extends BasePtrLoadMoreFragment<StoreItem> {

    private AppAdapter mAdapter;

    @Override
    protected BaseAdapter getListAdapter() {
        if (mAdapter == null) mAdapter = new AppAdapter();
        return mAdapter;
    }

    @Override
    protected void invokeListWebAPI() {
        //请求数据，并将数据返回给BasePtrLoadMoreFragment中处理, 待封装...
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://o2odev.aigegou.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRequestService apiRequestService = retrofit.create(ApiRequestService.class);
        Call<StoreItem> call = apiRequestService.getStoreItems("unlimited_invitation", "goods_list_v2", "合肥市", "31.838668", "117.217654", mPage);
        call.enqueue(provideCallBack());
    }

    @Override
    protected boolean parseHasListData(StoreItem storeItem) {
        boolean hasData = storeItem != null
                && storeItem.datas != null
                && !storeItem.datas.isEmpty();
        return hasData;
    }

    @Override
    protected List<?> parseListData(StoreItem storeItem) {
        return storeItem.datas;
    }
}
