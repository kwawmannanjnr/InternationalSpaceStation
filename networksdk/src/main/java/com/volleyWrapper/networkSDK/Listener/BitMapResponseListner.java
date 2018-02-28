package com.volleyWrapper.networkSDK.Listener;

import android.graphics.Bitmap;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;

import java.util.Map;

/**
 * Created by Bikash on 1/4/2018.
 */
public class BitMapResponseListner implements Response.Listener<Bitmap> {
    private BitMapListener listener;

    public BitMapResponseListner(BitMapListener aListner) {
            this.listener = aListner;

    }
    @Override
    public void onResponse(Bitmap response) {

    }
    public BitMapListener getListener()
    {
        return listener;
    }
    public interface BitMapListener
    {
        void onImageResponse(String response);
        void onImageResponseHeaders(Map<String,String> headers, Object requestTAG);
        void onImageResponseObject(NetworkResponse response, Response<Bitmap> responseObject, Object requestTAG, Object requestParams);
    }
}
