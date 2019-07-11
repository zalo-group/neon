package com.zing.zalo.neon.test.migrate;

import android.support.annotation.Nullable;
import com.zing.neon.data.serialization.SerializedInput;
import com.zing.neon.data.serialization.SerializedOutput;
import com.zing.zalo.neon.annotations.Migrator;
import com.zing.zalo.neon.annotations.Neon;
import com.zing.zalo.neon.helper.DebugBuilder;
import com.zing.zalo.neon.helper.NeonSerializable;

@Neon
@Migrator(MigratorAdapter.class)
public class MigratorClass implements NeonSerializable {
    public int color;

    @Override
    public void serialize(SerializedOutput output) {
        MigratorClass__Neon.serialize(this, output);
    }

    public static NeonSerializable.Creator<MigratorClass> CREATOR = new Creator<MigratorClass>() {
        @Nullable
        @Override
        public MigratorClass createFromSerialized(SerializedInput input, DebugBuilder builder) {
            try {
                MigratorClass result = new MigratorClass();
                MigratorClass__Neon.createFromSerialized(result, input, builder);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}
