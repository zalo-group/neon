package com.zalo.zing.primitive;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Ignore;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.DebugBuilder;
import com.zing.zalo.data.serialization.ZarcelSerializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelPrimitive implements ZarcelSerializable {

    public int mCats;

    public int mDogs;

    public float mRadius;

    public double mTimer;

    public String mName;

    public boolean mWrong;

    @Ignore
    private boolean _no_warning;
    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelPrimitive__Zarcel.serialize(this, serializedOutput);
    }

    public static ZarcelSerializable.Creator<ZarcelPrimitive> CREATOR = new Creator<ZarcelPrimitive>() {
        @Nullable
        @Override
        public ZarcelPrimitive createFromSerialized(SerializedInput input, DebugBuilder builder) {
            try {
                ZarcelPrimitive result = new ZarcelPrimitive();
                ZarcelPrimitive__Zarcel.createFromSerialized(result, input, builder);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
