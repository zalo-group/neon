package com.zing.neon.data.serialization;

/**
 * SerializedInput
 * Created by khanhtm on 10/05/2016.
 */
public interface SerializedInput {
    int readByte();

    int readInt16();

    int readInt32();

    boolean readBool();

    long readInt64();

    String readString();

    byte[] readByteArray();

    double readDouble();

    void skip(int count);

    int getPosition();

    int available();

    boolean markSupported();

    void mark(int readlimit);
}
