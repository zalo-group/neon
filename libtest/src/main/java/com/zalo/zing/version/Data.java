package com.zalo.zing.version;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.DebugBuilder;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;


public class Data {
    @Zarcel
    public static class DataOld implements Serializable {
        public int y;

        @Override
        public void serialize(SerializedOutput serializedOutput) {
            DataOld__Zarcel.serialize(this, serializedOutput);
        }

        public static Serializable.Creator<DataOld> CREATOR = new Serializable.Creator<DataOld>() {
            @Nullable
            @Override
            public DataOld createFromSerialized(SerializedInput input, DebugBuilder builder) {
                DataOld result = new DataOld();
                DataOld__Zarcel.createFromSerialized(result, input, builder);
                return result;
            }
        };
    }

    @Zarcel(version = 2, compatibleSince = 2)
    public static class DataNewest implements Serializable {
        @Zarcel.Property(sinceVersion = 2)
        public int i;

        @Override
        public void serialize(SerializedOutput serializedOutput) {
            DataNewest__Zarcel.serialize(this, serializedOutput);
        }

        public static Serializable.Creator<DataNewest> CREATOR = new Serializable.Creator<DataNewest>() {
            @Nullable
            @Override
            public DataNewest createFromSerialized(SerializedInput input, DebugBuilder builder) {
                DataNewest result = new DataNewest();
                DataNewest__Zarcel.createFromSerialized(result, input, builder);
                return result;
            }
        };
    }
}
