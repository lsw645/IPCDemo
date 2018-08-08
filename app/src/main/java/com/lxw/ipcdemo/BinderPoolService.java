package com.lxw.ipcdemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lxw.ipcdemo.aidl.impl.BinderPoolImpl;

public class BinderPoolService extends Service {
    public BinderPoolService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("服务班定");
        return new BinderPoolImpl();
    }
}
