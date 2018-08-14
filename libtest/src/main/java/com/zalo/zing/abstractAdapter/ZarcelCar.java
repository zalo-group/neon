package com.zalo.zing.abstractAdapter;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
public class ZarcelCar extends ZarcelVehicle implements Serializable {

    public int maxSpeed;
    public int numberOfSeat;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelCar__Zarcel.serialize(this, serializedOutput);
    }

    public static Serializable.Creator<ZarcelCar> CREATOR = new Serializable.Creator<ZarcelCar>() {
        @Nullable
        @Override
        public ZarcelCar createFromSerialized(SerializedInput input) {
            try {
                ZarcelCar result = new ZarcelCar();
                ZarcelCar__Zarcel.createFromSerialized(result, input);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
