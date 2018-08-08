package com.lxw.ipcdemo.test;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
public abstract class BookManagerImpl extends Binder implements IBookManager {

    public BookManagerImpl() {
        this.attachInterface(this, DESCRIPTOR);
    }

    public static IBookManager asInterface(IBinder obj) {
        if (obj == null) {
            return null;
        }
        IInterface iin = obj.queryLocalInterface(DESCRIPTOR);

        if (iin != null && (iin instanceof IBookManager)) {
            //返回当前进程的IBookManager
            return (IBookManager) iin;
        }
        return new BookManagerImpl.Proxy(obj);
    }


    @Override
    public IBinder asBinder() {
        return this;
    }

    @Override
    protected boolean onTransact(int code, @NonNull Parcel data, @Nullable Parcel reply, int flags) throws RemoteException {
        switch (code) {
            case INTERFACE_TRANSACTION:
                reply.writeString(DESCRIPTOR);
                return true;
            case TRANSACTION_getBookList:
                data.enforceInterface(DESCRIPTOR);
                List<Book> books = this.getBookList();
                reply.writeNoException();
                reply.writeTypedList(books);
                return true;
            case TRANSACTION_addBook:
                data.enforceInterface(DESCRIPTOR);
                Book arg0;
                if (0 != data.readInt()) {
                    // 反序列化 重新new
                    arg0 = Book.CREATOR.createFromParcel(data);
                } else {
                    arg0 = null;
                }
                this.addBook(arg0);
                reply.writeNoException();
                return true;
            default:
                break;

        }

        return super.onTransact(code, data, reply, flags);
    }

    private static class Proxy implements IBookManager {
        private IBinder mRemote;

        public Proxy(IBinder remote) {
            mRemote = remote;
        }

        @Override
        public List<Book> getBookList() throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            List<Book> result;
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                mRemote.transact(TRANSACTION_getBookList, data, reply, 0);
                reply.readException();
                result = this.getBookList();
            } finally {
                data.recycle();
                reply.recycle();
            }
            return result;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                data.writeInterfaceToken(DESCRIPTOR);
                if (book != null) {
                    data.writeInt(1);
                    data.writeParcelable(book, 0);
                } else {
                    data.writeInt(0);
                }
                mRemote.transact(TRANSACTION_addBook, data, reply, 0);

            } finally {
                data.recycle();
                reply.recycle();
            }

        }

        @Override
        public IBinder asBinder() {
            return mRemote;
        }

        public String getInterfaceDescriptor() {
            return DESCRIPTOR;
        }


    }
}
