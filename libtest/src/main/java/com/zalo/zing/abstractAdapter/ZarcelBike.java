package com.zalo.zing.abstractAdapter;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.helper.DebugBuilder;
import com.zing.zalo.helper.ZarcelSerializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelBike extends ZarcelVehicle implements ZarcelSerializable {

    public int maxSpeed;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelBike__Zarcel.serialize(this, serializedOutput);
    }

    public static ZarcelSerializable.Creator<com.zalo.zing.abstractAdapter.ZarcelBike> CREATOR = new ZarcelSerializable.Creator<com.zalo.zing.abstractAdapter.ZarcelBike>() {
        @Nullable
        @Override
        public com.zalo.zing.abstractAdapter.ZarcelBike createFromSerialized(SerializedInput input, DebugBuilder builder) {
            try {
                com.zalo.zing.abstractAdapter.ZarcelBike result = new com.zalo.zing.abstractAdapter.ZarcelBike();
                ZarcelBike__Zarcel.createFromSerialized(result, input, builder);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
