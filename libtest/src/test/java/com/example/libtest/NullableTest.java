package com.example.libtest;

import com.zalo.zing.log.Logger;
import com.zalo.zing.nullable.ZarcelNullable;
import com.zing.zalo.data.serialization.SerializedByteBufferInput;
import com.zing.zalo.data.serialization.SerializedByteBufferOutput;
import com.zing.zalo.helper.SerializableHelper;
import org.junit.Test;

public class NullableTest extends BaseTest {
    @Test
    public void nullable_nonError() throws Exception {
        ZarcelNullable origin = new ZarcelNullable();
        setZarcelNullable(origin, false);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        origin.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        ZarcelNullable result = SerializableHelper.deserialize(input, ZarcelNullable.CREATOR, Logger.getInstance());
        assertZarcelNullable(origin, result);
    }

    @Test(expected = NullPointerException.class)
    public void nullable_error() {
        ZarcelNullable origin = new ZarcelNullable();
        setZarcelNullable(origin, true);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        origin.serialize(writer);
        ZarcelNullable result =
                ZarcelNullable.CREATOR.createFromSerialized(new SerializedByteBufferInput(writer.toByteArray()), null);
        assertZarcelNullable(origin, result);
    }
}
