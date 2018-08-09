package com.zalo.zing.object;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel(version = 2)
public class ZarcelObjectTwo implements Serializable {

    @Zarcel.Property(sinceVersion = 1)
    public int mSize;

    @Zarcel.Property(sinceVersion = 2)
    public int[] mElement;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelObjectTwo$Zarcel.serialize(this, serializedOutput);
    }

    public static Creator<ZarcelObjectTwo> CREATOR = new Creator<ZarcelObjectTwo>() {
        @Nullable
        @Override
        public ZarcelObjectTwo createFromSerialized(SerializedInput input) {
            try {
                ZarcelObjectTwo result = new ZarcelObjectTwo();
                ZarcelObjectTwo$Zarcel.createFromSerialized(result, input);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
