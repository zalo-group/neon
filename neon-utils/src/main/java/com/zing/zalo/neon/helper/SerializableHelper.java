package com.zing.zalo.neon.helper;

import com.zing.neon.data.serialization.SerializedInput;

public final class SerializableHelper {
    private SerializableHelper() {
    }

    /**
     * @return Object T and log if debug = true.
     */

    public static <T extends NeonSerializable> T deserialize(
            SerializedInput serializedInput,
            NeonSerializable.Creator<T> input,
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

    public static <T extends NeonSerializable> T deserialize(
            SerializedInput serializedInput,
            NeonSerializable.Creator<T> input,
            DebugBuilder.Logger logger
    ) throws Exception {
        return deserialize(serializedInput, input, logger, 4, 4);
    }
}
