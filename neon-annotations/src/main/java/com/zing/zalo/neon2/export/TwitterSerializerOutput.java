package com.zing.zalo.neon2.export;

import com.twitter.serial.stream.SerializerInput;
import com.twitter.serial.stream.SerializerOutput;
import com.twitter.serial.stream.bytebuffer.ByteBufferSerial;
import com.twitter.serial.stream.bytebuffer.ByteBufferSerializerOutput;
import com.twitter.serial.util.SerializationUtils;
import com.zing.zalo.neon2.export.exception.NeonException;

import java.io.IOException;

/**
 * Created by Tien Loc Bui on 02/10/2019.
 */
public class TwitterSerializerOutput implements SerializedOutput {
    private SerializerOutput mOutput;

    public TwitterSerializerOutput() {
        mOutput = new ByteBufferSerializerOutput();
    }

    @Override
    public SerializedOutput writeByte(byte val) throws NeonException {
        try {
            mOutput.writeByte(val);
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeBoolean(boolean val) throws NeonException {
        try {
            mOutput.writeBoolean(val);
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeInt(int val) throws NeonException {
        try {
            mOutput.writeInt(val);
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeLong(long val) throws NeonException {
        try {
            mOutput.writeLong(val);
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeFloat(float val) throws NeonException {
        try {
            mOutput.writeFloat(val);
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeDouble(double val) throws NeonException {
        try {
            mOutput.writeDouble(val);
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeChar(char val) throws NeonException {
        try {
            mOutput.writeInt(val);
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeShort(short val) throws NeonException {
        try {
            mOutput.writeInt(val);
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeByteArray(byte[] val) throws NeonException {
        try {
            if (val == null) {
                mOutput.writeNull();
            } else {
                mOutput.writeInt(val.length);
                for (byte b : val) {
                    writeByte(b);
                }
            }
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeBooleanArray(boolean[] val) throws NeonException {
        try {
            if (val == null) {
                mOutput.writeNull();
            } else {
                mOutput.writeInt(val.length);
                for (boolean b : val) {
                    writeBoolean(b);
                }
            }
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeIntArray(int[] val) throws NeonException {
        try {
            if (val == null) {
                mOutput.writeNull();
            } else {
                mOutput.writeInt(val.length);
                for (int b : val) {
                    writeInt(b);
                }
            }
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeLongArray(long[] val) throws NeonException {
        try {
            if (val == null) {
                mOutput.writeNull();
            } else {
                mOutput.writeInt(val.length);
                for (long b : val) {
                    writeLong(b);
                }
            }
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeFloatArray(float[] val) throws NeonException {
        try {
            if (val == null) {
                mOutput.writeNull();
            } else {
                mOutput.writeInt(val.length);
                for (float b : val) {
                    writeFloat(b);
                }
            }
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeDoubleArray(double[] val) throws NeonException {
        try {
            if (val == null) {
                mOutput.writeNull();
            } else {
                mOutput.writeInt(val.length);
                for (double b : val) {
                    writeDouble(b);
                }
            }
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeCharArray(char[] val) throws NeonException {
        try {
            if (val == null) {
                mOutput.writeNull();
            } else {
                mOutput.writeInt(val.length);
                for (char b : val) {
                    writeChar(b);
                }
            }
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeShortArray(short[] val) throws NeonException {
        try {
            if (val == null) {
                mOutput.writeNull();
            } else {
                mOutput.writeInt(val.length);
                for (short b : val) {
                    writeShort(b);
                }
            }
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeObjectStart(int version) throws NeonException {
        try {
            mOutput.writeObjectStart(version);
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeObjectEnd() throws NeonException {
        try {
            mOutput.writeObjectEnd();
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }

    @Override
    public SerializedOutput writeNull() throws NeonException {
        try {
            mOutput.writeNull();
        } catch (IOException e) {
            throw new NeonException(e);
        }
        return this;
    }
    
    public byte[] toByteArray() {
        return ((ByteBufferSerializerOutput)mOutput).getSerializedData();
    }
}
