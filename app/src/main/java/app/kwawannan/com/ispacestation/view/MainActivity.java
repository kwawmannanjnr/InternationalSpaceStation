package app.kwawannan.com.ispacestation.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import app.kwawannan.com.ispacestation.R;
import app.kwawannan.com.ispacestation.presenter.gps.GPSManager;
import app.kwawannan.com.ispacestation.presenter.gps.GPSService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 200;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 201;

    private GPSManager gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        LocationManager.getInstance(this).init();
//        ISSUtils.checkPermission( this);
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();

        scheduler.scheduleAtFixedRate
                (new Runnable() {
                    public void run() {
                        // call service
                        bindService();
                    }
                }, 0, 10, TimeUnit.MINUTES);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        loadISSFragment();

        // create class object
        gps = new GPSManager(MainActivity.this);



    }

    @Override
    protected void onResume() {
        super.onResume();
        googleplayService();
        if( gps != null) {
            if(checkPermissions()) {
//                gps.getLocation();
                gps.resumeGpsService();
            }else if (!checkPermissions()) {
                requestPermissions();

            }

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
       if( gps != null) {
            gps.stopUsingGPS();
        }
    }

    /**

     * Return the current state of the permissions needed.

     */

    private boolean checkPermissions() {

        int permissionState = ActivityCompat.checkSelfPermission(this,

                Manifest.permission.ACCESS_FINE_LOCATION);

        return permissionState == PackageManager.PERMISSION_GRANTED;

    }



    private void requestPermissions() {

        boolean shouldProvideRationale =

                ActivityCompat.shouldShowRequestPermissionRationale(this,

                        Manifest.permission.ACCESS_FINE_LOCATION);



        // Provide an additional rationale to the user. This would happen if the user denied the

        // request previously, but didn't check the "Don't ask again" checkbox.

        if (shouldProvideRationale) {

            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale,

                    android.R.string.ok, new View.OnClickListener() {

                        @Override

                        public void onClick(View view) {

                            // Request permission

                            ActivityCompat.requestPermissions(MainActivity.this,

                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},

                                    REQUEST_PERMISSIONS_REQUEST_CODE);

                        }

                    });

        } else {

            Log.i(TAG, "Requesting permission");

            // Request permission. It's possible this can be auto answered if device policy

            // sets the permission in a given state or the user denied the permission

            // previously and checked "Never ask again".

            ActivityCompat.requestPermissions(MainActivity.this,

                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},

                    REQUEST_PERMISSIONS_REQUEST_CODE);

        }

    }
    /**

     * Shows a {@link Snackbar}.

     *

     * @param mainTextStringId The id for the string resource for the Snackbar text.

     * @param actionStringId   The text of the action item.

     * @param listener         The listener associated with the Snackbar action.

     */

    private void showSnackbar(final int mainTextStringId, final int actionStringId,

                              View.OnClickListener listener) {

        Snackbar.make(

                findViewById(android.R.id.content),

                getString(mainTextStringId),

                Snackbar.LENGTH_INDEFINITE)

                .setAction(getString(actionStringId), listener).show();

    }




    /**

     * Callback received when a permissions request has been completed.

     */

    @Override

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,

                                           @NonNull int[] grantResults) {

        Log.i(TAG, "onRequestPermissionResult");

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {

            if (grantResults.length <= 0) {

                // If user interaction was interrupted, the permission request is cancelled and you

                // receive empty arrays.

                Log.i(TAG, "User interaction was cancelled.");

            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.i(TAG, "Permission granted, updates requested, starting location updates");

                    gps = new GPSManager(this);

            } else {

                // Permission denied.


                // Notify the user via a SnackBar that they have rejected a core permission for the

                // app, which makes the Activity useless. In a real app, core permissions would

                // typically be best requested during a welcome-screen flow.


                // Additionally, it is important to remember that a permission might have been

                // rejected without asking the user for permission (device policy or "Never ask

                // again" prompts). Therefore, a user interface affordance is typically implemented

                // when permissions are denied. Otherwise, your app could appear unresponsive to

                // touches or interactions which have required permissions.

                showSnackbar(R.string.permission_denied_explanation,
                        R.string.action_settings, new View.OnClickListener() {

                            @Override

                            public void onClick(View view) {

                                // Build intent that displays the App settings screen.

                                Intent intent = new Intent();

                                intent.setAction(

                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);

                                Uri uri = Uri.fromParts("package",

                                        BuildConfig.APPLICATION_ID, null);

                                intent.setData(uri);

                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                startActivity(intent);

                            }

                        });


            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void bindService(){

        Log.d(TAG, "------- starting service ----");
        startService(new Intent(getApplicationContext(),GPSService.class));
        Log.d(TAG, "------- Binding service ----");
    }


    private void googleplayService() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);

        if(result != ConnectionResult.SUCCESS) {
           /* if(googleAPI.isUserResolvableError(result)) {
                //prompt the dialog to update google play
                googleAPI.getErrorDialog(this,result,PLAY_SERVICES_RESOLUTION_REQUEST).show();

            }*/
           //Few devices are not updating with playservice so Gps Manager will fetch the location for those devices.
           gps = new GPSManager(this);

        }
        else{
            //google play up to date
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    private void loadISSFragment()
    {
        ISSFragment issFragment= new ISSFragment();
        String tag = "ISSFragment";
        FragmentManager mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.container_iss_activity, issFragment, tag).commit();
        Log.d(TAG, "loadISSFragment() -> All Layout loaded ");
    }
}
