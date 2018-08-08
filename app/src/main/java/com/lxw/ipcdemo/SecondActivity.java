package com.lxw.ipcdemo;

import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;

import com.lxw.ipcdemo.aidl.BinderPool;
import com.lxw.ipcdemo.aidl.Book;
import com.lxw.ipcdemo.aidl.impl.BinderPoolImpl;

public class SecondActivity extends AppCompatActivity {
    private Book mBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        BinderPool instance = BinderPool.getInstance(this);
        IBinder binder = instance.queryBinder(BinderPoolImpl.BINDER_COMPUTE);


    }


}
