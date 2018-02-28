package app.kwawannan.com.ispacestation.presenter.api;

/**
 * Interface for request method and response observer
 * @author Kwaw Annan
 *
 */
public interface IHttpConnection {
	interface IResponseObserver
	{
		int SUCCESS_OK = 200;
		int ACCEPTED = 202;
		int FAILURE_CONNECTION = -1;
		int BAD_REQUEST = 400;
		int UNAUTHORIZED = 401;
		int CREDENTIAL_CHANGE = 402;
		int FORBIDDEN = 403;
		int FILE_NOT_FOUND = 404;
		int INTERNAL_SERVER_ERROR = 500;


		enum RequestTypeEnum {
            GET_CITY_WEATHER_CONDITIONS,
			GET_PASSTIME,
			POST_CONTENT
        }

	       
	}


}
