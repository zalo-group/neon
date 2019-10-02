package com.zing.zalo.neon2.internal.javapoet.field.factory;

import com.zing.zalo.neon2.internal.descriptor.FieldDescriptor;
import com.zing.zalo.neon2.internal.descriptor.FieldType;
import com.zing.zalo.neon2.internal.javapoet.field.NeonField;

/**
 * Created by Tien Loc Bui on 30/08/2019.
 */
public class NeonFieldFactory {
    // Create all field from here
    public static NeonField createField(FieldDescriptor descriptor) {
        if (descriptor == null)
            return null;
        FieldType fieldType = descriptor.type();
        switch (fieldType) {
            case INT:
                return new IntField(descriptor);
            case FLOAT:
                return new FloatField(descriptor);
            case DOUBLE:
                return new DoubleField(descriptor);
            case CHAR:
                return new CharField(descriptor);
            case BYTE:
                return new ByteField(descriptor);
            case BOOLEAN:
                return new BooleanField(descriptor);
            case SHORT:
                return new ShortField(descriptor);
            case LONG:
                return new LongField(descriptor);
            case OBJECT:
                return new ObjectField(descriptor);
            case INT_ARRAY:
                return new IntArrayField(descriptor);
            case FLOAT_ARRAY:
                return new FloatArrayField(descriptor);
            case DOUBLE_ARRAY:
                return new DoubleArrayField(descriptor);
            case CHAR_ARRAY:
                return new CharArrayField(descriptor);
            case BYTE_ARRAY:
                return new ByteArrayField(descriptor);
            case BOOLEAN_ARRAY:
                return new BooleanArrayField(descriptor);
            case SHORT_ARRAY:
                return new ShortArrayField(descriptor);
            case LONG_ARRAY:
                return new LongArrayField(descriptor);
            case OBJECT_ARRAY:
                return new ObjectArrayField(descriptor);
            default:
                return null;
        }
    }
}
