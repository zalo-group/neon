package com.zing.zalo.data.serialization;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * SerializedByteArrayOutput
 * Created by khanhtm on 10/05/2016.
 */
public class SerializedByteArrayOutput implements SerializedOutput {
    private ByteArrayOutputStream outbuf;
    private DataOutputStream out;

    public SerializedByteArrayOutput() {
        outbuf = new ByteArrayOutputStream();
        out = new DataOutputStream(outbuf);
    }

    public void cleanup() {
        try {
            if (outbuf != null) {
                outbuf.close();
                outbuf = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (out != null) {
                out.close();
                out = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeInt16(int x) {
        try {
            for(int i = 0 ; i < 2 ; i++) {
                out.write( x >> (i * 8));
            }
        } catch (Exception e) {
            throw new RuntimeException("writeInt16: write byte error", e);
        }
    }

    @Override
    public void writeInt32(int x) {
        try {
            for(int i = 0; i < 4; i++) {
                out.write(x >> (i * 8));
            }
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeInt64(long x) {
        try {
            for(int i = 0; i < 8; i++) {
                out.write((int)(x >> (i * 8)));
            }
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeBool(boolean value) {
        try {
            out.write(value ? 1 : 0);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeBytes(byte[] b) {
        try {
            out.write(b);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeBytes(byte[] b, int offset, int count) {
        try {
            out.write(b, offset, count);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeByte(int i) {
        try {
            out.writeByte((byte) i);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeByte(byte b) {
        try {
            out.writeByte(b);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeString(String s) {
        try {
            if (s==null) {
                writeInt32(-1);
                return;
            }
            if (s.isEmpty()) {
                writeInt32(0);
                return;
            }
            byte b[] = s.getBytes("UTF-8");
            writeByteArray(b);
        } catch (Exception e) {
            throw new RuntimeException("write byte error", e);
        }
    }

    @Override
    public void writeByteArray(byte[] b, int offset, int count) {
        try {
            writeInt32(count);
            out.write(b, offset, count);
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
        return outbuf!=null ? outbuf.toByteArray() : null;
    }
}
