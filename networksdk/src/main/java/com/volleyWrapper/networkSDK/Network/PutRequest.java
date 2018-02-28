package com.volleyWrapper.networkSDK.Network;


import android.support.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.volleyWrapper.networkSDK.Listener.ErrorResponseListener;
import com.volleyWrapper.networkSDK.Listener.ResponseListener;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Class PutRequest is ItemClickListener for the GrowingListView
 *
 * @author  Bikash Kumar Mohanty
 * @version 1.0
 * @since   2017-5-1
 */
public class PutRequest extends StringRequest {

    private Map<String, String> params = new HashMap<String, String>();
    private Map<String, String> headers = new HashMap<String, String>();
    private ResponseListener responseListener = null;
    private ErrorResponseListener errorResponseListener = null;
    private String url = null;
    private Priority  priority = Priority.NORMAL;
    private Object requestTAG;
    private Object requestParams;
    private String requestbodyContentType;
    private String requestBody;
    private String encodingType;

    public PutRequest(String url,ResponseListener listener, ErrorResponseListener errorListener,Object requestTAG,@Nullable Object requestParams) {
        super(Method.PUT, url, listener, errorListener);
        this.url = url;
        this.responseListener = listener;
        this.requestTAG = requestTAG;
        this.errorResponseListener = errorListener;
        this.requestParams = requestParams;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        if (params.size()==0) return super.getParams();
        return params;
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

    @Override
    public String getBodyContentType() {
        return requestbodyContentType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return requestBody == null ? null : requestBody.getBytes((encodingType==null?"utf-8":encodingType));
        } catch (UnsupportedEncodingException uee) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    requestBody, "utf-8");
            return null;
        }
    }

    public void addPOSTParam(String key, String value)
    {
        if (key == null && value == null && key.isEmpty() && value.isEmpty()) return;
        params.put(key,value);
    }
    public void addPOSTParam(HashMap params)
    {
        if (params == null && params.size() == 0) return;
        this.params = params;
    }

    public void addHeader(String key, String value)
    {
        if (key == null && value == null && key.isEmpty() && value.isEmpty() ) return;
        headers.put(key,value);
    }
    public void addHeader(HashMap params)
    {
        if (params == null && params.size() == 0) return;
        this.headers = params;
    }

    public void setPriority(Priority priority)
    {
        if (priority==null) return;
        this.priority = priority;
    }

    public void setRequestbodyContentType(String requestbodyContentType) {
        this.requestbodyContentType = requestbodyContentType;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }
}
