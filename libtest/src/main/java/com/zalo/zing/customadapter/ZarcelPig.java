package com.zalo.zing.customadapter;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zalo.zing.object.ZarcelObjectOne;
import com.zalo.zing.object.ZarcelObjectTwo;
import com.zing.zalo.zarcel.helper.DebugBuilder;
import com.zing.zalo.zarcel.helper.ZarcelSerializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelPig extends ZarcelAnimal implements ZarcelSerializable {

    public ZarcelObjectOne weight;
    public ZarcelObjectTwo[] height;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelPig__Zarcel.serialize(this, serializedOutput);
    }

    public static ZarcelSerializable.Creator<ZarcelPig> CREATOR = new ZarcelSerializable.Creator<ZarcelPig>() {
        @Nullable
        @Override
        public ZarcelPig createFromSerialized(SerializedInput input, DebugBuilder builder) {
                ZarcelPig result = new ZarcelPig();
                ZarcelPig__Zarcel.createFromSerialized(result, input, builder);
                return result;
        }
    };

}
