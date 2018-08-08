package com.lxw.ipcdemo.test;

import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

import com.lxw.ipcdemo.aidl.Book;

import java.util.List;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2018/08/07
 *     desc   :
 * </pre>
 */
public interface IBookManager extends IInterface {
    static final String DESCRIPTOR = "com.lxw.ipcdemo.test.IBookManager";
    int TRANSACTION_getBookList = IBinder.FIRST_CALL_TRANSACTION + 0;
    int TRANSACTION_addBook = IBinder.FIRST_CALL_TRANSACTION + 1;

    List<Book> getBookList() throws RemoteException;

    void addBook(Book book) throws RemoteException;
}
