package com.zalo.zing.nullable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zalo.zing.primitive.ZarcelPrimitive;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelNullable implements Serializable {

    @NonNull
    public ZarcelPrimitive mPet;

    @Nullable
    public ZarcelPrimitive mOptionalPet;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelNullable__Zarcel.serialize(this, serializedOutput);
    }

    public static Serializable.Creator<ZarcelNullable> CREATOR = new Serializable.Creator<ZarcelNullable>() {
        @Nullable
        @Override
        public ZarcelNullable createFromSerialized(SerializedInput input) {
            try {
                ZarcelNullable result = new ZarcelNullable();
                ZarcelNullable__Zarcel.createFromSerialized(result, input);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
