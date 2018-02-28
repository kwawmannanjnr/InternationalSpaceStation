package app.kwawannan.com.ispacestation.model;

/**
 * Created by Kwaw Annan on 02/14/2018.
 *
 * "request": {
 "altitude": 100,
 "datetime": 1515203526,
 "latitude": 41.05182,
 "longitude": -73.54223,
 "passes": 5
 }

 */

public class PassTimeRequest {
    int altitude;
    double latitude;
    double longitude;
    int passes;
    long datetime;

    public int getAltitude() {
        return altitude;
    }

    public void setAltitude(int altitude) {
        this.altitude = altitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getPasses() {
        return passes;
    }

    public void setPasses(int passes) {
        this.passes = passes;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }


}
