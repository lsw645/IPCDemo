package com.lxw.ipcdemo;

import android.app.ActivityManager;
import android.app.Application;
import android.os.Process;
import android.util.Log;

import java.util.List;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2018/08/07
 *     desc   :
 * </pre>
 */
public class App extends Application {
    private static final String TAG = "IPCAPP";

    @Override
    public void onCreate() {
        super.onCreate();
        int pid = Process.myPid();
        String processName = "";
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : list){
            if (runningAppProcessInfo.pid == pid){
                processName = runningAppProcessInfo.processName;
            }
        }
        System.out.println("sdadsa");
        Log.d(TAG, "App start returned: " + processName);
    }
}
