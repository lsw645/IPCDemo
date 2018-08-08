package com.lxw.ipcdemo.aidl.impl;

import android.os.IBinder;
import android.os.RemoteException;

import com.lxw.ipcdemo.aidl.IBinderPool;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2018/08/08
 *     desc   :
 * </pre>
 */
public class BinderPoolImpl extends IBinderPool.Stub {
    public static final int BINDER_SECURITY_CENTER = 1;
    public static final int BINDER_COMPUTE = 2;

    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
        IBinder binder = null;
        switch (binderCode) {
            case BINDER_SECURITY_CENTER:
                binder = new SecurityImpl();
                break;
            case BINDER_COMPUTE:
                binder = new ComputeImpl();
                break;
            default:
                break;
        }

        return binder;
    }
}
