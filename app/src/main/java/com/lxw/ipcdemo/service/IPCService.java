package com.lxw.ipcdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.lxw.ipcdemo.aidl.Book;
import com.lxw.ipcdemo.aidl.IBookManager;
import com.lxw.ipcdemo.aidl.IOnNewBookArrivedListener;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class IPCService extends Service {
    private static final String TAG = "IPCService";

    public IPCService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        int check = checkCallingOrSelfPermission("com.lxw.ipcdemo.service.IPCService");
//        if (check == PackageManager.PERMISSION_DENIED) {
//            return null;
//        }
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        return mStub;
    }

    private CopyOnWriteArrayList<Book> mList = new CopyOnWriteArrayList<Book>();
    //    private List<IOnNewBookArrivedListener> mListeners = new ArrayList<>();
    private RemoteCallbackList<IOnNewBookArrivedListener> mCallbackList = new RemoteCallbackList<>();
    private int count = 10;

    @Override
    public void onCreate() {
        super.onCreate();
        mList.add(new Book(1, "红孩儿"));
        mList.add(new Book(2, "空调有问题"));
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    mList.add(new Book(count++, "haha" + count));
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    IBookManager.Stub mStub = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            return mList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mList.add(book);
//            for (IOnNewBookArrivedListener listener : mCallbackList) {
//                listener.onNewBookArrived(book);
//            }
            int count = mCallbackList.beginBroadcast();
            for (int i = 0; i < count; i++) {
                mCallbackList.getBroadcastItem(i).onNewBookArrived(book);
            }
        }

        @Override
        public void registerListener(IOnNewBookArrivedListener listener) throws RemoteException {
            mCallbackList.register(listener);
        }

        @Override
        public void unregisterListener(IOnNewBookArrivedListener listener) throws RemoteException {
            Log.d(TAG, "unregisterListener() returned: " + listener.toString());
            mCallbackList.unregister(listener);
        }
    };
}
