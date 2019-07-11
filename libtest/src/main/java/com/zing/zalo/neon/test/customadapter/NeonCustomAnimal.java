package com.zing.zalo.neon.test.customadapter;

import android.support.annotation.Nullable;
import com.zing.neon.data.serialization.SerializedInput;
import com.zing.neon.data.serialization.SerializedOutput;
import com.zing.zalo.neon.annotations.Neon;
import com.zing.zalo.neon.helper.DebugBuilder;
import com.zing.zalo.neon.helper.NeonSerializable;

@Neon
public class NeonCustomAnimal implements NeonSerializable {

    @Neon.Custom(adapter = AnimalAdapter.class)
    public NeonAnimal[] animals;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        NeonCustomAnimal__Neon.serialize(this, serializedOutput);
    }

    public static NeonSerializable.Creator<NeonCustomAnimal> CREATOR =
            new NeonSerializable.Creator<NeonCustomAnimal>() {
                @Nullable
                @Override
                public NeonCustomAnimal createFromSerialized(SerializedInput input,
                        DebugBuilder builder) {

                    NeonCustomAnimal result = new NeonCustomAnimal();
                    NeonCustomAnimal__Neon.createFromSerialized(result, input, builder);
                    return result;
                }
            };
}
