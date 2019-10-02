//package com.zing.zalo.neon.helper;
//
//public class DebugBuilderImpl implements DebugBuilder {
//    private int mNestedObject;
//    private int mNestedArray;
//    private final int mMaxDepth;
//    private int mCurrentByte;
//    private String mIndentString;
//    private StringBuilder mBuilder;
//
//    public DebugBuilderImpl(int indent, int maxDepth) {
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < indent; i++)
//            builder.append(" ");
//        mIndentString = builder.toString();
//        mBuilder = new StringBuilder();
//        mMaxDepth = maxDepth;
//        mCurrentByte = 0;
//        mNestedObject = 0;
//        mNestedArray = 0;
//    }
//
//    @Override
//    public void addByte(int bytes) {
//        if (mNestedObject > mMaxDepth) {
//            mCurrentByte += bytes;
//        }
//    }
//
//    @Override
//    public void beginObject(String objectName) {
//        if (mNestedObject <= mMaxDepth) {
//            mBuilder.append(objectName).append(" {");
//        }
//        mNestedObject++;
//    }
//
//    @Override
//    public void endObject() {
//        if (mNestedObject - 1 == mMaxDepth) {
//            beginNewLine();
//            mBuilder.append(mCurrentByte).append(" bytes");
//            mCurrentByte = 0;
//        }
//        mNestedObject--;
//        if (mNestedObject <= mMaxDepth) {
//            beginNewLine();
//            mBuilder.append("}");
//        }
//    }
//
//    @Override
//    public void addObjectAttrName(String attrName) {
//        if (mNestedObject <= mMaxDepth) {
//            beginNewLine();
//            mBuilder.append(attrName).append(" : ");
//        }
//    }
//
//    @Override
//    public void addType(String attrName, String typeName) {
//        if (mNestedObject > mMaxDepth) {
//            addBytes(typeName);
//        } else {
//            beginNewLine();
//            mBuilder.append(attrName).append(" : ").append(typeName);
//        }
//    }
//
//    @Override
//    public void addVersion(int version) {
//        if (mNestedObject > mMaxDepth) {
//            addBytes("int");
//        } else {
//            beginNewLine();
//            mBuilder.append("version").append(" : ").append(version);
//        }
//    }
//
//    @Override
//    public void endPrimitiveArray(String type, int size) {
//        if (mNestedObject <= mMaxDepth) {
//            mBuilder.append(type).append("[").append(size).append("]");
//        } else {
//            addBytes(type, size);
//        }
//    }
//
//    @Override
//    public void beginObjectArray(String objectName) {
//        if (mNestedObject <= mMaxDepth) {
//            mBuilder.append(objectName).append(" [");
//            mNestedArray++;
//        }
//    }
//
//    @Override
//    public void endObjectArray() {
//        if (mNestedObject <= mMaxDepth) {
//            mNestedArray--;
//            beginNewLine();
//            mBuilder.append("]");
//        }
//    }
//
//    @Override
//    public void endNullObject() {
//        if (mNestedObject <= mMaxDepth) {
//            mBuilder.append("null");
//        }
//    }
//
//    @Override
//    public void addCustomAttr(String attrName, String attrValue, int attrSize) {
//        if (mNestedObject > mMaxDepth) {
//            mCurrentByte += attrSize;
//        } else {
//            beginNewLine();
//            mBuilder.append(attrName).append(" : ").append(attrValue);
//        }
//    }
//
//    @Override
//    public void onError() {
//        mBuilder.append(" <<<<<<<<<<<<");
//    }
//
//    @Override
//    public String toString() {
//        return mBuilder.toString();
//    }
//
//    @Override
//    public String getLog() {
//        return mBuilder.toString();
//    }
//
//    private void beginNewLine() {
//        mBuilder.append('\n');
//        addIndent();
//    }
//
//    private void addIndent() {
//        for (int i = 0; i < mNestedObject; i++) {
//            mBuilder.append(mIndentString);
//        }
//        for (int i = 0; i < mNestedArray; i++) {
//            mBuilder.append(mIndentString);
//        }
//    }
//
//    private void addBytes(String type) {
//        switch (type) {
//            case "boolean":
//                mCurrentByte++;
//                break;
//            case "int":
//            case "float":
//                mCurrentByte += 4;
//                break;
//            case "double":
//            case "long":
//                mCurrentByte += 8;
//                break;
//        }
//    }
//
//    private void addBytes(String type, int size) {
//        switch (type) {
//            case "boolean":
//                mCurrentByte += size;
//                break;
//            case "int":
//            case "float":
//                mCurrentByte += (4 * size);
//                break;
//            case "double":
//            case "long":
//                mCurrentByte += (8 * size);
//                break;
//        }
//    }
//}
//
