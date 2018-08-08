// IOnNewBookArrivedListener.aidl
package com.lxw.ipcdemo.aidl;
import com.lxw.ipcdemo.aidl.Book;
// Declare any non-default types here with import statements

interface IOnNewBookArrivedListener {
    void onNewBookArrived(in Book book);
}
