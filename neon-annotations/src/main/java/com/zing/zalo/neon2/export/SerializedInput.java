package com.zing.zalo.neon2.export;

import com.zing.zalo.neon2.export.exception.NeonEndObjectException;
import com.zing.zalo.neon2.export.exception.NeonException;

/**
 * Created by Tien Loc Bui on 11/09/2019.
 */
public interface SerializedInput {
    byte readByte() throws NeonException, NeonEndObjectException;

    boolean readBoolean() throws NeonException, NeonEndObjectException;

    int readInt() throws NeonException, NeonEndObjectException;

    long readLong() throws NeonException, NeonEndObjectException;

    float readFloat() throws NeonException, NeonEndObjectException;

    double readDouble() throws NeonException, NeonEndObjectException;

    char readChar() throws NeonException, NeonEndObjectException;

    short readShort() throws NeonException, NeonEndObjectException;

    byte[] readByteArray() throws NeonException, NeonEndObjectException;

    boolean[] readBooleanArray() throws NeonException, NeonEndObjectException;

    int[] readIntArray() throws NeonException, NeonEndObjectException;

    long[] readLongArray() throws NeonException, NeonEndObjectException;

    float[] readFloatArray() throws NeonException, NeonEndObjectException;

    double[] readDoubleArray() throws NeonException, NeonEndObjectException;

    char[] readCharArray() throws NeonException, NeonEndObjectException;

    short[] readShortArray() throws NeonException, NeonEndObjectException;

    boolean nextIsNull() throws NeonException;

    /**
     * Read object to get object version
     * @param currentVersion is current version of object
     * @return version of serialization data.
     */
    int readObjectStart(int currentVersion) throws NeonException;

    /**
     * Mark that object is ended.
     */
    void readObjectEnd() throws NeonException;
}
