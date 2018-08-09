package com.zalo.zing.abstractAdapter;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelBike extends ZarcelVehicle implements Serializable {

    public int maxSpeed;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelBike$Zarcel.serialize(this, serializedOutput);
    }

    public static Serializable.Creator<com.zalo.zing.abstractAdapter.ZarcelBike> CREATOR = new Serializable.Creator<com.zalo.zing.abstractAdapter.ZarcelBike>() {
        @Nullable
        @Override
        public com.zalo.zing.abstractAdapter.ZarcelBike createFromSerialized(SerializedInput input) {
            try {
                com.zalo.zing.abstractAdapter.ZarcelBike result = new com.zalo.zing.abstractAdapter.ZarcelBike();
                ZarcelBike$Zarcel.createFromSerialized(result, input);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
