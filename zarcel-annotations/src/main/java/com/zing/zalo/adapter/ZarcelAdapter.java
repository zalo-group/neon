package com.zing.zalo.adapter;


import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

public interface ZarcelAdapter<T> {
    void serialize(T object, SerializedOutput writer) throws Exception;

    T createFromSerialized(SerializedInput reader) throws Exception;
}
