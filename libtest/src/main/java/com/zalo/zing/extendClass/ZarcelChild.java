package com.zalo.zing.extendClass;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.DebugBuilder;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelChild extends ZarcelParent implements Serializable {
    public String daddyName;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelChild__Zarcel.serialize(this, serializedOutput);
    }

    public static Serializable.Creator<ZarcelChild> CREATOR = new Serializable.Creator<ZarcelChild>() {
        @Nullable
        @Override
        public ZarcelChild createFromSerialized(SerializedInput input, DebugBuilder builder) {
            try {
                ZarcelChild result = new ZarcelChild();
                ZarcelChild__Zarcel.createFromSerialized(result, input, builder);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
