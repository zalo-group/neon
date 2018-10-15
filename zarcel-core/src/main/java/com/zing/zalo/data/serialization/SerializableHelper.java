package com.zing.zalo.data.serialization;

public class SerializableHelper<T extends Serializable> {
    public T deserialize(byte[] serializedData, Serializable.Creator<T> input, boolean debug) throws Exception {
        // TODO: handle with debug tag.
//        if (debug) {
//            try {
//                obj = input.createFromSerialized(...);
//            } catch (Exception e) {
//                builder.onError();
//                throw new (builder.toString(), e);
//            }
//            Logging -> builder.toString();
//            return obj;
//        } else {
//            return input.createFromSerialized(aabnbb);
//        }
        return null;
    }
}
