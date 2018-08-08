package com.lxw.ipcdemo.aidl.impl;

import android.os.RemoteException;

import com.lxw.ipcdemo.aidl.ICompute;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2018/08/08
 *     desc   :
 * </pre>
 */
public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }

}
