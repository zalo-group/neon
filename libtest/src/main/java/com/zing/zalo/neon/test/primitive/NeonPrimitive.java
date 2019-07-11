package com.zing.zalo.neon.test.primitive;

import android.support.annotation.Nullable;
import com.zing.neon.data.serialization.SerializedInput;
import com.zing.neon.data.serialization.SerializedOutput;
import com.zing.zalo.neon.annotations.Ignore;
import com.zing.zalo.neon.annotations.Neon;
import com.zing.zalo.neon.helper.DebugBuilder;
import com.zing.zalo.neon.helper.NeonSerializable;

@Neon
public class NeonPrimitive implements NeonSerializable {

    public int mCats;

    public int mDogs;

    public float mRadius;

    public double mTimer;

    public String mName;

    public boolean mWrong;

    @Ignore
    private boolean _no_warning;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        NeonPrimitive__Neon.serialize(this, serializedOutput);
    }

    public static NeonSerializable.Creator<NeonPrimitive> CREATOR = new Creator<NeonPrimitive>() {
        @Nullable
        @Override
        public NeonPrimitive createFromSerialized(SerializedInput input, DebugBuilder builder) {
            try {
                NeonPrimitive result = new NeonPrimitive();
                NeonPrimitive__Neon.createFromSerialized(result, input, builder);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
