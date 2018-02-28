package app.kwawannan.com.ispacestation.utils;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.Date;

/**
 * Created by Kwaw Annan on 02/14/2018.
 */
public class ISSUtils {
    private static ISSUtils mSelf;
    private static String[] permissionNeed = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
    };
    private ISSUtils(){

    }
    public static ISSUtils getInstance() {
        if(mSelf == null) {
            mSelf = new ISSUtils();
        }
        return mSelf;
    }

    public String convertSec2HrMin(int aSec) {
        int hours = aSec / 3600;
        int minutes = (aSec % 3600) / 60;
        int seconds = aSec % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);

    }
    public String convertLong2Date(long aTime) {
        Date date = new Date(aTime * 1000);
        return date.toString();
    }
    public static boolean checkPermissionGranted(Activity activity, String permission)
    {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;

    }

    public static  void checkPermission(Activity activity)
    {
        for (String lPerm : permissionNeed) {
            if (!checkPermissionGranted(activity, lPerm)) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        lPerm)) {

                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.

                } else {

                    // No explanation needed, we can request the permission.

                    ActivityCompat.requestPermissions(activity,
                            permissionNeed,
                            1);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }
    }
}
