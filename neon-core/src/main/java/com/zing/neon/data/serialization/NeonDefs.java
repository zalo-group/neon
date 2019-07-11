package com.zing.neon.data.serialization;

class NeonDefs {
    static final byte TYPE_BYTE = 0;
    static final byte TYPE_BOOLEAN = 1;
    static final byte TYPE_SHORT = 2;
    static final byte TYPE_INT = 3;
    static final byte TYPE_LONG = 4;
    static final byte TYPE_DOUBLE = 5;
    static final byte TYPE_BYTE_ARRAY = 6;
    static final byte TYPE_STRING = 7;

    static final byte SUBTYPE_BYTE = 1;
    static final byte SUBTYPE_SHORT = 2;
    static final byte SUBTYPE_INT = 3;
    static final byte SUBTYPE_LONG = 4;

    static final byte SUBTYPE_ZERO = 5;
    static final byte SUBTYPE_NULL = 6;
    static final byte SUBTYPE_DEFAULT = 0;

    static final byte SIZE_BYTE = 1;
    static final byte SIZE_SHORT = 2;
    static final byte SIZE_INT = 4;
    static final byte SIZE_LONG = 8;

    private static final byte SUBTYPE_BITS = 3;
    private static final byte SUBTYPE_MASK = (1 << (SUBTYPE_BITS)) - 1;

    static byte makeHeader(byte type, byte subType) {
        return (byte) ((type << SUBTYPE_BITS) | subType);
    }

    static byte getHeaderType(byte header) {
        return (byte) (header >> SUBTYPE_BITS);
    }

    static byte getHeaderSubType(byte header) {
        return (byte) (header & SUBTYPE_MASK);
    }

    static String getTypeName(byte type) {
        switch (type) {
            case TYPE_BYTE:
                return "byte";
            case TYPE_BOOLEAN:
                return "boolean";
            case TYPE_SHORT:
                return "int16";
            case TYPE_INT:
                return "int32";
            case TYPE_LONG:
                return "int64";
            case TYPE_DOUBLE:
                return "double";
            case TYPE_BYTE_ARRAY:
                return "byte array";
            case TYPE_STRING:
                return "string";
            default:
                return null;
        }
    }
}
