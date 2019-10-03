package com.zing.zalo.neon2.export;

import com.zing.zalo.neon2.export.exception.NeonException;

/**
 * Created by Tien Loc Bui on 11/09/2019.
 */
@SuppressWarnings("UnusedReturnValue")
public interface SerializedOutput {
    SerializedOutput writeByte(byte val) throws NeonException;

    SerializedOutput writeBoolean(boolean val) throws NeonException;

    SerializedOutput writeInt(int val) throws NeonException;

    SerializedOutput writeLong(long val) throws NeonException;

    SerializedOutput writeFloat(float val) throws NeonException;

    SerializedOutput writeDouble(double val) throws NeonException;

    SerializedOutput writeChar(char val) throws NeonException;

    SerializedOutput writeShort(short val) throws NeonException;

    SerializedOutput writeByteArray(byte[] val) throws NeonException;

    SerializedOutput writeBooleanArray(boolean[] val) throws NeonException;

    SerializedOutput writeIntArray(int[] val) throws NeonException;

    SerializedOutput writeLongArray(long[] val) throws NeonException;

    SerializedOutput writeFloatArray(float[] val) throws NeonException;

    SerializedOutput writeDoubleArray(double[] val) throws NeonException;

    SerializedOutput writeCharArray(char[] val) throws NeonException;

    SerializedOutput writeShortArray(short[] val) throws NeonException;

    /**
     * Write begin of object with object version
     */
    SerializedOutput writeObjectStart(int version) throws NeonException;

    /**
     * Mark that object is ended.
     */
    SerializedOutput writeObjectEnd() throws NeonException;

    /**
     * Write null object, null array
     */
    SerializedOutput writeNull() throws NeonException;
}
