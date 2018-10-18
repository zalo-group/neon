package com.example.libtest;

import com.zalo.zing.object.ZarcelObject;
import com.zing.zalo.data.serialization.SerializedByteArrayInput;
import com.zing.zalo.data.serialization.SerializedByteArrayOutput;
import com.zing.zalo.data.serialization.SerializedByteBufferInput;
import com.zing.zalo.data.serialization.SerializedByteBufferOutput;
import org.junit.Ignore;
import org.junit.Test;

public class ObjectTest extends BaseTest {
    private final int NUM_LOOP = 2000;

    @Test
    public void object() {
        ZarcelObject origin = new ZarcelObject();
        setZarcelObjectValue(origin);

        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        origin.serialize(writer);
        ZarcelObject result =
                ZarcelObject.CREATOR.createFromSerialized(new SerializedByteBufferInput(writer.toByteArray()),null);
        assertZarcelObject(origin, result);
    }

    @Test
    @Ignore
    public void timing() {
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
