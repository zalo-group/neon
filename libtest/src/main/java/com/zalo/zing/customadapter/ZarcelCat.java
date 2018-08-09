package com.zalo.zing.customadapter;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zalo.zing.object.ZarcelObjectOne;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelCat extends ZarcelAnimal implements Serializable {

    public int[] foods;
    public ZarcelObjectOne[] playable;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelCat$Zarcel.serialize(this, serializedOutput);
    }

    public static Serializable.Creator<ZarcelCat> CREATOR = new Serializable.Creator<ZarcelCat>() {
        @Nullable
        @Override
        public ZarcelCat createFromSerialized(SerializedInput input) {
            try {
                ZarcelCat result = new ZarcelCat();
                com.zalo.zing.customadapter.ZarcelCat$Zarcel.createFromSerialized(result, input);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
