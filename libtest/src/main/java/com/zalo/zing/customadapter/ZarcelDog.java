package com.zalo.zing.customadapter;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zalo.zing.primitive.ZarcelPrimitive;
import com.zing.zalo.helper.DebugBuilder;
import com.zing.zalo.helper.ZarcelSerializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelDog extends ZarcelAnimal implements ZarcelSerializable {

    public String foods;
    public ZarcelPrimitive eatAny;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelDog__Zarcel.serialize(this, serializedOutput);
    }

    public static ZarcelSerializable.Creator<ZarcelDog> CREATOR = new ZarcelSerializable.Creator<ZarcelDog>() {
        @Nullable
        @Override
        public ZarcelDog createFromSerialized(SerializedInput input, DebugBuilder builder) {
            try {
                ZarcelDog result = new ZarcelDog();
                ZarcelDog__Zarcel.createFromSerialized(result, input, builder);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
