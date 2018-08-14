package com.zalo.zing.extendClass;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zalo.zing.customadapter.AnimalAdapter;
import com.zalo.zing.customadapter.ZarcelAnimal;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel(version = 3)
public class ZarcelParent implements Serializable {

    @Zarcel.Property(sinceVersion = 1)
    public int x;

    @Zarcel.Property(sinceVersion = 1)
    public int y;

    @Zarcel.Property(sinceVersion = 1)
    public int z;

    @Zarcel.Property(sinceVersion = 3)
    @Zarcel.Custom(adapter = AnimalAdapter.class)
    public ZarcelAnimal[] animals;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelParent__Zarcel.serialize(this, serializedOutput);
    }

    public static Serializable.Creator<ZarcelParent> CREATOR = new Serializable.Creator<ZarcelParent>() {
        @Nullable
        @Override
        public ZarcelParent createFromSerialized(SerializedInput input) {
            try {
                ZarcelParent result = new ZarcelParent();
                ZarcelParent__Zarcel.createFromSerialized(result, input);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };

}
