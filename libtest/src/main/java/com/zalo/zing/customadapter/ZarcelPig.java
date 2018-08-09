package com.zalo.zing.customadapter;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zalo.zing.object.ZarcelObjectOne;
import com.zalo.zing.object.ZarcelObjectTwo;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelPig extends ZarcelAnimal implements Serializable {

    public ZarcelObjectOne weight;
    public ZarcelObjectTwo[] height;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelPig$Zarcel.serialize(this, serializedOutput);
    }

    public static Serializable.Creator<ZarcelPig> CREATOR = new Serializable.Creator<ZarcelPig>() {
        @Nullable
        @Override
        public ZarcelPig createFromSerialized(SerializedInput input) {
            try {
                ZarcelPig result = new ZarcelPig();
                ZarcelPig$Zarcel.createFromSerialized(result, input);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };

}
