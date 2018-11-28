package com.example.libtest;

import com.zalo.zing.log.Logger;
import com.zalo.zing.object.ZarcelObject;
import com.zing.zalo.helper.SerializableHelper;
import com.zing.zalo.data.serialization.SerializedByteBufferInput;
import com.zing.zalo.data.serialization.SerializedByteBufferOutput;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Map;

public class ObjectTest extends BaseTest {
    private final int NUM_LOOP = 2000;

    @Test
    public void object() throws Exception {
        ZarcelObject origin = new ZarcelObject();
        setZarcelObjectValue(origin);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        origin.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        ZarcelObject result = SerializableHelper.deserialize(input, ZarcelObject.CREATOR, Logger.getInstance());
        assertZarcelObject(origin, result);
    }

    @Test
    @Ignore
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
