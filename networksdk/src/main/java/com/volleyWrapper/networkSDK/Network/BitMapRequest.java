package com.volleyWrapper.networkSDK.Network;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.volleyWrapper.networkSDK.Listener.BitMapResponseListner;
import com.volleyWrapper.networkSDK.Listener.ErrorResponseListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 5156634 on 1/5/2018.
 */
public class BitMapRequest extends ImageRequest{

    private Map<String, String> headers = new HashMap<String, String>();
    private Priority  priority = Priority.NORMAL;
    private String url = null;
    private BitMapResponseListner responseListener = null;
    private ErrorResponseListener errorResponseListener = null;
    private Object requestTAG;
    private Object requestParams;

    public BitMapRequest(String url, BitMapResponseListner listener, ErrorResponseListener errorListener, Object requestTAG, @Nullable Object requestParams) {
        super(url, listener, 1200, 600, ImageView.ScaleType.CENTER, null, errorListener);
        this.url = url;
        this.responseListener = listener;
        this.requestTAG = requestTAG;
        this.errorResponseListener = errorListener;
        this.requestParams = requestParams;
    }
   /* private BitMapRequest(String url, BitMapResponseListner listener, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, Response.ErrorListener errorListener) {
        super(url, listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);

    }*/

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
    protected Response<Bitmap> parseNetworkResponse(NetworkResponse response) {
        Response<Bitmap> responseObject = super.parseNetworkResponse(response);
        responseListener.getListener().onImageResponseHeaders(response.headers,requestTAG);
        responseListener.getListener().onImageResponseObject(response,responseObject,requestTAG,requestParams);

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
