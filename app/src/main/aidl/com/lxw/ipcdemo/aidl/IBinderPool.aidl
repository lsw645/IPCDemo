// IBinder.aidl
package com.lxw.ipcdemo.aidl;

// Declare any non-default types here with import statements

interface IBinderPool {
    IBinder queryBinder(int binderCode);
}
