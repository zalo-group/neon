package com.zing.zalo.neon.test.customadapter;

import android.support.annotation.Nullable;
import com.zing.neon.data.serialization.SerializedInput;
import com.zing.neon.data.serialization.SerializedOutput;
import com.zing.zalo.neon.adapter.DateNeonAdapter;
import com.zing.zalo.neon.annotations.Ignore;
import com.zing.zalo.neon.annotations.Neon;
import com.zing.zalo.neon.helper.DebugBuilder;
import com.zing.zalo.neon.helper.NeonSerializable;
import java.util.Date;

@Neon
public class Human implements NeonSerializable {
    public String name;
    public int age;
    @Neon.Custom(adapter = DateNeonAdapter.class)
    public Date birthdate;
    public Human[] children;
    public transient Human father;

    private int __hidden__;
    @Ignore
    public Math nonNeonObject;

    @Override
    public void serialize(SerializedOutput output) {
        Human__Neon.serialize(this, output);
    }

    public static NeonSerializable.Creator<Human> CREATOR = new NeonSerializable.Creator<Human>() {
        @Nullable
        @Override
        public Human createFromSerialized(SerializedInput input, DebugBuilder builder) {
            try {
                Human result = new Human();
                Human__Neon.createFromSerialized(result, input, builder);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
