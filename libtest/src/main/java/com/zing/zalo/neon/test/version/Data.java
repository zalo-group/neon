package com.zing.zalo.neon.test.version;

import android.support.annotation.Nullable;
import com.zing.neon.data.serialization.SerializedInput;
import com.zing.neon.data.serialization.SerializedOutput;
import com.zing.zalo.neon.annotations.Neon;
import com.zing.zalo.neon.helper.DebugBuilder;
import com.zing.zalo.neon.helper.NeonSerializable;

public class Data {
    @Neon
    public static class DataOld implements NeonSerializable {
        public int y;

        @Override
        public void serialize(SerializedOutput serializedOutput) {
            DataOld__Neon.serialize(this, serializedOutput);
        }

        public static NeonSerializable.Creator<DataOld> CREATOR =
                new NeonSerializable.Creator<DataOld>() {
                    @Nullable
                    @Override
                    public DataOld createFromSerialized(SerializedInput input,
                            DebugBuilder builder) {
                        DataOld result = new DataOld();
                        DataOld__Neon.createFromSerialized(result, input, builder);
                        return result;
                    }
                };
    }

    @Neon(version = 2, compatibleSince = 2)
    public static class DataNewest implements NeonSerializable {
        @Neon.Property(sinceVersion = 2)
        public int i;

        @Override
        public void serialize(SerializedOutput serializedOutput) {
            DataNewest__Neon.serialize(this, serializedOutput);
        }

        public static NeonSerializable.Creator<DataNewest> CREATOR =
                new NeonSerializable.Creator<DataNewest>() {
                    @Nullable
                    @Override
                    public DataNewest createFromSerialized(SerializedInput input,
                            DebugBuilder builder) {
                        DataNewest result = new DataNewest();
                        DataNewest__Neon.createFromSerialized(result, input, builder);
                        return result;
                    }
                };
    }
}
