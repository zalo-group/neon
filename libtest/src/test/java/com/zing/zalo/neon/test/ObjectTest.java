package com.zing.zalo.neon.test;

import com.zing.neon.data.serialization.SerializedByteBufferInput;
import com.zing.neon.data.serialization.SerializedByteBufferOutput;
import com.zing.zalo.neon.helper.SerializableHelper;
import com.zing.zalo.neon.test.object.NeonObject;
import org.junit.Test;

public class ObjectTest extends BaseTest {
    private final int NUM_LOOP = 2000;

    @Test
    public void object() throws Exception {
        NeonObject origin = new NeonObject();
        setNeonObjectValue(origin);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        origin.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        NeonObject result = SerializableHelper.deserialize(input, NeonObject.CREATOR, null);
        assertNeonObject(origin, result);
    }

    @Test
    public void timing() throws Exception {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < NUM_LOOP; i++) {
            object();
        }
        long end = System.currentTimeMillis() - begin;
        System.out.println("Converted: " + NUM_LOOP);
        System.out.println("Total time: " + String.valueOf(end) + "ms");
        System.out.println("Average time: " + String.valueOf(end / (float) NUM_LOOP) + "ms.");
    }
}
