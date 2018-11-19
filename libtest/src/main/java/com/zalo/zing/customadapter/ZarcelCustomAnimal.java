package com.zalo.zing.customadapter;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.DebugBuilder;
import com.zing.zalo.data.serialization.ZarcelSerializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelCustomAnimal implements ZarcelSerializable {


    @Zarcel.Custom(adapter = AnimalAdapter.class)
    public ZarcelAnimal[] animals;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelCustomAnimal__Zarcel.serialize(this, serializedOutput);
    }

    public static ZarcelSerializable.Creator<ZarcelCustomAnimal> CREATOR = new ZarcelSerializable.Creator<ZarcelCustomAnimal>() {
        @Nullable
        @Override
        public ZarcelCustomAnimal createFromSerialized(SerializedInput input, DebugBuilder builder) {

            ZarcelCustomAnimal result = new ZarcelCustomAnimal();
            ZarcelCustomAnimal__Zarcel.createFromSerialized(result, input, builder);
            return result;
        }
    };
}
