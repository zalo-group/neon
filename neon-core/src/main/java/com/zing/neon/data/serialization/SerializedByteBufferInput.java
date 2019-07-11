package com.zing.neon.data.serialization;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * A 200% faster version of {@link SerializedByteArrayInput}.
 */
public class SerializedByteBufferInput implements SerializedInput {

    private ByteBuffer in;
    private int length;

    public SerializedByteBufferInput(byte data[]) {
        in = ByteBuffer.wrap(data);
        length = data.length;
    }

    public void cleanup() {
        try {
            if (in != null) {
                in.clear();
                in = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int readByte() {
        try {
            return (int) readByteHeader();
        } catch (Exception e) {
            throw new RuntimeException("read byte error", e);
        }
    }

    @Override
    public int readInt16() {
        try {
            return (int) readShortHeader();
        } catch (Exception e) {
            throw new RuntimeException("readInt16: read byte error", e);
        }
    }

    @Override
    public int readInt32() {
        try {
            return readIntHeader();
        } catch (Exception e) {
            throw new RuntimeException("read byte error", e);
        }
    }

    @Override
    public boolean readBool() {
        return readBoolHeader();
    }

    @Override
    public long readInt64() {
        try {
            return readLongHeader();
        } catch (Exception e) {
            throw new RuntimeException("read byte error", e);
        }
    }

    @Override
    public String readString() {
        try {
            return readStringHeader();
        } catch (Exception e) {
            throw new RuntimeException("read string error", e);
        }
    }

    @Override
    public byte[] readByteArray() {
        try {
            return readBytesHeader();
        } catch (Exception e) {
            throw new RuntimeException("read byte error", e);
        }
    }

    @Override
    public double readDouble() {
        return readDoubleHeader();
    }

    @Override
    public void skip(int count) {
        if (count == 0) {
            return;
        }
        if (in != null) {
            try {
                in.position(in.position() + count);
            } catch (Exception e) {
                throw new RuntimeException("skip error", e);
            }
        }
    }

    /**
     * @param expectedType expected type.
     * @return subtype when expectedType == obtainedType
     */
    public byte readHeader(byte expectedType) {
        byte obtainedType = in.get();
        if (NeonDefs.getHeaderType(obtainedType) == expectedType) {
            return NeonDefs.getHeaderSubType(obtainedType);
        }
        throw new RuntimeException("Expected type: "
                + NeonDefs.getTypeName(expectedType)
                + ", but serialization type: "
                + NeonDefs
                .getTypeName(obtainedType));
    }

    private boolean readBoolHeader() {
        byte subType = readHeader(NeonDefs.TYPE_BOOLEAN);
        if (subType == NeonDefs.SUBTYPE_ZERO) {
            return false;
        }
        return true;
    }

    private byte readByteHeader() {
        byte subtype = readHeader(NeonDefs.TYPE_BYTE);
        if (subtype == NeonDefs.SUBTYPE_ZERO) {
            return 0;
        }
        return in.get();
    }

    private short readShortHeader() {
        byte subtype = readHeader(NeonDefs.TYPE_SHORT);
        switch (subtype) {
            case NeonDefs.SUBTYPE_ZERO:
                return 0;
            case NeonDefs.SUBTYPE_BYTE:
                return (short) (in.get() & 0x00FF);
            default:
                return in.getShort();
        }
    }

    private int readIntHeader() {
        byte subtype = readHeader(NeonDefs.TYPE_INT);
        switch (subtype) {
            case NeonDefs.SUBTYPE_ZERO:
                return 0;
            case NeonDefs.SUBTYPE_BYTE:
                return (in.get() & 0x000000FF);
            case NeonDefs.SUBTYPE_SHORT:
                return (in.getShort() & 0x0000FFFF);
            default:
                return in.getInt();
        }
    }

    private long readLongHeader() {
        byte subtype = readHeader(NeonDefs.TYPE_LONG);
        switch (subtype) {
            case NeonDefs.SUBTYPE_ZERO:
                return 0;
            case NeonDefs.SUBTYPE_BYTE:
                return (in.get() & 0x00000000000000FFL);
            case NeonDefs.SUBTYPE_SHORT:
                return (in.getShort() & 0x000000000000FFFFL);
            case NeonDefs.SUBTYPE_INT:
                return (in.getInt() & 0x00000000FFFFFFFFL);
            default:
                return in.getLong();
        }
    }

    private double readDoubleHeader() {
        byte subtype = readHeader(NeonDefs.TYPE_DOUBLE);
        long result;
        switch (subtype) {
            case NeonDefs.SUBTYPE_ZERO:
                result = 0;
                break;
            case NeonDefs.SUBTYPE_BYTE:
                result = (in.get() & 0x00000000000000FFL);
                break;
            case NeonDefs.SUBTYPE_SHORT:
                result = (in.getShort() & 0x000000000000FFFFL);
                break;
            case NeonDefs.SUBTYPE_INT:
                result = (in.getInt() & 0x00000000FFFFFFFFL);
                break;
            default:
                result = in.getLong();
                break;
        }
        return Double.longBitsToDouble(result);
    }

    private byte[] readBytesHeader() {
        byte subtype = readHeader(NeonDefs.TYPE_BYTE_ARRAY);
        switch (subtype) {
            case NeonDefs.SUBTYPE_ZERO:
                return new byte[0];
            default:
                int length = readIntHeader();
                if (length > this.length) throw new RuntimeException("read byte length error");
                byte[] result = new byte[length];
                in.get(result, 0, length);
                return result;
        }
    }

    private String readStringHeader() throws UnsupportedEncodingException {
        byte subtype = readHeader(NeonDefs.TYPE_STRING);
        switch (subtype) {
            case NeonDefs.SUBTYPE_ZERO:
                return "";
            case NeonDefs.SUBTYPE_NULL:
                return null;
            default:
                int length = readIntHeader();
                if (length > this.length) throw new RuntimeException("read byte length error");
                byte[] result = new byte[length];
                in.get(result, 0, length);
                return new String(result, "UTF-8");
        }
    }

    @Override
    public int getPosition() {
        return in.position();
    }

    @Override
    public int available() {
        return length;
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public void mark(int readlimit) {

    }
}
