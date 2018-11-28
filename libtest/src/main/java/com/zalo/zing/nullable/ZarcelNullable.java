package com.zalo.zing.nullable;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zalo.zing.primitive.ZarcelPrimitive;
import com.zing.zalo.zarcel.helper.DebugBuilder;
import com.zing.zalo.zarcel.helper.ZarcelSerializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelNullable implements ZarcelSerializable {

    @NonNull
    public ZarcelPrimitive mPet;

    @Nullable
    public ZarcelPrimitive mOptionalPet;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelNullable__Zarcel.serialize(this, serializedOutput);
    }

    public static ZarcelSerializable.Creator<ZarcelNullable> CREATOR = new ZarcelSerializable.Creator<ZarcelNullable>() {
        @Nullable
        @Override
        public ZarcelNullable createFromSerialized(SerializedInput input, DebugBuilder builder) {
            try {
                ZarcelNullable result = new ZarcelNullable();
                ZarcelNullable__Zarcel.createFromSerialized(result, input, builder);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
