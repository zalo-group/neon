package com.example.libtest;

import com.zalo.zing.primitive.ZarcelPrimitive;
import com.zing.zalo.data.serialization.SerializedByteArrayInput;
import com.zing.zalo.data.serialization.SerializedByteArrayOutput;
import org.junit.Test;

public class PrimitiveTest extends BaseTest {
    @Test
    public void primitiveTest() {
        ZarcelPrimitive origin = new ZarcelPrimitive();
        setZarcelPrimitiveProperty(origin);
        SerializedByteArrayOutput writer = new SerializedByteArrayOutput();
        origin.serialize(writer);
        ZarcelPrimitive result =
                ZarcelPrimitive.CREATOR.createFromSerialized(new SerializedByteArrayInput(writer.toByteArray()));
        assertZarcelPrimitive(origin, result);
    }
}
