package com.lxw.ipcdemo.aidl.impl;

import android.os.RemoteException;

import com.lxw.ipcdemo.aidl.ISecurityCenter;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2018/08/08
 *     desc   :
 * </pre>
 */
public class SecurityImpl extends ISecurityCenter.Stub {
    @Override
    public String encrypt(String content) throws RemoteException {
        return null;
    }

    @Override
    public String decrypt(String password) throws RemoteException {
        return null;
    }

}
