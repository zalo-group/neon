package com.zalo.zing.object;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.zarcel.helper.DebugBuilder;
import com.zing.zalo.zarcel.helper.ZarcelSerializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelObjectOne implements ZarcelSerializable {

    public float[] xPosition;
    public float[] yPosition;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelObjectOne__Zarcel.serialize(this, serializedOutput);
    }

    public static Creator<ZarcelObjectOne> CREATOR = new Creator<ZarcelObjectOne>() {
        @Nullable
        @Override
        public ZarcelObjectOne createFromSerialized(SerializedInput input, DebugBuilder builder) {
            try {
                ZarcelObjectOne result = new ZarcelObjectOne();
                ZarcelObjectOne__Zarcel.createFromSerialized(result, input, builder);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
