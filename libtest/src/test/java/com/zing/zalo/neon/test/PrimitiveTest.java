package com.zing.zalo.neon.test;

import com.zing.neon.data.serialization.SerializedByteBufferInput;
import com.zing.neon.data.serialization.SerializedByteBufferOutput;
import com.zing.zalo.neon.helper.SerializableHelper;
import com.zing.zalo.neon.test.log.Logger;
import com.zing.zalo.neon.test.primitive.NeonPrimitive;
import org.junit.Test;

public class PrimitiveTest extends BaseTest {
    @Test
    public void primitiveTest() throws Exception {
        NeonPrimitive origin = new NeonPrimitive();
        setNeonPrimitiveProperty(origin);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        origin.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        NeonPrimitive result =
                SerializableHelper.deserialize(input, NeonPrimitive.CREATOR, Logger.getInstance());
        assertNeonPrimitive(origin, result);
    }
}
