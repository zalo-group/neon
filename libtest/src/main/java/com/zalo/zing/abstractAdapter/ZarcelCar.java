package com.zalo.zing.abstractAdapter;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.DebugBuilder;
import com.zing.zalo.data.serialization.ZarcelSerializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelCar extends ZarcelVehicle implements ZarcelSerializable {

    public int maxSpeed;
    public int numberOfSeat;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelCar__Zarcel.serialize(this, serializedOutput);
    }

    public static ZarcelSerializable.Creator<ZarcelCar> CREATOR = new ZarcelSerializable.Creator<ZarcelCar>() {
        @Nullable
        @Override
        public ZarcelCar createFromSerialized(SerializedInput input, DebugBuilder builder) {
            try {
                ZarcelCar result = new ZarcelCar();
                ZarcelCar__Zarcel.createFromSerialized(result, input, builder);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
