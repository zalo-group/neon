package com.zing.zalo.neon.test.extendClass;

import android.support.annotation.Nullable;
import com.zing.neon.data.serialization.SerializedInput;
import com.zing.neon.data.serialization.SerializedOutput;
import com.zing.zalo.neon.annotations.Neon;
import com.zing.zalo.neon.helper.DebugBuilder;
import com.zing.zalo.neon.helper.NeonSerializable;

@Neon
public class NeonChild extends NeonParent implements NeonSerializable {
    public String daddyName;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        NeonChild__Neon.serialize(this, serializedOutput);
    }

    public static NeonSerializable.Creator<NeonChild> CREATOR =
            new NeonSerializable.Creator<NeonChild>() {
                @Nullable
                @Override
                public NeonChild createFromSerialized(SerializedInput input, DebugBuilder builder) {
                    try {
                        NeonChild result = new NeonChild();
                        NeonChild__Neon.createFromSerialized(result, input, builder);
                        return result;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            };
}
