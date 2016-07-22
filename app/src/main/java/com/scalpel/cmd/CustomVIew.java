package com.scalpel.cmd;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Author: Mrchen on 16/1/14.
 */
public class CustomVIew implements Parcelable {
    public int num;
    public String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.num);
        dest.writeString(this.name);
    }

    public CustomVIew() {
    }

    protected CustomVIew(Parcel in) {
        this.num = in.readInt();
        this.name = in.readString();
    }

    public static final Creator<CustomVIew> CREATOR = new Creator<CustomVIew>() {
        public CustomVIew createFromParcel(Parcel source) {
            return new CustomVIew(source);
        }

        public CustomVIew[] newArray(int size) {
            return new CustomVIew[size];
        }
    };
}
