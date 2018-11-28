package com.zalo.zing.customadapter;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zalo.zing.object.ZarcelObjectOne;
import com.zing.zalo.helper.DebugBuilder;
import com.zing.zalo.helper.ZarcelSerializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelCat extends ZarcelAnimal implements ZarcelSerializable {

    public int[] foods;
    public ZarcelObjectOne[] playable;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelCat__Zarcel.serialize(this, serializedOutput);
    }

    public static ZarcelSerializable.Creator<ZarcelCat> CREATOR = new ZarcelSerializable.Creator<ZarcelCat>() {
        @Nullable
        @Override
        public ZarcelCat createFromSerialized(SerializedInput input, DebugBuilder builder) {
            try {
                ZarcelCat result = new ZarcelCat();
                ZarcelCat__Zarcel.createFromSerialized(result, input, builder);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
