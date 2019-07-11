package com.zing.zalo.neon.helper;

public interface DebugBuilder {

    void addVersion(int version);

    void addByte(int bytes);

    void beginObject(String objectName);

    void endObject();

    void addType(String attrName, String typeName);

    void addObjectAttrName(String attrName);

    void endPrimitiveArray(String type, int size);

    void beginObjectArray(String objectName);

    void endObjectArray();

    void endNullObject();

    void addCustomAttr(String attrName, String attrValue, int attrSize);

    void onError();

    String getLog();

    interface Logger {
        void log(DebugBuilder builder);
    }
}
