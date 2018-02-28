package app.kwawannan.com.ispacestation.presenter;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.volleyWrapper.networkSDK.JobQueue.PriorityJobQueue;
import com.volleyWrapper.networkSDK.Listener.ErrorResponseListener;
import com.volleyWrapper.networkSDK.Listener.ResponseListener;
import com.volleyWrapper.networkSDK.Network.Constants;
import com.volleyWrapper.networkSDK.Network.GetRequest;
import com.volleyWrapper.networkSDK.Network.NetworkHandler;
import com.volleyWrapper.networkSDK.Network.PostRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.kwawannan.com.ispacestation.application.AppConstant;
import app.kwawannan.com.ispacestation.presenter.api.IHttpConnection;
import app.kwawannan.com.ispacestation.presenter.api.KeyName;


/**
 * Created by Kwaw Annan on 02/14/2018.
 */

public class GatewayController implements ResponseListener.Listener,ErrorResponseListener.ErrorListener{
    private static GatewayController instance = null;
    private IISSPresenter presenter;
    private Context mContext;
    /**
     * Network handler instance for network calls defined on Network sdk module.
     */
    private NetworkHandler networkHandler;
    private PriorityJobQueue priorityJobQueue;
    private IHttpConnection.IResponseObserver.RequestTypeEnum mResponseType;
    private String TAG = GatewayController.class.getName();

    private GatewayController(Context aCxt , IISSPresenter presenter) {
        this.mContext = aCxt;
        this.presenter = presenter;
        this.networkHandler = NetworkHandler.getInstance(mContext);
        this.priorityJobQueue = networkHandler.getJobQueue();
        Log.d(TAG, "Inside GatewayController ()");
    }
    public static GatewayController getInstance(Context mContext , IISSPresenter presenter) {
        if (instance == null) instance = new GatewayController(mContext , presenter);
        return instance;
    }

    //Methods to process the network request

    public void processNetworkRequest(IHttpConnection.IResponseObserver.RequestTypeEnum mResponseType, Object requestParams, Request.Priority priority) {
        this.mResponseType = mResponseType;
        switch (mResponseType) {
            case POST_CONTENT:
                Log.v(TAG, "LOGIN_AUTH_SERVICE processNetworkRequest()-->");
                postRequest(requestParams, mResponseType, priority);
                break;
            case GET_PASSTIME:
                Log.v(TAG, "Passtime processNetworkRequest()-->");
                getPassTime(requestParams, mResponseType, priority);
                break;

        }
    }

    private void getPassTime(Object requestParams, IHttpConnection.IResponseObserver.RequestTypeEnum mResponseType, Request.Priority priority) {
        HashMap<String, String> params = (HashMap<String, String>) requestParams;
        Log.v(TAG, "getWeatherContent() Request-->");

        Log.d(TAG, "Request is about to get the Contents  = ");
        //Hard coded URL , It can be put on config file
        String lUrl = AppConstant.PASSTIME_URI + params.get(KeyName.LATITUDE) +"&lon="+ params.get(KeyName.LONGITUDE) +"";


        GetRequest getSanboxdata = (GetRequest) networkHandler.getRequestObject(Constants.NetworkRequestType.GET, mResponseType, lUrl, new ResponseListener(this), new ErrorResponseListener(this), requestParams);
        Log.d(TAG, "List Data Request() ->  Url=" + lUrl);
        getSanboxdata.setPriority(priority);
        getSanboxdata.setRetryPolicy(new DefaultRetryPolicy(AppConstants.SOCKET_TIMEOUT_TIME, AppConstants.RE_TRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        priorityJobQueue.addToRequestQueue(getSanboxdata, TagConstants.TAG_CONTENT_RESPONSE_TYPE);
    }


    /**
     * Post Method added for Demonstration
     * @param requestParams
     * @param mResponseType
     * @param priority
     */
    private void postRequest(Object requestParams, IHttpConnection.IResponseObserver.RequestTypeEnum mResponseType, Request.Priority priority) {
        Log.v(TAG, "postRequestpostRequest()-->");
        if (requestParams instanceof HashMap) {
            Log.v(TAG, "postRequest postRequest()-->requestParams check");
            HashMap<String, String> params = (HashMap<String, String>) requestParams;
            //TODO write the logic to make network call
            JSONObject luserRegistrationdata = new JSONObject();

            String lUrl = "http://lyrics.wikia.com/api.php?func=getSong&artist=Tom+Waits&song=new+coat+of+paint&fmt=json";
            // String lUrl = "https://user-auth-service-uat.cfapps.scus-10.test.cf.fedex.com/v1/user/auth";
            int requestType = Constants.NetworkRequestType.POST;
            PostRequest authRequest = (PostRequest) networkHandler.getRequestObject(requestType, mResponseType, lUrl, new ResponseListener(this), new ErrorResponseListener(this), requestParams);
            authRequest.addHeader("Content-Type", "application/json");
            authRequest.addHeader("X-locale", "en_US"); // As per working param
            authRequest.addHeader("X-version", "1");
            authRequest.setRequestBody(luserRegistrationdata.toString());
            authRequest.setEncodingType("utf-8");
            authRequest.setPriority(priority);
            authRequest.setRetryPolicy(new DefaultRetryPolicy(AppConstants.SOCKET_TIMEOUT_TIME, AppConstants.RE_TRY_COUNT, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            priorityJobQueue.addToRequestQueue(authRequest, TagConstants.TAG_POST_RESPONSE_TYPE);
        }
    }


    @Override
    public void onResponse(String response) {

    }

    @Override
    public void onResponseHeaders(Map<String, String> headers, Object requestTAG) {

    }

    @Override
    public void onResponseObject(NetworkResponse response, Response<String> responseObject, Object requestTAG, Object requestParams) {

        Log.v(TAG, "onResponseObject()-->pre");
        IHttpConnection.IResponseObserver.RequestTypeEnum mResponseTypeFromRequest = (IHttpConnection.IResponseObserver.RequestTypeEnum) requestTAG;
        //Reset if response received
        presenter.responseReceived(response.statusCode, responseObject.result, mResponseTypeFromRequest, requestParams);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onErrorResponse(VolleyError error, Object requestTag) {

    }

    @Override
    public void onErrorResponse(VolleyError error, NetworkResponse response, Object requestTAG, Object requestParams) {
        IHttpConnection.IResponseObserver.RequestTypeEnum mResponseTypeFromRequest = (IHttpConnection.IResponseObserver.RequestTypeEnum) requestTAG;
        try {

            if (response == null || (response != null && (Integer) response.statusCode == null)) {
                presenter.responseReceived(-1, null, mResponseTypeFromRequest, requestParams);
            } else {
                presenter.responseReceived(response.statusCode, null, mResponseTypeFromRequest, requestParams);
            }
        } catch (Exception ex) {
            presenter.responseReceived(-1, null, mResponseTypeFromRequest, requestParams);
            ex.printStackTrace();
            Log.e(TAG, "error==" + ex.toString());
        }
    }
}
