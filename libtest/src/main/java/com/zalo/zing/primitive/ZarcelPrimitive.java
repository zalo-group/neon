package com.zalo.zing.primitive;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelPrimitive implements Serializable {

    public int mCats;

    public int mDogs;

    public float mRadius;

    public double mTimer;

    public String mName;

    public boolean mWrong;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelPrimitive$Zarcel.serialize(this, serializedOutput);
    }

    public static Serializable.Creator<ZarcelPrimitive> CREATOR = new Creator<ZarcelPrimitive>() {
        @Nullable
        @Override
        public ZarcelPrimitive createFromSerialized(SerializedInput input) {
            try {
                ZarcelPrimitive result = new ZarcelPrimitive();
                ZarcelPrimitive$Zarcel.createFromSerialized(result, input);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
