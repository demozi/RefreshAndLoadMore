package com.demozi.refreshandloadmore.mvp.model;

import com.demozi.refreshandloadmore.entity.SimpleResult;
import com.demozi.refreshandloadmore.mvp.PtrLoadMoreCallback;

import java.util.List;

/**
 * Created by wujian on 2017/3/31.
 */

public interface IPtrLoadMoreModel<T extends SimpleResult> {

    void loadListData(int page, PtrLoadMoreCallback<T> callback);

    List<?> parseListData(T result);

}
