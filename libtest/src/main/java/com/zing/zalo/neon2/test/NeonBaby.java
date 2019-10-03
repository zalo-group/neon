package com.zing.zalo.neon2.test;

import android.support.annotation.Nullable;

import com.zing.zalo.neon2.annotations.NeonObject;

import java.util.Objects;

/**
 * Created by Tien Loc Bui on 23/09/2019.
 */
@NeonObject
public class NeonBaby {
    NeonBaby generation2;
    int age;

    NeonBaby(NeonBaby generation2, int age) {
        this.generation2 = generation2;
        this.age = age;
    }

    NeonBaby() {
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof NeonBaby) {
            return this.age == ((NeonBaby) obj).age &&
                    ((this.generation2 == null && ((NeonBaby) obj).generation2 == null) ||
                            (this.generation2 != null && this.generation2.equals(((NeonBaby) obj).generation2)));
        }
        return super.equals(obj);
    }
}
