package com.zing.zalo.neon2.internal.descriptor;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/**
 * Created by Tien Loc Bui on 11/09/2019.
 */
public enum ClassType {
    CLASS, ENUM, INTERFACE, NONE;

    static ClassType parse(Element element) {
        switch (element.getKind()) {
            case ENUM:
            case ENUM_CONSTANT:
                return ENUM;
            case CLASS:
                return CLASS;
            case INTERFACE:
                return INTERFACE;
            default:
                return NONE;
        }
    }
}