package com.zing.zalo.neon.adapter;

import com.zing.neon.data.serialization.SerializedInput;
import com.zing.neon.data.serialization.SerializedOutput;
import com.zing.zalo.neon.annotations.NonNull;
import com.zing.zalo.neon.helper.DebugBuilder;

public interface NeonAdapter<T> {
    void serialize(@NonNull T object, SerializedOutput writer) throws Exception;

    T createFromSerialized(SerializedInput reader, DebugBuilder builder) throws Exception;
}
