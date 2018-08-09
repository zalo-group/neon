package com.zalo.zing.extendClass;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelChild extends ZarcelRoot implements Serializable {
    public String daddyName;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelChild$Zarcel.serialize(this, serializedOutput);
    }

    public static Serializable.Creator<ZarcelChild> CREATOR = new Serializable.Creator<ZarcelChild>() {
        @Nullable
        @Override
        public ZarcelChild createFromSerialized(SerializedInput input) {
            try {
                ZarcelChild result = new ZarcelChild();
                ZarcelChild$Zarcel.createFromSerialized(result, input);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
