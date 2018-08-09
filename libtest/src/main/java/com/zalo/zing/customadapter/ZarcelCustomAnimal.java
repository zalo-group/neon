package com.zalo.zing.customadapter;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelCustomAnimal implements Serializable {


    @Zarcel.Custom(adapter = AnimalAdapter.class)
    public ZarcelAnimal[] animals;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelCustomAnimal$Zarcel.serialize(this, serializedOutput);
    }

    public static Serializable.Creator<ZarcelCustomAnimal> CREATOR = new Serializable.Creator<ZarcelCustomAnimal>() {
        @Nullable
        @Override
        public ZarcelCustomAnimal createFromSerialized(SerializedInput input) {

            ZarcelCustomAnimal result = new ZarcelCustomAnimal();
            ZarcelCustomAnimal$Zarcel.createFromSerialized(result, input);
            return result;
        }
    };
}
