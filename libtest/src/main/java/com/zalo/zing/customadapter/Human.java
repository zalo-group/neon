package com.zalo.zing.customadapter;

import android.support.annotation.Nullable;
import com.zing.zalo.adapter.DateZarcelAdapter;
import com.zing.zalo.annotations.Ignore;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.helper.DebugBuilder;
import com.zing.zalo.helper.ZarcelSerializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

import java.util.Date;

@Zarcel
public class Human implements ZarcelSerializable {
    public String name;
    public int age;
    @Zarcel.Custom(adapter = DateZarcelAdapter.class)
    public Date birthdate;
    public Human[] children;
    public transient Human father;

    private int __hidden__;
    @Ignore
    public Math nonZarcelObject;

    @Override
    public void serialize(SerializedOutput output) {
        Human__Zarcel.serialize(this, output);
    }

    public static ZarcelSerializable.Creator<Human> CREATOR = new ZarcelSerializable.Creator<Human>() {
        @Nullable
        @Override
        public Human createFromSerialized(SerializedInput input, DebugBuilder builder) {
            try {
                Human result = new Human();
                Human__Zarcel.createFromSerialized(result, input, builder);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
