package app.kwawannan.com.ispacestation.application;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.List;


/**
 * Created by Kwaw Annan on 02/14/2018.
 */

public class ISSApplication extends Application {

    private static final String TAG = ISSApplication.class.getSimpleName();
    private static Context mApplicationcontext;
    private Typeface universeRegular;
    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationcontext = getApplicationContext();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }

    public static Context getAppContext() {
        return mApplicationcontext;
    }
    public static void setAppContext(Context aCxt) {
        mApplicationcontext = aCxt;
    }

    public static void killApplication() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    public static boolean isNetworkUp() {
        try{
            NetworkInfo info = ((ConnectivityManager) getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (info == null || !info.isConnected()) {
                Log.d(TAG,"NetworkCheck Not connected");
                return false;
            }
        }catch (Exception e) {
            Log.w(TAG,"Exception while checking Network Up ", e);
        }
        Log.v(TAG,"NetworkCheck connected");
        return true;
    }

    public static boolean isAppRunning(Context aCxt) {
        ActivityManager activityManager = (ActivityManager) aCxt.getSystemService( ACTIVITY_SERVICE );
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for(int i = 0; i < procInfos.size(); i++)
        {
            if(procInfos.get(i).processName.equals("wm.virtusa.com.weathermap"))
            {
                return true;
            }
        }
        return false;
    }

}
