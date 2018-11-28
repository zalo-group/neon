package com.zing.zalo.helper;

import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.zarcel.helper.DebugBuilder;
import com.zing.zalo.zarcel.helper.ZarcelSerializable;

import java.util.AbstractMap;
import java.util.Map;

public final class SerializableHelper {
    private SerializableHelper() {
    }

    /**
     * @return Object T and log if debug = true.
     */

    public static <T extends ZarcelSerializable> T deserialize(
            SerializedInput serializedInput,
            ZarcelSerializable.Creator<T> input,
            DebugBuilder.Logger logger,
            int indent,
            int maxDepth
    ) throws Exception {
        if (logger == null) {
            return input.createFromSerialized(serializedInput, null);
        }

        DebugBuilder builder = new DebugBuilderImpl(indent, maxDepth);
        try {
            T obj = input.createFromSerialized(serializedInput, builder);
            logger.log(builder);
            return obj;
        } catch (Exception e) {
            builder.onError();
            throw new Exception(builder.getLog(), e);
        }
    }

    public static <T extends ZarcelSerializable> T deserialize(
            SerializedInput serializedInput,
            ZarcelSerializable.Creator<T> input,
            DebugBuilder.Logger logger
    ) throws Exception {
        return deserialize(serializedInput, input, logger, 4, 4);
    }
}
