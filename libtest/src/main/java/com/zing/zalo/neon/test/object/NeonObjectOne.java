package com.zing.zalo.neon.test.object;

import android.support.annotation.Nullable;
import com.zing.neon.data.serialization.SerializedInput;
import com.zing.neon.data.serialization.SerializedOutput;
import com.zing.zalo.neon.annotations.Neon;
import com.zing.zalo.neon.helper.DebugBuilder;
import com.zing.zalo.neon.helper.NeonSerializable;

@Neon
public class NeonObjectOne implements NeonSerializable {

    public float[] xPosition;
    public float[] yPosition;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        NeonObjectOne__Neon.serialize(this, serializedOutput);
    }

    public static Creator<NeonObjectOne> CREATOR = new Creator<NeonObjectOne>() {
        @Nullable
        @Override
        public NeonObjectOne createFromSerialized(SerializedInput input, DebugBuilder builder) {
            try {
                NeonObjectOne result = new NeonObjectOne();
                NeonObjectOne__Neon.createFromSerialized(result, input, builder);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
