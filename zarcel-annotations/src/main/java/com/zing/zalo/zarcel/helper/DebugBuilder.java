package com.zing.zalo.zarcel.helper;

public class DebugBuilder {
    private int mNestedObject;
    private int mNestedArray;
    private final int mMaxDepth;
    private int mCurrentByte;
    private String mIndentString;
    private StringBuilder mBuilder;

    public DebugBuilder(int indent, int maxDepth) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < indent; i++)
            builder.append(" ");
        mIndentString = builder.toString();
        mBuilder = new StringBuilder();
        mMaxDepth = maxDepth;
        mCurrentByte = 0;
        mNestedObject = 0;
        mNestedArray = 0;
    }

    public void addByte(int bytes) {
        if (mNestedObject > mMaxDepth)
            mCurrentByte += bytes;
    }

    public void beginObject(String objectName) {
        if (mNestedObject <= mMaxDepth)
            mBuilder.append(objectName).append(" {");
        mNestedObject++;
    }

    public void endObject() {
        if (mNestedObject - 1 == mMaxDepth) {
            beginNewLine();
            mBuilder.append(mCurrentByte).append(" bytes");
            mCurrentByte = 0;
        }
        mNestedObject--;
        if (mNestedObject <= mMaxDepth) {
            beginNewLine();
            mBuilder.append("}");
        }
    }

    public void addType(String attrName, String typeName) {
        if (mNestedObject > mMaxDepth) {
            addBytes(typeName);
        } else {
            beginNewLine();
            mBuilder.append(attrName).append(" : ").append(typeName);
        }
    }

    public void addIntAttr(String attrName, int value) {
        if (mNestedObject > mMaxDepth) {
            addBytes("int");
        } else {
            beginNewLine();
            mBuilder.append(attrName).append(" : ").append(value);
        }
    }

    public void addObject(String attrName) {
        if (mNestedObject <= mMaxDepth) {
            beginNewLine();
            mBuilder.append(attrName).append(" : ");
        }
    }

    public void endPrimitiveArray(String type, int size) {
        if (mNestedObject <= mMaxDepth) {
            mBuilder.append(type).append("[").append(size).append("]");
        } else {
            addBytes(type, size);
        }
    }

    public void beginObjectArray(String objectName) {
        if (mNestedObject <= mMaxDepth) {
            mBuilder.append(objectName).append(" [");
            mNestedArray++;
        }
    }

    public void endObjectArray() {
        if (mNestedObject <= mMaxDepth) {
            mNestedArray--;
            beginNewLine();
            mBuilder.append("]");
        }
    }

    public void endNullObject() {
        if (mNestedObject <= mMaxDepth) {
            mBuilder.append("null");
        }
    }

    public void onError() {
        mBuilder.append(" <<<<<<<<<<<<");
    }

    private void beginNewLine() {
        mBuilder.append('\n');
        addIndent();
    }

    private void addIndent() {
        for (int i = 0; i < mNestedObject; i++) {
            mBuilder.append(mIndentString);
        }
        for (int i = 0; i < mNestedArray; i++) {
            mBuilder.append(mIndentString);
        }
    }

    @Override
    public String toString() {
        return mBuilder.toString();
    }

    private void addBytes(String type) {
        switch (type) {
            case "boolean":
                mCurrentByte++;
                break;
            case "int":
            case "float":
                mCurrentByte += 4;
                break;
            case "double":
            case "long":
                mCurrentByte += 8;
                break;
        }
    }

    private void addBytes(String type, int size) {
        switch (type) {
            case "boolean":
                mCurrentByte += size;
                break;
            case "int":
            case "float":
                mCurrentByte += (4 * size);
                break;
            case "double":
            case "long":
                mCurrentByte += (8 * size);
                break;
        }
    }

}
