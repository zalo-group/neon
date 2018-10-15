package com.zing.zalo.data.serialization;

import java.text.MessageFormat;

public class DebugBuilder {
    private int mNestedObject;
    private String mIndentString;
    private StringBuilder mBuilder;

    public DebugBuilder(int indent) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < indent; i++)
            builder.append(" ");
        mIndentString = builder.toString();
    }

    public void beginObject(String objectName) {
        mNestedObject++;
        mBuilder.append(objectName).append(" {");
    }

    public void endObject() {
        mNestedObject--;
        beginNewLine();
        mBuilder.append("}");
    }

    public void addType(String attrName, String typeName) {
        beginNewLine();
        mBuilder.append(attrName).append(" : ").append(typeName);
    }

    public void addObject(String attrName) {
        beginNewLine();
        mBuilder.append(attrName).append(" : ");
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
    }
}
