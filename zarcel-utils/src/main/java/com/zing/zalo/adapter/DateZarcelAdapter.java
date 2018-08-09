package com.zing.zalo.adapter;

import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

import java.util.Date;

public class DateZarcelAdapter implements ZarcelAdapter<Date> {

    @Override
    public void serialize(Date object, SerializedOutput writer) {
        writer.writeInt64(object == null ? -1 : object.getTime());
    }

    @Override
    public Date createFromSerialized(SerializedInput reader) {
        long ts = reader.readInt64();
        return ts < 0 ? null : new Date(ts);
    }
}
