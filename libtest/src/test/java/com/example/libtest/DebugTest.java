package com.example.libtest;

import com.zalo.zing.customadapter.ZarcelPig;
import com.zalo.zing.extendClass.ZarcelChild;
import com.zing.zalo.helper.SerializableHelper;
import com.zing.zalo.data.serialization.SerializedByteBufferInput;
import com.zing.zalo.data.serialization.SerializedByteBufferOutput;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class DebugTest extends BaseTest {
    private final int NUM_LOOP = 2000;

    @Test
    public void debugTest() {
        ZarcelChild origin = new ZarcelChild();
        setZarcelChild(origin);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput(2000000);
        origin.serialize(writer);
        Map.Entry<ZarcelChild, String> result = null;
        try {
            result = SerializableHelper.deserialize(new SerializedByteBufferInput(writer.toByteArray()), ZarcelChild.CREATOR, true);
        } catch (Exception e) {
            Assert.fail();
        }
        System.out.println(result.getValue());
    }

    @Test
    public void debugTest2() {
        ZarcelChild origin = new ZarcelChild();
        setZarcelChild(origin);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput(1000000000);
        origin.serialize(writer);
        try {
            Map.Entry<ZarcelPig, String> result = SerializableHelper.deserialize(new SerializedByteBufferInput(writer.toByteArray()), ZarcelPig.CREATOR, true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        Assert.fail();
    }
}
