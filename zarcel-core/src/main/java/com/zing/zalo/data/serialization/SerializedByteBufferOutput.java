package com.zing.zalo.data.serialization;

import java.nio.ByteBuffer;

/**
 * A 200% faster version of {@link SerializedByteArrayOutput}.
 */
public class SerializedByteBufferOutput implements SerializedOutput {
    private ByteBuffer out;
    private int capacity = 0;
    private int maxCapacity = 1 << 19; // 512kB
    private final int INT16_NEEDED = 2;
    private final int INT32_NEEDED = 4;
    private final int INT64_NEEDED = 8;
    private final int BYTE_NEEDED = 1;
    private final int DEFAULT_SIZE = 1024;


    public SerializedByteBufferOutput() {
        out = ByteBuffer.allocate(DEFAULT_SIZE);
        capacity = DEFAULT_SIZE;
    }

    public SerializedByteBufferOutput(int maxCapacity) {
        out = ByteBuffer.allocate(DEFAULT_SIZE);
        capacity = DEFAULT_SIZE;
        this.maxCapacity = maxCapacity;
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
            ensureCapacity(INT16_NEEDED);
            for (int i = 0; i < 2; i++) {
                out.put((byte) (x >> (i * 8)));
            }
        } catch (Exception e) {
            throw new RuntimeException("writeInt16: write byte error", e);
        }
    }

    @Override
    public void writeInt32(int x) {
        try {
            ensureCapacity(INT32_NEEDED);
            for (int i = 0; i < 4; i++) {
                out.put((byte) (x >> (i * 8)));
            }
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeInt64(long x) {
        try {
            ensureCapacity(INT64_NEEDED);
            for (int i = 0; i < 8; i++) {
                out.put((byte) (x >> (i * 8)));
            }
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeBool(boolean value) {
        try {
            ensureCapacity(BYTE_NEEDED);
            out.put((byte) (value ? 1 : 0));
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeBytes(byte[] b) {
        try {
            ensureCapacity(b.length);
            writeBytes(b, 0, b.length);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeBytes(byte[] b, int offset, int count) {
        try {
            ensureCapacity(count);
            writeInt32(count);
            out.put(b, offset, count);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeByte(int i) {
        try {
            ensureCapacity(BYTE_NEEDED);
            out.put((byte) i);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeByte(byte b) {
        try {
            ensureCapacity(BYTE_NEEDED);
            out.put(b);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeString(String s) {
        try {
            if (s == null) {
                writeInt32(-1);
                return;
            }
            if (s.isEmpty()) {
                writeInt32(0);
                return;
            }
            byte b[] = s.getBytes("UTF-8");
            writeBytes(b, 0, b.length);
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
            writeInt64(Double.doubleToRawLongBits(d));
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    public byte[] toByteArray() {
        if (out == null)
            return null;
        return ByteBuffer.allocate(out.position()).put(out.array(), 0, out.position()).array();
    }
}
