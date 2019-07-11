package com.zing.zalo.neon.test.abstractAdapter;

import android.support.annotation.Nullable;
import com.zing.neon.data.serialization.SerializedInput;
import com.zing.neon.data.serialization.SerializedOutput;
import com.zing.zalo.neon.annotations.Neon;
import com.zing.zalo.neon.helper.DebugBuilder;
import com.zing.zalo.neon.helper.NeonSerializable;
import java.lang.reflect.InvocationTargetException;

@Neon
public class NeonAbstract implements NeonSerializable {

    @Neon.Custom(adapter = VehicleAdapter.class)
    public NeonVehicle vehicle;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        try {
            NeonAbstract__Neon.serialize(this, serializedOutput);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Creator<NeonAbstract> CREATOR = new Creator<NeonAbstract>() {
        @Nullable
        @Override
        public NeonAbstract createFromSerialized(SerializedInput input, DebugBuilder builder) {
            try {
                NeonAbstract result = new NeonAbstract();
                NeonAbstract__Neon.createFromSerialized(result, input, builder);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
