package com.demozi.refreshandloadmore.base;

import java.util.List;

/**
 * Created by wujian on 2017/3/29.
 */

public interface PtrLoadMoreListAdapterAction<T> {

    void appendData(List<T> data);

    void clearData();

    List<T> getData();

}
