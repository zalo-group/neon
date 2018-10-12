package com.zing.zalo.data.serialization;

public class SerializableHelper<T extends Serializable> {
    public T deserialize(byte[] serializedData, Serializable.Creator<T> input, SerializedError error) throws Exception {
        try {
            return input.createFromSerialized(new SerializedByteBufferInput(serializedData));
        } catch (Exception e) {
            if (error != null) {
                ErrorBuilder builder = new ErrorBuilder(4);
                error.printErrorObject(builder, new SerializedByteBufferInput(serializedData));
                throw new Exception(builder.toString(), e);
            }
        }
        return null;
    }
}
