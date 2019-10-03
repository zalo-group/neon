package com.zing.zalo.neon2.export;

import com.twitter.serial.stream.SerializerInput;
import com.twitter.serial.stream.bytebuffer.ByteBufferSerializerInput;
import com.twitter.serial.util.OptionalFieldException;
import com.twitter.serial.util.SerializationUtils;
import com.zing.zalo.neon2.export.exception.NeonEndObjectException;
import com.zing.zalo.neon2.export.exception.NeonException;

import java.io.IOException;

/**
 * Created by Tien Loc Bui on 02/10/2019.
 */
public class TwitterSerializerInput implements SerializedInput {
    private SerializerInput mInput;

    public static SerializedInput fromByteArray(byte[] bytes) {
        return new TwitterSerializerInput(bytes);
    }

    private TwitterSerializerInput(byte[] bytes) {
        mInput = new ByteBufferSerializerInput(bytes);
    }

    @Override
    public byte readByte() throws NeonException, NeonEndObjectException {
        try {
            return mInput.readByte();
        } catch (OptionalFieldException e) {
            throw new NeonEndObjectException(e);
        } catch (IOException ioe) {
            throw new NeonException(ioe);
        }
    }

    @Override
    public boolean readBoolean() throws NeonException, NeonEndObjectException {
        try {
            return mInput.readBoolean();
        } catch (OptionalFieldException e) {
            throw new NeonEndObjectException(e);
        } catch (IOException ioe) {
            throw new NeonException(ioe);
        }
    }

    @Override
    public int readInt() throws NeonException, NeonEndObjectException {
        try {
            return mInput.readInt();
        } catch (OptionalFieldException e) {
            throw new NeonEndObjectException(e);
        } catch (IOException ioe) {
            throw new NeonException(ioe);
        }
    }

    @Override
    public long readLong() throws NeonException, NeonEndObjectException {
        try {
            return mInput.readLong();
        } catch (OptionalFieldException e) {
            throw new NeonEndObjectException(e);
        } catch (IOException ioe) {
            throw new NeonException(ioe);
        }
    }

    @Override
    public float readFloat() throws NeonException, NeonEndObjectException {
        try {
            return mInput.readFloat();
        } catch (OptionalFieldException e) {
            throw new NeonEndObjectException(e);
        } catch (IOException ioe) {
            throw new NeonException(ioe);
        }
    }

    @Override
    public double readDouble() throws NeonException, NeonEndObjectException {
        try {
            return mInput.readDouble();
        } catch (OptionalFieldException e) {
            throw new NeonEndObjectException(e);
        } catch (IOException ioe) {
            throw new NeonException(ioe);
        }
    }

    @Override
    public char readChar() throws NeonException, NeonEndObjectException {
        try {
            return (char) mInput.readInt();
        } catch (OptionalFieldException e) {
            throw new NeonEndObjectException(e);
        } catch (IOException ioe) {
            throw new NeonException(ioe);
        }
    }

    @Override
    public short readShort() throws NeonException, NeonEndObjectException {
        try {
            return (short) mInput.readInt();
        } catch (OptionalFieldException e) {
            throw new NeonEndObjectException(e);
        } catch (IOException ioe) {
            throw new NeonException(ioe);
        }
    }

    @Override
    public byte[] readByteArray() throws NeonException, NeonEndObjectException {
        if (nextIsNull())
            return null;
        int length = readInt();
        byte[] result = new byte[length];
        try {
            for (int i = 0; i < length; i++) {
                result[i] = readByte();
            }
            return result;
        } catch (NeonEndObjectException e) {
            throw new NeonException("Array data broken.");
        }
    }

    @Override
    public boolean[] readBooleanArray() throws NeonException, NeonEndObjectException {
        if (nextIsNull())
            return null;
        int length = readInt();
        boolean[] result = new boolean[length];
        try {
            for (int i = 0; i < length; i++) {
                result[i] = readBoolean();
            }
        } catch (NeonEndObjectException e) {
            throw new NeonException("Array data broken.");
        }
        return result;
    }

    @Override
    public int[] readIntArray() throws NeonException, NeonEndObjectException {
        if (nextIsNull())
            return null;
        int length = readInt();
        int[] result = new int[length];
        try {
            for (int i = 0; i < length; i++) {
                result[i] = readInt();
            }
        } catch (NeonEndObjectException e) {
            throw new NeonException("Array data broken.");
        }
        return result;
    }

    @Override
    public long[] readLongArray() throws NeonException, NeonEndObjectException {
        if (nextIsNull())
            return null;
        int length = readInt();
        long[] result = new long[length];
        try {
            for (int i = 0; i < length; i++) {
                result[i] = readLong();
            }
        } catch (NeonEndObjectException e) {
            throw new NeonException("Array data broken.");
        }
        return result;
    }

    @Override
    public float[] readFloatArray() throws NeonException, NeonEndObjectException {
        if (nextIsNull())
            return null;
        int length = readInt();
        float[] result = new float[length];
        try {
            for (int i = 0; i < length; i++) {
                result[i] = readFloat();
            }
        } catch (NeonEndObjectException e) {
            throw new NeonException("Array data broken.");
        }
        return result;
    }

    @Override
    public double[] readDoubleArray() throws NeonException, NeonEndObjectException {
        if (nextIsNull())
            return null;
        int length = readInt();
        double[] result = new double[length];
        try {
            for (int i = 0; i < length; i++) {
                result[i] = readDouble();
            }
        } catch (NeonEndObjectException e) {
            throw new NeonException("Array data broken.");
        }
        return result;
    }

    @Override
    public char[] readCharArray() throws NeonException, NeonEndObjectException {
        if (nextIsNull())
            return null;
        int length = readInt();
        char[] result = new char[length];
        try {
            for (int i = 0; i < length; i++) {
                result[i] = readChar();
            }
        } catch (NeonEndObjectException e) {
            throw new NeonException("Array data broken.");
        }
        return result;
    }

    @Override
    public short[] readShortArray() throws NeonException, NeonEndObjectException {
        if (nextIsNull())
            return null;
        int length = readInt();
        short[] result = new short[length];
        try {
            for (int i = 0; i < length; i++) {
                result[i] = readShort();
            }
        } catch (NeonEndObjectException e) {
            throw new NeonException("Array data broken.");
        }
        return result;
    }

    @Override
    public boolean nextIsNull() throws NeonException {
        try {
            return SerializationUtils.readNullIndicator(mInput);
        } catch (IOException e) {
            throw new NeonException(e);
        }
    }

    @Override
    public int readObjectStart(int currentVersion) throws NeonException {
        try {
            int dataVer = mInput.readObjectStart();
            if (dataVer > currentVersion)
                throw new NeonException("Not supported forward compatibility data.");
            return dataVer;
        } catch (IOException e) {
            throw new NeonException(e);
        }
    }

    @Override
    public void readObjectEnd() throws NeonException {
        try {
            mInput.readObjectEnd();
        } catch (IOException e) {
            throw new NeonException(e);
        }
    }
}
