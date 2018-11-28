package com.zing.zalo.adapter;


import com.zing.zalo.annotations.NonNull;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;
import com.zing.zalo.helper.DebugBuilder;

public interface ZarcelAdapter<T> {
    void serialize(@NonNull T object, SerializedOutput writer) throws Exception;

    T createFromSerialized(SerializedInput reader, DebugBuilder builder) throws Exception;
}
