package com.volleyWrapper.networkSDK.Network;

import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.volleyWrapper.networkSDK.Listener.ErrorResponseListener;
import com.volleyWrapper.networkSDK.Listener.ResponseListener;


import java.util.HashMap;
import java.util.Map;


/**
 * Class GetRequest is ItemClickListener for the GrowingListView
 *
 * @author  Bikash Kumar Mohanty
 * @version 1.0
 * @since   2018-5-1
 */

public class GetRequest extends StringRequest {

    private Map<String, String> headers = new HashMap<String, String>();
    private Priority  priority = Priority.NORMAL;
    private String url = null;
    private ResponseListener responseListener = null;
    private ErrorResponseListener errorResponseListener = null;
    private Object requestTAG;
    private Object requestParams;


    public GetRequest(String url, ResponseListener listener, ErrorResponseListener errorListener,Object requestTAG,@Nullable Object requestParams) {
        super(Method.GET, url, listener, errorListener);
        this.url = url;
        this.responseListener = listener;
        this.requestTAG = requestTAG;
        this.errorResponseListener = errorListener;
        this.requestParams = requestParams;
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (headers.size()==0) return super.getHeaders();
        return headers;
    }

    @Override
    public Priority getPriority() {
        return priority;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        Response<String> responseObject = super.parseNetworkResponse(response);
        responseListener.getListener().onResponseHeaders(response.headers,requestTAG);
        responseListener.getListener().onResponseObject(response,responseObject,requestTAG,requestParams);

        return responseObject;
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        VolleyError error = super.parseNetworkError(volleyError);
        errorResponseListener.getListener().onErrorResponse(error,requestTAG);
        errorResponseListener.getListener().onErrorResponse(error,error.networkResponse,requestTAG,requestParams);
        return super.parseNetworkError(volleyError);
    }

    public void addHeader(String key, String value)
    {
        if (key == null && value == null && key.isEmpty() && value.isEmpty()) return;
        headers.put(key,value);
    }
    public void addHeader(HashMap<String,String> params)
    {
        if (params == null && params.size() == 0) return;
        this.headers = params;
    }

    public void setPriority(Priority priority)
    {
        if (priority==null) return;
        this.priority = priority;
    }

    public String getUrl()
    {
        return url;
    }
}
