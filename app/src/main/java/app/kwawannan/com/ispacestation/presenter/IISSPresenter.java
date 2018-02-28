package app.kwawannan.com.ispacestation.presenter;

import android.location.Location;

import app.kwawannan.com.ispacestation.presenter.api.IHttpConnection;

/**
 * Created by Kwaw Annan on 02/14/2018.
 */

public interface IISSPresenter {

    void getPassTime(Location location) ;

    void responseReceived(int status, String body, IHttpConnection.IResponseObserver.RequestTypeEnum aRespType, Object requestParams) ;

}
