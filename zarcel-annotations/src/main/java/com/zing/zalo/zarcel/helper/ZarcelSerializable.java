package com.zing.zalo.zarcel.helper;

import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

public interface ZarcelSerializable {
    void serialize(SerializedOutput output);

    interface Creator<T extends ZarcelSerializable> {
        T createFromSerialized(SerializedInput input, DebugBuilder builder);
    }
}