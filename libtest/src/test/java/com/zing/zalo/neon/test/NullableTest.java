package com.zing.zalo.neon.test;

import com.zing.neon.data.serialization.SerializedByteBufferInput;
import com.zing.neon.data.serialization.SerializedByteBufferOutput;
import com.zing.zalo.neon.helper.SerializableHelper;
import com.zing.zalo.neon.test.log.Logger;
import com.zing.zalo.neon.test.nullable.NeonNullable;
import org.junit.Test;

public class NullableTest extends BaseTest {
    @Test
    public void nullable_nonError() throws Exception {
        NeonNullable origin = new NeonNullable();
        setNeonNullable(origin, false);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        origin.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        NeonNullable result =
                SerializableHelper.deserialize(input, NeonNullable.CREATOR, Logger.getInstance());
        assertNeonNullable(origin, result);
    }

    @Test(expected = NullPointerException.class)
    public void nullable_error() {
        NeonNullable origin = new NeonNullable();
        setNeonNullable(origin, true);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        origin.serialize(writer);
        NeonNullable result =
                NeonNullable.CREATOR.createFromSerialized(
                        new SerializedByteBufferInput(writer.toByteArray()), null);
        assertNeonNullable(origin, result);
    }
}
