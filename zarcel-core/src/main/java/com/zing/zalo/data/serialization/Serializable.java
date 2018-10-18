package com.zing.zalo.data.serialization;

/**
 * Serializable
 * Created by khanhtm on 10/05/2016.
 */
public interface Serializable {
    void serialize(SerializedOutput output);

    interface Creator<T extends Serializable> {
        T createFromSerialized(SerializedInput input, DebugBuilder builder);
    }
}
