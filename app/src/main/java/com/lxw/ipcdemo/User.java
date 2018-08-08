package com.lxw.ipcdemo;

import android.os.Parcel;
import android.os.Parcelable;

import com.lxw.ipcdemo.aidl.Book;

/**
 * <pre>
 *     author : lxw
 *     e-mail : lsw@tairunmh.com
 *     time   : 2018/08/07
 *     desc   :
 * </pre>
 */
public class User implements Parcelable{

    public int userId;

    public String userName;

    public boolean isMale;

    public Book book;


    protected User(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
        isMale = in.readByte() != 0;
        book = in.readParcelable(Book.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeByte((byte) (isMale ? 1 : 0));
        dest.writeParcelable(book, flags);
    }
}
