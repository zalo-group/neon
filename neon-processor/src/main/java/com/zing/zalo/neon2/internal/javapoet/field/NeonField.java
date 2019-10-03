package com.zing.zalo.neon2.internal.javapoet.field;

import static com.squareup.javapoet.MethodSpec.Builder;

import com.zing.zalo.neon2.internal.descriptor.ClassDescriptor;
import com.zing.zalo.neon2.internal.descriptor.FieldDescriptor;

import javax.annotation.processing.Messager;

/**
 * Created by Tien Loc Bui on 30/08/2019.
 */
public interface NeonField {

    /**
     * Gets descriptor.
     *
     * @return the field descriptor
     */
    FieldDescriptor getDescriptor();

    /**
     * Prepare valid data and use writer to add serialize field statement.
     *
     * @param messager                print log if needed
     * @param writer                  {@link Builder} add code statement to specific method.
     * @param classDescriptor         the class descriptor that can get some class information.
     * @param serializeOutputVariable variable name of
     *                                {@link com.zing.zalo.neon2.export.SerializedOutput} defined in current method.
     * @param objectVariable          variable name of current object of {@link ClassDescriptor}
     */
    void writeSerializeCode(Messager messager, Builder writer, String serializeOutputVariable, String objectVariable, ClassDescriptor classDescriptor);

    /**
     * Prepare valid data and use writer to add deserialize field statement.
     *
     * @param messager               print log if needed
     * @param writer                 {@link Builder} add code statement to specific method.
     * @param classDescriptor        the class descriptor that can get some class information.
     * @param serializeInputVariable variable name of
     *                               {@link com.zing.zalo.neon2.export.SerializedInput} defined in current method.
     * @param objectVariable         variable name of current object of {@link ClassDescriptor}
     */
    void writeDeserializeCode(Messager messager, Builder writer, String serializeInputVariable, String objectVariable, ClassDescriptor classDescriptor);
}
