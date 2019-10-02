package com.zing.zalo.neon2.internal.javapoet.cls;

import com.zing.zalo.neon2.internal.descriptor.ClassDescriptor;
import com.zing.zalo.neon2.internal.javapoet.field.NeonField;

import java.util.List;

import javax.annotation.processing.Filer;

/**
 * Created by Tien Loc Bui on 30/08/2019.
 */
public interface NeonClass {
    void writeToFile(Filer filer);

    ClassDescriptor getDescriptor();

    List<NeonField> getFields();
}
