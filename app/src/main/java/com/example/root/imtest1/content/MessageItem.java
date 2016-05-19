package com.example.root.imtest1.content;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 16-4-28.
 */
public class MessageItem implements Parcelable {

    public static final int LEFT_CHAT_MSG = 1;
    public static final int RIGHT_CHAT_MEG = 2;

    //聊天对象的JID
    private String userName;
    private String content;
    private int POSITION;
    public MessageItem(String name, String content, int position) {
        userName = name;
        this.content = content;
        POSITION = position;
    }

    public String getContent() {
        return content;
    }

    public String getUserName() {
        return userName;
    }

    public int getPosition() {
        return POSITION;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userName);
        dest.writeString(content);
        dest.writeInt(POSITION);
    }

    protected MessageItem(Parcel in) {
        userName = in.readString();
        content = in.readString();
        POSITION = in.readInt();
    }

    public static final Creator<MessageItem> CREATOR = new Creator<MessageItem>() {
        @Override
        public MessageItem createFromParcel(Parcel in) {
            return new MessageItem(in);
        }

        @Override
        public MessageItem[] newArray(int size) {
            return new MessageItem[size];
        }
    };
}
