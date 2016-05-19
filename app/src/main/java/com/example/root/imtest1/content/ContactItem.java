package com.example.root.imtest1.content;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 16-4-28.
 */
public class ContactItem implements Parcelable {

    private String mName, mJid;
    private int mPhoto;   //资源ID
    private String mGroupName;

    public ContactItem(String name, String jid, int photo, String groupname) {
        this.mName = name;
        this.mJid = jid;
        this.mPhoto = photo;
        this.mGroupName = groupname;
    }

    protected ContactItem(Parcel in) {
        mName = in.readString();
        mJid = in.readString();
        mPhoto = in.readInt();
        mGroupName = in.readString();
    }

    //读取接口，目的是要从Parcel中构造一个实现了Parcelable的类的实例处理。因为实现类在这里还是不可知的，所以需要用到模板的方式，继承类名通过模板参数传入
    //为了能够实现模板参数的传入，这里定义Creator嵌入接口,内含两个接口函数分别返回单个和多个继承类实例
    //需重写本接口中的两个方法：createFromParcel(Parcel in) 实现从Parcel容器中读取传递数据值，
    // 封装成Parcelable对象返回逻辑层，newArray(int size) 创建一个类型为T，长度为size的数组，
    // 仅一句话即可（return new T[size]），供外部类反序列化本类数组使用。
    //因此写的顺序和读的顺序必须一致
    public static final Creator<ContactItem> CREATOR = new Creator<ContactItem>() {
        @Override
        public ContactItem createFromParcel(Parcel in) {

            return new ContactItem(in);
        }

        @Override
        public ContactItem[] newArray(int size) {
            return new ContactItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mJid);
        dest.writeInt(mPhoto);
        dest.writeString(mGroupName);
    }

    public int getPhoto() {
        return mPhoto;
    }

    public String getName() {
        return mName;
    }

    public String getJid() {
        return mJid;
    }

    public String getGroupName() {
        return mGroupName;
    }
}
