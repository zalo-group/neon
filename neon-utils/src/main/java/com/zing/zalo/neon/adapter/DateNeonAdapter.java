package com.zing.zalo.neon.adapter;

import com.zing.neon.data.serialization.SerializedInput;
import com.zing.neon.data.serialization.SerializedOutput;
import com.zing.zalo.neon.annotations.NonNull;
import com.zing.zalo.neon.helper.DebugBuilder;
import java.util.Date;

public class DateNeonAdapter implements NeonAdapter<Date> {

    @Override
    public void serialize(@NonNull Date object, SerializedOutput writer) {
        writer.writeInt64(object == null ? -1 : object.getTime());
    }

    @Override
    public Date createFromSerialized(SerializedInput reader, DebugBuilder builder) {
        if (builder != null) {
            builder.addType("timestamp", "long");
        }
        long ts = reader.readInt64();
        return ts < 0 ? null : new Date(ts);
    }
}
