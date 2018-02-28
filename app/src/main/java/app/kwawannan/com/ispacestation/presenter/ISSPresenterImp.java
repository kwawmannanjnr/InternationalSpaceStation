package app.kwawannan.com.ispacestation.presenter;

import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Vector;

import app.kwawannan.com.ispacestation.application.ISSApplication;
import app.kwawannan.com.ispacestation.model.SpaceStationData;
import app.kwawannan.com.ispacestation.presenter.api.IHttpConnection;
import app.kwawannan.com.ispacestation.presenter.api.KeyName;
import app.kwawannan.com.ispacestation.view.IISSView;


/**
 * Created by Kwaw Annan on 02/14/2018.
 */

public class ISSPresenterImp implements IISSPresenter{

    private static final String TAG = ISSPresenterImp.class.getSimpleName();
    private static ISSPresenterImp mSelf = null;
    private IISSView mView ;

    public ISSPresenterImp(IISSView aView) {
        this.mView = aView;
    }

   /* public static  ApiCallInterface getInstance() {
        if(mSelf  == null) {
            mSelf = new ApiCallInterface();
        }
        return mSelf;
    }*/

    @Override
    public synchronized void getPassTime(Location location) {
        Log.d(TAG,"getWeatherContent()");
        GatewayController gatewayController = GatewayController.getInstance(ISSApplication.getAppContext() , this);
        HashMap<String, String> params = new HashMap<>();
        params.put(KeyName.LATITUDE , ""+location.getLatitude());
        params.put(KeyName.LONGITUDE , ""+location.getLongitude());
        gatewayController.processNetworkRequest(IHttpConnection.IResponseObserver.RequestTypeEnum.GET_PASSTIME,params, Request.Priority.IMMEDIATE);
    }

    @Override
    public synchronized  void responseReceived(int status, String body, IHttpConnection.IResponseObserver.RequestTypeEnum aRespType, Object requestParams) {

        Log.d(TAG, "response received resType is " + aRespType + " status " + status + " body message received =" + body);
        Log.d(TAG, "response received resType is " + aRespType + " status " + status + " body message received =" + body);
        switch (aRespType) {
            case GET_CITY_WEATHER_CONDITIONS:
                switch (status) {
                    case ResponseCodeConstants.FAILURE_CONNECTION:
                        Log.d(TAG, "GET_CITY_WEATHER_CONDITIONS responseReceived()-->FAILURE_CONNECTION");
//                        break;
                    case ResponseCodeConstants.INTERNAL_SERVER_ERROR:
                    case ResponseCodeConstants.FORBIDDEN:
                        Log.d(TAG, "GET_CITY_WEATHER_CONDITIONS responseReceived()-->INTERNAL_SERVER_ERROR");
//                        break;
                    case ResponseCodeConstants.UNAUTHORIZED:
                        Log.d(TAG, "AuthRequestCheck responseReceived()-->UNAUTHORIZED");
//                        break;
                    case ResponseCodeConstants.SUCCESS_OK:
                    case ResponseCodeConstants.NOT_MODIFIED:
                        parseResponse(body,aRespType, requestParams);
                        break;
                    default:
                        Log.d(TAG, "AuthRequestCheck responseReceived()-->default");
                        break;
                }

                break;
            case POST_CONTENT:
                break;
            case GET_PASSTIME:
                switch (status) {
                    case ResponseCodeConstants.SUCCESS_OK:
                        parseResponse(body,aRespType, requestParams);
                        break;
                }
                break;
        }
    }


    public  void parseResponse(String aResponse, IHttpConnection.IResponseObserver.RequestTypeEnum aRespType, Object aRequestParam) {

        Log.d(TAG , "parseResponse() - response " +aResponse);
        Gson lGson = new Gson();
        try {
            SpaceStationData issDetails = lGson.fromJson(aResponse, SpaceStationData.class);
//            Log.d(TAG , "weather Data " + issDetails.getName()  + "  " +issDetails.getId());
            if( mView!= null) {
                Vector<SpaceStationData> lData = new Vector<SpaceStationData>();
                lData.add(issDetails);
                mView.notifyDataChange(lData);
            }
        }catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }

    }
}
