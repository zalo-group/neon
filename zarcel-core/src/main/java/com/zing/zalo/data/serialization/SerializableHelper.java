package com.zing.zalo.data.serialization;

import java.util.AbstractMap;
import java.util.Map;

public class SerializableHelper<T extends Serializable> {

    private final int mMaxDepth;

    public SerializableHelper() {
        mMaxDepth = 4;
    }

    public SerializableHelper(int maxDepth) {
        mMaxDepth = maxDepth;
    }

    /**
     * @return Object T and log if debug = true.
     */


    public Map.Entry<T, String> deserialize(SerializedInput serializedInput, Serializable.Creator<T> input, boolean debug) throws Exception {
        DebugBuilder builder = new DebugBuilder(4, mMaxDepth);
        T obj;
        if (debug) {
            try {
                obj = input.createFromSerialized(serializedInput, builder);
            } catch (Exception e) {
                builder.onError();
                throw new Exception(builder.toString(), e);
            }
            return new AbstractMap.SimpleEntry<>(obj, builder.toString());
        } else {
            return new AbstractMap.SimpleEntry<>(input.createFromSerialized(serializedInput, null), null);
        }
    }
}
