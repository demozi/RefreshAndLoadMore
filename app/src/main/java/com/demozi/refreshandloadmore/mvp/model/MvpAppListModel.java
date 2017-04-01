package com.demozi.refreshandloadmore.mvp.model;

import com.demozi.refreshandloadmore.entity.StoreItem;
import com.demozi.refreshandloadmore.mvp.PtrLoadMoreCallback;
import com.demozi.refreshandloadmore.mvp.model.IMvpAppListModel;
import com.demozi.refreshandloadmore.web.ApiRequestService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wujian on 2017/3/31.
 */

public class MvpAppListModel implements IMvpAppListModel {


    @Override
    public void loadListData(int page, PtrLoadMoreCallback<StoreItem> callback) {
        //请求数据，并将数据返回给BasePtrLoadMoreFragment中处理

        callback.onLoadStart();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://o2odev.aigegou.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiRequestService apiRequestService = retrofit.create(ApiRequestService.class);
        Call<StoreItem> call = apiRequestService.getStoreItems("unlimited_invitation", "goods_list_v2", "合肥市", "31.838668", "117.217654", page);
        call.enqueue(callback);
    }

    @Override
    public List<?> parseListData(StoreItem result) {
        boolean hasData = result != null
                && result.datas != null
                && !result.datas.isEmpty();
        if (hasData) return result.datas;
        return null;
    }
}
