package com.zing.neon.data.serialization;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

public class SerializedByteBufferOutput implements SerializedOutput {
    private ByteBuffer out;
    private int capacity;
    private int maxCapacity = 1 << 19; // 512kB
    private int defaultSize = 2048;

    public SerializedByteBufferOutput() {
        out = ByteBuffer.allocate(defaultSize);
        capacity = defaultSize;
    }

    public SerializedByteBufferOutput(int maxCapacity) {
        this();
        this.maxCapacity = maxCapacity;
        if (maxCapacity < defaultSize) {
            out = ByteBuffer.allocate(maxCapacity);
            capacity = maxCapacity;
        }
    }

    SerializedByteBufferOutput(int maxCapacity, int defaultSize) {
        this.defaultSize = defaultSize;
        this.maxCapacity = maxCapacity;
        if (defaultSize > maxCapacity) {
            this.defaultSize = maxCapacity;
        }
        out = ByteBuffer.allocate(defaultSize);
        if (defaultSize <= 0) {
            capacity = 1;
        } else {
            capacity = defaultSize;
        }
    }

    public void cleanup() {
        try {
            if (out != null) {
                out.clear();
                out = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ensureCapacity(int sizeNeeded) {
        if (out.remaining() < sizeNeeded && out.capacity() < maxCapacity) {
            capacity *= 2;
            while (capacity - out.position() < sizeNeeded) {
                capacity *= 2;
            }
            if (capacity > maxCapacity) {
                capacity = maxCapacity;
            }
            out = ByteBuffer.allocate(capacity).put(out.array(), 0, out.position());
        }
    }

    @Override
    public void writeInt16(int x) {
        try {
            writeShortHeader(NeonDefs.TYPE_SHORT, x);
        } catch (Exception e) {
            throw new RuntimeException("writeInt16: write byte error", e);
        }
    }

    @Override
    public void writeInt32(int x) {
        try {
            writeIntHeader(NeonDefs.TYPE_INT, x);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeInt64(long x) {
        try {
            writeLongHeader(NeonDefs.TYPE_LONG, x);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeBool(boolean value) {
        try {
            writeBoolHeader(NeonDefs.TYPE_BOOLEAN, value);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeBytes(byte[] b) {
        try {
            writeBytesHeader(NeonDefs.TYPE_BYTE_ARRAY, b, 0, b.length);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeBytes(byte[] b, int offset, int length) {
        try {
            writeBytesHeader(NeonDefs.TYPE_BYTE_ARRAY, b, offset, length);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeByte(int i) {
        try {
            writeByteHeader(NeonDefs.TYPE_BYTE, (byte) i);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeByte(byte b) {
        try {
            writeByteHeader(NeonDefs.TYPE_BYTE, b);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeString(String s) {
        try {
            writeStringHeader(NeonDefs.TYPE_STRING, s);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeByteArray(byte[] b, int offset, int count) {
        try {
            writeBytes(b, offset, count);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeByteArray(byte[] b) {
        writeByteArray(b, 0, b.length);
    }

    @Override
    public void writeDouble(double d) {
        try {
            writeLongHeader(NeonDefs.TYPE_DOUBLE, Double.doubleToRawLongBits(d));
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    private void writeHeader(byte type) {
        ensureCapacity(NeonDefs.SIZE_BYTE);
        out.put(type);
    }

    private void writeByteHeader(byte type, byte value) {
        if (value == 0) {
            writeHeader(NeonDefs.makeHeader(type, NeonDefs.SUBTYPE_ZERO));
        } else {
            writeHeader(NeonDefs.makeHeader(type, NeonDefs.SUBTYPE_BYTE));
            ensureCapacity(NeonDefs.SIZE_BYTE);
            out.put(value);
        }
    }

    private void writeBoolHeader(byte type, boolean value) {
        if (value) {
            writeHeader(NeonDefs.makeHeader(type, NeonDefs.SUBTYPE_DEFAULT));
        } else {
            writeHeader(NeonDefs.makeHeader(type, NeonDefs.SUBTYPE_ZERO));
        }
    }

    private void writeShortHeader(byte type, short value) {
        if (value == 0) {
            writeHeader(NeonDefs.makeHeader(type, NeonDefs.SUBTYPE_ZERO));
        } else if ((value & 0xFF00) == 0) {
            writeByteHeader(type, (byte) value);
        } else {
            writeHeader(NeonDefs.makeHeader(type, NeonDefs.SUBTYPE_SHORT));
            ensureCapacity(NeonDefs.SIZE_SHORT);
            out.putShort(value);
        }
    }

    private void writeShortHeader(byte type, int value) {
        writeShortHeader(type, (short) value);
    }

    private void writeIntHeader(byte type, int value) {
        if (value == 0) {
            writeHeader(NeonDefs.makeHeader(type, NeonDefs.SUBTYPE_ZERO));
        } else if ((value & 0xFFFF0000) == 0) {
            writeShortHeader(type, value);
        } else {
            writeHeader(NeonDefs.makeHeader(type, NeonDefs.SUBTYPE_INT));
            ensureCapacity(NeonDefs.SIZE_INT);
            out.putInt(value);
        }
    }

    private void writeLongHeader(byte type, long value) {
        if (value == 0) {
            writeHeader(NeonDefs.makeHeader(type, NeonDefs.SUBTYPE_ZERO));
        } else if ((value & 0xFFFFFFFF00000000L) == 0) {
            writeIntHeader(type, (int) value);
        } else {
            writeHeader(NeonDefs.makeHeader(type, NeonDefs.SUBTYPE_LONG));
            ensureCapacity(NeonDefs.SIZE_LONG);
            out.putLong(value);
        }
    }

    private void writeBytesHeader(byte type, byte[] b, int offset, int length) {
        if (length == 0) {
            writeHeader(NeonDefs.makeHeader(type, NeonDefs.SUBTYPE_ZERO));
            return;
        } else {
            writeHeader(NeonDefs.makeHeader(type, NeonDefs.SUBTYPE_INT));
        }
        writeIntHeader(NeonDefs.TYPE_INT, length);
        ensureCapacity(length);
        out.put(b, offset, length);
    }

    private void writeStringHeader(byte type, String s) throws UnsupportedEncodingException {
        if (s == null) {
            writeHeader(NeonDefs.makeHeader(type, NeonDefs.SUBTYPE_NULL));
            return;
        }
        if (s.isEmpty()) {
            writeHeader(NeonDefs.makeHeader(type, NeonDefs.SUBTYPE_ZERO));
            return;
        }
        byte b[] = s.getBytes("UTF-8");
        writeBytesHeader(type, b, 0, b.length);
    }

    public byte[] toByteArray() {
        if (out == null) {
            return null;
        }
        return ByteBuffer.allocate(out.position()).put(out.array(), 0, out.position()).array();
    }
}
