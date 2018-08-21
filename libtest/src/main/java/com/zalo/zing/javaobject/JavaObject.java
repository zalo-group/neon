package com.zalo.zing.javaobject;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class JavaObject implements Serializable {

    // Object

    public JavaOne mFirstValue;
    public JavaTwo mSecondValue;
    public String status;

    // Array

    public JavaOne[] mArrayPet;
    public JavaTwo[] mArrayCats;

    // Primitive Array

    public int[] mSize;
    public float[] mPoint;
    public double[] mSeconds;
    public boolean[] mSizeWrong;
}
