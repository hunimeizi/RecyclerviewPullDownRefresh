package com.recyclerviewpulldownrefresh.ui.recyclerviewpulldownrefresh;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderBean implements Parcelable {

    private String a, b;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.a);
        dest.writeString(this.b);
    }

    public OrderBean() {
    }

    protected OrderBean(Parcel in) {
        this.a = in.readString();
        this.b = in.readString();
    }

    public static final Parcelable.Creator<OrderBean> CREATOR = new Parcelable.Creator<OrderBean>() {
        public OrderBean createFromParcel(Parcel source) {
            return new OrderBean(source);
        }

        public OrderBean[] newArray(int size) {
            return new OrderBean[size];
        }
    };
}
