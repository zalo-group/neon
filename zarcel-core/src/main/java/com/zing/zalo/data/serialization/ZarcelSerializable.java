package com.zing.zalo.data.serialization;

public interface ZarcelSerializable {
    void serialize(SerializedOutput output);

    interface Creator<T extends ZarcelSerializable> {
        T createFromSerialized(SerializedInput input, DebugBuilder builder);
    }
}