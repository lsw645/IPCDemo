package com.lxw.thirdprocess;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.lxw.ipcdemo.aidl.Book;
import com.lxw.ipcdemo.aidl.IBookManager;
import com.lxw.ipcdemo.aidl.ICompute;
import com.lxw.ipcdemo.aidl.IOnNewBookArrivedListener;
import com.lxw.ipcdemo.aidl.ISecurityCenter;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private IBookManager mIBookManager;
    BinderPool instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //一定要使用子线程进行加载binder复用池，因为内部使用了CountdownLatch
        //会导致主线程阻塞，而onServiceConnection中的回调是主线程的。所以导致binder
        //连接成功后，无法回到主线程
        new Thread() {
            @Override
            public void run() {
                instance = BinderPool.getInstance(MainActivity.this);
            }
        }.start();
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindService();
            }
        });
        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IBinder binder = instance.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
                ISecurityCenter iSecurityCenter = ISecurityCenter.Stub.asInterface(binder);
                try {
                    iSecurityCenter.decrypt("123");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        instance = BinderPool.getInstance(MainActivity.this);
                        IBinder binder = instance.queryBinder(BinderPool.BINDER_COMPUTE);
                        ICompute iCompute = ICompute.Stub.asInterface(binder);
                        try {
                            int add = iCompute.add(1, 2);
                            Toast.makeText(MainActivity.this, "asda" + add, Toast.LENGTH_SHORT).show();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
            }
        });
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setAction("com.lxw.ipcdemo.IPCService");

        intent.setPackage("com.lxw.ipcdemo");

        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBookManager = IBookManager.Stub.asInterface(service);
//                        mIBookManager.asBinder().linkToDeath(mDeathRecipient,0);
            try {
                List<Book> bookList = mIBookManager.getBookList();
                mIBookManager.registerListener(mListener);
                System.out.println(mListener);
                printBookList(bookList);
                mIBookManager.addBook(new Book(1, "西游记"));

//                            printBookList(mIBookManager.getBookList());

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            System.out.println("onServiceDisconnected " + name);
            bindService();
        }
    };

    private void printBookList(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    private IOnNewBookArrivedListener mListener = new IOnNewBookArrivedListener.Stub() {
        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            System.out.println(book.toString());
        }
    };

    //监控 远程binder突然cash
    // 方案一 ， 使用IBinder.DeathRecipient
    // 方案二 是onServiceDisconnected 中 进行重连操作
    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mIBookManager != null) {
                mIBookManager.asBinder().unlinkToDeath(mDeathRecipient, 0);
            }
        }
    };

    @Override
    protected void onDestroy() {
        if (mIBookManager != null && mIBookManager.asBinder().isBinderAlive()) {
            try {
                mIBookManager.unregisterListener(mListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mServiceConnection);

        super.onDestroy();
    }
}
