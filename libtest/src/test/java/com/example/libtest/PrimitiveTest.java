package com.example.libtest;

import com.zalo.zing.log.Logger;
import com.zalo.zing.primitive.ZarcelPrimitive;
import com.zing.zalo.helper.SerializableHelper;
import com.zing.zalo.data.serialization.SerializedByteBufferInput;
import com.zing.zalo.data.serialization.SerializedByteBufferOutput;
import org.junit.Test;

import java.util.Map;

public class PrimitiveTest extends BaseTest {
    @Test
    public void primitiveTest() throws Exception {
        ZarcelPrimitive origin = new ZarcelPrimitive();
        setZarcelPrimitiveProperty(origin);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        origin.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        ZarcelPrimitive result = SerializableHelper.deserialize(input, ZarcelPrimitive.CREATOR, Logger.getInstance());
        assertZarcelPrimitive(origin, result);
    }
}
