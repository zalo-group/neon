package com.zing.neon.data.serialization;

import java.io.ByteArrayInputStream;

/**
 * SerializedByteArrayInput
 * Created by khanhtm on 10/05/2016.
 */
public class SerializedByteArrayInput implements SerializedInput {
    private ByteArrayInputStream in;
    private int position;
    private int length;

    public SerializedByteArrayInput(byte data[]) {
        in = new ByteArrayInputStream(data);
        position = 0;
        length = data.length;
    }

    public void cleanup() {
        try {
            if (in != null) {
                in.close();
                in = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int readByte() {
        try {
            int ret = in.read();
            ++position;
            return ret;
        } catch (Exception e) {
            throw new RuntimeException("read byte error", e);
        }
    }

    @Override
    public int readInt16() {
        try {
            int i = 0;
            for (int j = 0; j < 2; j++) {
                i |= (in.read() << (j * 8));
                position++;
            }
            return i;
        } catch (Exception e) {
            throw new RuntimeException("readInt16: read byte error", e);
        }
    }

    @Override
    public int readInt32() {
        try {
            int i = 0;
            for (int j = 0; j < 4; j++) {
                i |= (in.read() << (j * 8));
                position++;
            }
            return i;
        } catch (Exception e) {
            throw new RuntimeException("read byte error", e);
        }
    }

    @Override
    public boolean readBool() {
        return readByte() == 1;
    }

    @Override
    public long readInt64() {
        try {
            long i = 0;
            for (int j = 0; j < 8; j++) {
                i |= ((long) in.read() << (j * 8));
                position++;
            }
            return i;
        } catch (Exception e) {
            throw new RuntimeException("read byte error", e);
        }
    }

    @Override
    public String readString() {
        try {
            int l = readInt32();
            if (l < 0) return null;
            if (l == 0) return "";
            if (l > length) throw new RuntimeException("read byte length error");
            byte[] b = new byte[l];
            if (in.read(b) != l) {
                throw new RuntimeException("read byte error");
            }
            position += l;
            return new String(b, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("read string error", e);
        }
    }

    @Override
    public byte[] readByteArray() {
        try {
            int l = readInt32();
            if (l > length) throw new RuntimeException("read byte length error");
            byte[] b = new byte[l];
            if (in.read(b) != l) {
                throw new RuntimeException("read byte error");
            }
            position += l;
            return b;
        } catch (Exception e) {
            throw new RuntimeException("read byte error", e);
        }
    }

    @Override
    public double readDouble() {
        return Double.longBitsToDouble(readInt64());
    }

    @Override
    public void skip(int count) {
        if (count == 0) {
            return;
        }
        if (in != null) {
            try {
                in.skip(count);
                position += count;
            } catch (Exception e) {
                throw new RuntimeException("skip error", e);
            }
        }
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public int available() {
        return in.available();
    }

    @Override
    public boolean markSupported() {
        return in.markSupported();
    }

    @Override
    public void mark(int readlimit) {
        in.mark(readlimit);
        position = readlimit;
    }
}
