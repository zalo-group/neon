package com.zing.zalo.data.serialization;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * A 200% faster version of {@link SerializedByteArrayOutput}.
 */
public class SerializedByteBufferOutput implements SerializedOutput {
    private ByteBuffer out;
    private int capacity;
    private int maxCapacity = 1 << 19; // 512kB
    private int defaultSize = 1024;


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
        if (defaultSize > maxCapacity)
            this.defaultSize = maxCapacity;
        out = ByteBuffer.allocate(defaultSize);
        if (defaultSize <= 0)
            capacity = 1;
        else
            capacity = defaultSize;

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
            while (capacity - out.position() < sizeNeeded)
                capacity *= 2;
            if (capacity > maxCapacity) {
                capacity = maxCapacity;
            }
            out = ByteBuffer.allocate(capacity).put(out.array(), 0, out.position());
        }
    }

    @Override
    public void writeInt16(int x) {
        try {
            writeShortHeader(ZarcelDefs.TYPE_SHORT, x);
        } catch (Exception e) {
            throw new RuntimeException("writeInt16: write byte error", e);
        }
    }

    @Override
    public void writeInt32(int x) {
        try {
            writeIntHeader(ZarcelDefs.TYPE_INT, x);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeInt64(long x) {
        try {
            writeLongHeader(ZarcelDefs.TYPE_LONG, x);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeBool(boolean value) {
        try {
            writeBoolHeader(ZarcelDefs.TYPE_BOOLEAN, value);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeBytes(byte[] b) {
        try {
            writeBytesHeader(ZarcelDefs.TYPE_BYTE_ARRAY, b, 0, b.length);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeBytes(byte[] b, int offset, int length) {
        try {
            writeBytesHeader(ZarcelDefs.TYPE_BYTE_ARRAY, b, offset, length);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeByte(int i) {
        try {
            writeByteHeader(ZarcelDefs.TYPE_BYTE, (byte) i);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeByte(byte b) {
        try {
            writeByteHeader(ZarcelDefs.TYPE_BYTE, b);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeString(String s) {
        try {
            writeStringHeader(ZarcelDefs.TYPE_STRING, s);
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
            writeLongHeader(ZarcelDefs.TYPE_DOUBLE, Double.doubleToRawLongBits(d));
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    private void writeHeader(byte type) {
        ensureCapacity(ZarcelDefs.SIZE_BYTE);
        out.put(type);
    }

    private void writeByteHeader(byte type, byte value) {
        if (value == 0) {
            writeHeader(ZarcelDefs.makeHeader(type, ZarcelDefs.SUBTYPE_ZERO));
        } else {
            writeHeader(ZarcelDefs.makeHeader(type, ZarcelDefs.SUBTYPE_BYTE));
            ensureCapacity(ZarcelDefs.SIZE_BYTE);
            out.put(value);
        }
    }

    private void writeBoolHeader(byte type, boolean value) {
        if (value)
            writeHeader(ZarcelDefs.makeHeader(type, ZarcelDefs.SUBTYPE_DEFAULT));
        else
            writeHeader(ZarcelDefs.makeHeader(type, ZarcelDefs.SUBTYPE_ZERO));
    }

    private void writeShortHeader(byte type, short value) {
        if (value == 0) {
            writeHeader(ZarcelDefs.makeHeader(type, ZarcelDefs.SUBTYPE_ZERO));
        } else if ((value & 0xFF00) == 0) {
            writeByteHeader(type, (byte) value);
        } else {
            writeHeader(ZarcelDefs.makeHeader(type, ZarcelDefs.SUBTYPE_SHORT));
            ensureCapacity(ZarcelDefs.SIZE_SHORT);
            out.putShort(value);
        }
    }

    private void writeShortHeader(byte type, int value) {
        writeShortHeader(type, (short) value);
    }

    private void writeIntHeader(byte type, int value) {
        if (value == 0) {
            writeHeader(ZarcelDefs.makeHeader(type, ZarcelDefs.SUBTYPE_ZERO));
        } else if ((value & 0xFFFF0000) == 0) {
            writeShortHeader(type, value);
        } else {
            writeHeader(ZarcelDefs.makeHeader(type, ZarcelDefs.SUBTYPE_INT));
            ensureCapacity(ZarcelDefs.SIZE_INT);
            out.putInt(value);
        }
    }

    private void writeLongHeader(byte type, long value) {
        if (value == 0) {
            writeHeader(ZarcelDefs.makeHeader(type, ZarcelDefs.SUBTYPE_ZERO));
        } else if ((value & 0xFFFFFFFF00000000L) == 0) {
            writeIntHeader(type, (int) value);
        } else {
            writeHeader(ZarcelDefs.makeHeader(type, ZarcelDefs.SUBTYPE_LONG));
            ensureCapacity(ZarcelDefs.SIZE_LONG);
            out.putLong(value);
        }
    }

    private void writeBytesHeader(byte type, byte[] b, int offset, int length) {
        if (length == 0) {
            writeHeader(ZarcelDefs.makeHeader(type, ZarcelDefs.SUBTYPE_ZERO));
            return;
        } else {
            writeHeader(ZarcelDefs.makeHeader(type, ZarcelDefs.SUBTYPE_INT));
        }
        writeIntHeader(ZarcelDefs.TYPE_INT, length);
        ensureCapacity(length);
        out.put(b, offset, length);
    }

    private void writeStringHeader(byte type, String s) throws UnsupportedEncodingException {
        if (s == null) {
            writeHeader(ZarcelDefs.makeHeader(type, ZarcelDefs.SUBTYPE_NULL));
            return;
        }
        if (s.isEmpty()) {
            writeHeader(ZarcelDefs.makeHeader(type, ZarcelDefs.SUBTYPE_ZERO));
            return;
        }
        byte b[] = s.getBytes("UTF-8");
        writeBytesHeader(type, b, 0, b.length);
    }

    public byte[] toByteArray() {
        if (out == null)
            return null;
        return ByteBuffer.allocate(out.position()).put(out.array(), 0, out.position()).array();
    }
}
