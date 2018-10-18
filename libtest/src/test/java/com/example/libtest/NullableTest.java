package com.example.libtest;

import com.zalo.zing.nullable.ZarcelNullable;
import com.zing.zalo.data.serialization.SerializableHelper;
import com.zing.zalo.data.serialization.SerializedByteBufferInput;
import com.zing.zalo.data.serialization.SerializedByteBufferOutput;
import org.junit.Test;

import java.util.Map;

public class NullableTest extends BaseTest {
    @Test
    public void nullable_nonError() throws Exception {
        ZarcelNullable origin = new ZarcelNullable();
        setZarcelNullable(origin, false);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        origin.serialize(writer);
        SerializableHelper<ZarcelNullable> helper = new SerializableHelper<>();
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        Map.Entry<ZarcelNullable, String> log = helper.deserialize(input, ZarcelNullable.CREATOR, true);
        System.out.println(log.getValue());
        assertZarcelNullable(origin, log.getKey());
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
