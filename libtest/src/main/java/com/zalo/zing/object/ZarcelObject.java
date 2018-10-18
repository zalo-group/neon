package com.zalo.zing.object;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.DebugBuilder;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelObject implements Serializable {

    // Object

    public ZarcelObjectOne mFirstValue;
    public ZarcelObjectTwo mSecondValue;
    public String status;

    // Array

    public ZarcelObjectOne[] mArrayPet;
    public ZarcelObjectTwo[] mArrayCats;

    // Primitive Array

    public int[] mSize;
    public float[] mPoint;
    public double[] mSeconds;
    public boolean[] mSizeWrong;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelObject__Zarcel.serialize(this, serializedOutput);
    }

    public static Creator<ZarcelObject> CREATOR = new Creator<ZarcelObject>() {
        @Nullable
        @Override
        public ZarcelObject createFromSerialized(SerializedInput input, DebugBuilder builder) {
            try {
                ZarcelObject result = new ZarcelObject();
                ZarcelObject__Zarcel.createFromSerialized(result, input, builder);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
