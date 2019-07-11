package com.zing.zalo.neon.test.object;

import android.support.annotation.Nullable;
import com.zing.neon.data.serialization.SerializedInput;
import com.zing.neon.data.serialization.SerializedOutput;
import com.zing.zalo.neon.annotations.Neon;
import com.zing.zalo.neon.helper.DebugBuilder;
import com.zing.zalo.neon.helper.NeonSerializable;

@Neon
public class NeonObject implements NeonSerializable {

    // Object

    public NeonObjectOne mFirstValue;
    public NeonObjectTwo mSecondValue;
    public String status;

    // Array

    public NeonObjectOne[] mArrayPet;
    public NeonObjectTwo[] mArrayCats;

    // Primitive Array

    public int[] mSize;
    public float[] mPoint;
    public double[] mSeconds;
    public boolean[] mSizeWrong;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        NeonObject__Neon.serialize(this, serializedOutput);
    }

    public static Creator<NeonObject> CREATOR = new Creator<NeonObject>() {
        @Nullable
        @Override
        public NeonObject createFromSerialized(SerializedInput input, DebugBuilder builder) {
            try {
                NeonObject result = new NeonObject();
                NeonObject__Neon.createFromSerialized(result, input, builder);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
