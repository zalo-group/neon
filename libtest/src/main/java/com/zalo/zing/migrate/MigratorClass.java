package com.zalo.zing.migrate;


import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Migrator;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel
@Migrator(MigratorAdapter.class)
public class MigratorClass implements Serializable {
    public int color;

    @Override
    public void serialize(SerializedOutput output) {
        MigratorClass__Zarcel.serialize(this, output);
    }

    public static Serializable.Creator<MigratorClass> CREATOR = new Creator<MigratorClass>() {
        @Nullable
        @Override
        public MigratorClass createFromSerialized(SerializedInput input) {
            try {
                MigratorClass result = new MigratorClass();
                MigratorClass__Zarcel.createFromSerialized(result, input);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
