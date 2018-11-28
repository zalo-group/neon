package com.zalo.zing.extendClass;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zalo.zing.customadapter.AnimalAdapter;
import com.zalo.zing.customadapter.ZarcelAnimal;
import com.zing.zalo.zarcel.helper.DebugBuilder;
import com.zing.zalo.zarcel.helper.ZarcelSerializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel(version = 3)
public class ZarcelParent implements ZarcelSerializable {

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

    public static ZarcelSerializable.Creator<ZarcelParent> CREATOR = new ZarcelSerializable.Creator<ZarcelParent>() {
        @Nullable
        @Override
        public ZarcelParent createFromSerialized(SerializedInput input, DebugBuilder builder) {
            try {
                ZarcelParent result = new ZarcelParent();
                ZarcelParent__Zarcel.createFromSerialized(result, input, builder);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };

}
