package com.wuxinbo.common.http;

@FunctionalInterface
public interface HttpCallBack<T> {

    /**
     * http 响应
     * @param resObj
     */
    void onResponse(T resObj);
}
