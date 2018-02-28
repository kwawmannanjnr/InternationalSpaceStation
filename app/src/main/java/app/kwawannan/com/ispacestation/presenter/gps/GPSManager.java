package app.kwawannan.com.ispacestation.presenter.gps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;

import app.kwawannan.com.ispacestation.view.ILocationChanged;


/**
 * Created by Kwaw Annan on 02/14/2018.
 */

public class GPSManager extends Service implements LocationListener {

    private final Activity mContext;

    // flag for GPS status
    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    private String provider;

    // flag for GPS status
    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude

    // The minimum distance to change Updates in meters . 30 meter to reduce polling to avoid battery drain
    public static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 30; // 30 meters

    // The minimum time between updates in milliseconds
    public static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 3 minute to avoid battery drain
    public static final long FASTEST_INTERVAL = 1000 * 10 * 1; // 30sec to avoid battery drain
    private static String permissionNeed =
            "Manifest.permission.ACCESS_FINE_LOCATION"
    ;
    private static final int REQUEST_CODE_PERMISSION = 2;
    // Declaring a Location Manager
    protected LocationManager locationManager;

    private static  ILocationChanged mLocaChangedListner;
    public static void registerListner(ILocationChanged aLocationChanged){
        mLocaChangedListner = aLocationChanged;
    }

    public GPSManager(Activity context) {
        this.mContext = context;
        getLocation();
    }


    public Location getLocation() {
     /*   if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( mContext, android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            Toast.makeText(mContext,"Permission is not granted so returning back ",Toast.LENGTH_LONG).show();
//            showSettingsAlert();
            return null ;
        }*/

        try {

            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            if(provider != null) {
                Location location = locationManager.getLastKnownLocation(provider);

                // Initialize the location fields
                if (location != null) {
                    System.out.println("Provider " + provider + " has been selected.");
                    onLocationChanged(location);
                    resumeGpsService();
                }
            }else {
//                ISSUtils.checkPermission(mContext);
            }


        }catch (SecurityException secx) {
            secx.printStackTrace();
//            ISSUtils.checkPermission(mContext);
            showSettingsAlert();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */

    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPSManager.this);
        }
    }

    public void resumeGpsService() {
        if(locationManager != null) {
            try {
                locationManager.requestLocationUpdates(provider, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
            }catch (SecurityException secx) {
                showSettingsAlert();
                secx.printStackTrace();
            }catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    /**
     * Function to get latitude
     * */

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        // return latitude
        return latitude;
    }

    /**
     * Function to get longitude
     * */

    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        // return longitude
        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     * @return boolean
     * */

    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will lauch Settings Options
     * */

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {

        if(mLocaChangedListner != null) {
            mLocaChangedListner.onLocationChanged(location);
        }

//        Toast.makeText(mContext , "Onlocation Changed called" , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
}
