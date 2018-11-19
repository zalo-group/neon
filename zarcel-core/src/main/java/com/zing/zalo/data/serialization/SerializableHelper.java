package com.zing.zalo.data.serialization;

import java.util.AbstractMap;
import java.util.Map;

public final class SerializableHelper {
    private SerializableHelper() {
    }

    /**
     * @return Object T and log if debug = true.
     */

    public static <T extends ZarcelSerializable> Map.Entry<T, String> deserialize(
            SerializedInput serializedInput,
            ZarcelSerializable.Creator<T> input,
            boolean debug,
            int indent,
            int maxDepth
    ) throws Exception {
        if (!debug) {
            return new AbstractMap.SimpleEntry<>(input.createFromSerialized(serializedInput, null), null);
        }

        DebugBuilder builder = new DebugBuilder(indent, maxDepth);
        try {
            T obj = input.createFromSerialized(serializedInput, builder);
            return new AbstractMap.SimpleEntry<>(obj, builder.toString());
        } catch (Exception e) {
            builder.onError();
            throw new Exception(builder.toString(), e);
        }
    }

    public static <T extends ZarcelSerializable> Map.Entry<T, String> deserialize(
            SerializedInput serializedInput,
            ZarcelSerializable.Creator<T> input,
            boolean debug
    ) throws Exception {
        return deserialize(serializedInput, input, debug, 4, 4);
    }
}
