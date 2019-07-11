package com.zing.zalo.neon.helper;

import com.zing.neon.data.serialization.SerializedInput;
import com.zing.neon.data.serialization.SerializedOutput;

public interface NeonSerializable {
    void serialize(SerializedOutput output);

    interface Creator<T extends NeonSerializable> {
        T createFromSerialized(SerializedInput input, DebugBuilder builder);
    }
}