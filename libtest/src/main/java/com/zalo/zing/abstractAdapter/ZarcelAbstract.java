package com.zalo.zing.abstractAdapter;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

import java.lang.reflect.InvocationTargetException;

@Zarcel
public class ZarcelAbstract implements Serializable {

    @Zarcel.Custom(adapter = VehicleAdapter.class)
    public ZarcelVehicle vehicle;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        try {
            ZarcelAbstract__Zarcel.serialize(this, serializedOutput);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Creator<ZarcelAbstract> CREATOR = new Creator<ZarcelAbstract>() {
        @Nullable
        @Override
        public ZarcelAbstract createFromSerialized(SerializedInput input) {
            try {
                ZarcelAbstract result = new ZarcelAbstract();
                ZarcelAbstract__Zarcel.createFromSerialized(result, input);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
