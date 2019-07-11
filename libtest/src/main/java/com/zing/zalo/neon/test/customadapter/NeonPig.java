package com.zing.zalo.neon.test.customadapter;

import android.support.annotation.Nullable;
import com.zing.neon.data.serialization.SerializedInput;
import com.zing.neon.data.serialization.SerializedOutput;
import com.zing.zalo.neon.annotations.Neon;
import com.zing.zalo.neon.helper.DebugBuilder;
import com.zing.zalo.neon.helper.NeonSerializable;
import com.zing.zalo.neon.test.object.NeonObjectOne;
import com.zing.zalo.neon.test.object.NeonObjectTwo;

@Neon
public class NeonPig extends NeonAnimal implements NeonSerializable {

    public NeonObjectOne weight;
    public NeonObjectTwo[] height;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        NeonPig__Neon.serialize(this, serializedOutput);
    }

    public static NeonSerializable.Creator<NeonPig> CREATOR =
            new NeonSerializable.Creator<NeonPig>() {
                @Nullable
                @Override
                public NeonPig createFromSerialized(SerializedInput input, DebugBuilder builder) {
                    NeonPig result = new NeonPig();
                    NeonPig__Neon.createFromSerialized(result, input, builder);
                    return result;
                }
            };
}
