package com.zalo.zing.customadapter;

import android.support.annotation.Nullable;
import com.zing.zalo.adapter.DateZarcelAdapter;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

import java.util.Date;

@Zarcel
public class Human implements Serializable {
    public String name;
    public int age;
    @Zarcel.Custom(adapter = DateZarcelAdapter.class)
    public Date birthdate;
    public Human[] children;
    public transient Human father;

    @Override
    public void serialize(SerializedOutput output) {
        Human$Zarcel.serialize(this, output);
    }

    public static Serializable.Creator<Human> CREATOR = new Serializable.Creator<Human>() {
        @Nullable
        @Override
        public Human createFromSerialized(SerializedInput input) {
            try {
                Human result = new Human();
                Human$Zarcel.createFromSerialized(result, input);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
