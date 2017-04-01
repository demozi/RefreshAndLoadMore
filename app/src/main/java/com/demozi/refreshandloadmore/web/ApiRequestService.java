package com.demozi.refreshandloadmore.web;

import com.demozi.refreshandloadmore.entity.StoreItem;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

/**
 * Created by wujian on 2017/3/30.
 *
 * 定义接口请求参数
 */

public interface ApiRequestService {

    @GET("/mobile/index.php")
    Call<StoreItem> getStoreItems(@Query("act") String act,
                                  @Query("op") String op,
                                  @Query("city_name") String city_name,
                                  @Query("lat") String lat,
                                  @Query("lng") String lng,
                                  @Query("curpage") int curpage);
}
