package com.volleyWrapper.networkSDK.Listener;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Class ResponseListener is ItemClickListener for the GrowingListView
 *
 * @author  Bikash Kumar Mohanty
 * @version 1.0
 * @since   2018-8-1
 */
public class ResponseListener implements Response.Listener<String> {

    private Listener listener;

    public ResponseListener(Listener listener)
    {
        this.listener = listener;

    }

    public Listener getListener()
    {
        return listener;
    }
    @Override
    public void onResponse(String response) {
        listener.onResponse(response);
    }

    public interface Listener
    {
        void onResponse(String response);
        void onResponseHeaders(Map<String,String> headers,Object requestTAG);
        void onResponseObject(NetworkResponse response,Response<String> responseObject,Object requestTAG,Object requestParams);
    }
}
