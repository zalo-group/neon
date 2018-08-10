package com.zing.zalo.data.serialization;

/**
 * SerializedOutput
 * Created by khanhtm on 10/05/2016.
 */
public interface SerializedOutput {
    public abstract void writeInt16(int x);

    public abstract void writeInt32(int x);

    public abstract void writeInt64(long x);

    public abstract void writeBool(boolean value);

    public abstract void writeBytes(byte[] b);

    public abstract void writeBytes(byte[] b, int offset, int count);

    public abstract void writeByte(int i);

    public abstract void writeByte(byte b);

    public abstract void writeString(String s);

    public abstract void writeByteArray(byte[] b, int offset, int count);

    public abstract void writeByteArray(byte[] b);

    public abstract void writeDouble(double d);

}
