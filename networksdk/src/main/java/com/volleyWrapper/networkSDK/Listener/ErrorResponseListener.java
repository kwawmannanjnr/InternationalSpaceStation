package com.volleyWrapper.networkSDK.Listener;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.Map;

/**
 * Class ErrorResponseListener is ItemClickListener for the GrowingListView
 *
 * @author  Bikash Kumar Mohanty
 * @version 1.0
 * @since   2018-8-1
 */
public class ErrorResponseListener implements Response.ErrorListener {

    /**
     * @param listener The interface given to the implentation class to give the error response
     * @see ErrorListener
     */

    private ErrorListener listener;

    /**
     * public constructor for ErrorResponseListener
     * @param listener The ErrorListener inteface object
     * @see ErrorListener
     */

    public ErrorResponseListener(ErrorListener listener)
    {
        this.listener = listener;

    }
    /**
     * Method Implementation to get the ErrorListener Object
     * @return ErrorListener This returns ErrorListener.
     * @see ErrorListener
     */
    public ErrorListener getListener()
    {
        return listener;
    }

    /**
     * Method Implementation of the Voller Error response interface method
     * @return Void returns nothing
     * @see Response.ErrorListener
     */

    @Override
    public void onErrorResponse(VolleyError error) {

        //onErrorResponse(error);
    }

    /**
     * Interface ErrorListener is the ErrorListener to get the error after response from network
     * @author  Bikash Kumar Mohanty
     * @version 1.0
     * @since   2016-07-18
     */
    public interface ErrorListener
    {
        /**
         * Method abstraction for providing error response
         * @param error The Volley error input
         * @return Void returns nothing
         *
         * */

        void onErrorResponse(VolleyError error);

        /**
         * Method abstraction for providing error response
         * @param error The Volley error input
         * @param requestTag The request tag
         * @return Void returns nothing
         *
         * */
        void onErrorResponse(VolleyError error,Object requestTag);

        /**
         * Method abstraction for providing error response
         * @param error The Volley error input
         * @param response Response Object from Volley
         * @param requestTAG The request tag
         * @param requestParams The request parameters that are passed to request.
         * @return Void returns nothing
         *
         * */
        void onErrorResponse(VolleyError error,NetworkResponse response, Object requestTAG,Object requestParams);
    }
}
