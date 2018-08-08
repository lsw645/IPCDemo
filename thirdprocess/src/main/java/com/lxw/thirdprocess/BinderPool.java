package com.lxw.thirdprocess;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.lxw.ipcdemo.aidl.IBinderPool;

import java.util.concurrent.CountDownLatch;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2018/08/08
 *     desc   :
 * </pre>
 */
public class BinderPool {
    public static final String TAG = "BinderPool";
    public static final int BINDER_SECURITY_CENTER = 1;
    public static final int BINDER_COMPUTE = 2;
    private Context mContext;

    private IBinderPool mIBinderPool;
    private static volatile BinderPool instance;
    private CountDownLatch mCountDownLatch;

    private BinderPool(Context context) {
        this.mContext = context.getApplicationContext();
        connectBinderPoolService();
    }

    private void connectBinderPoolService() {
        mCountDownLatch = new CountDownLatch(1);
        Intent intent = new Intent();
        intent.setAction("com.lxw.ipcdemo.BinderPoolService");
        intent.setPackage("com.lxw.ipcdemo");
        mContext.bindService(intent, mBinderPoolConnection, Context.BIND_AUTO_CREATE);
        try {
            System.out.println("等待中");
            mCountDownLatch.await();
            System.out.println("等待结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection mBinderPoolConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println("等待获取");
            mIBinderPool = IBinderPool.Stub.asInterface(service);
//            try {
////                mIBinderPool.asBinder().linkToDeath(mDeathRecipient, 0);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
            System.out.println("等待获取");
            mCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("sdadsadada");
            connectBinderPoolService();
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mIBinderPool != null && mIBinderPool.asBinder().isBinderAlive()) {
                mIBinderPool.asBinder().unlinkToDeath(mDeathRecipient, 0);
                mIBinderPool = null;
            }

            connectBinderPoolService();
        }
    };

    public static BinderPool getInstance(Context context) {
        if (instance == null) {
            synchronized (BinderPool.class) {
                if (instance == null) {
                    instance = new BinderPool(context);
                }
            }
        }
        return instance;
    }


    public IBinder queryBinder(int code) {
        IBinder binder = null;

        try {
            binder = mIBinderPool.queryBinder(code);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }

}
