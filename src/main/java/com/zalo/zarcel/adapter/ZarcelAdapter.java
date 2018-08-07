package com.zalo.zarcel.adapter;


import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

public interface ZarcelAdapter<T> {
    void serialize(T object, SerializedOutput writer);

    T createFromSerialized(SerializedInput reader);
}
