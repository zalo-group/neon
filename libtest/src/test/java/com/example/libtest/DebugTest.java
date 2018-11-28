package com.example.libtest;

import com.zalo.zing.customadapter.ZarcelPig;
import com.zalo.zing.extendClass.ZarcelChild;
import com.zalo.zing.log.Logger;
import com.zing.zalo.helper.SerializableHelper;
import com.zing.zalo.data.serialization.SerializedByteBufferInput;
import com.zing.zalo.data.serialization.SerializedByteBufferOutput;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class DebugTest extends BaseTest {

    @Test
    public void debugTest() {
        ZarcelChild origin = new ZarcelChild();
        setZarcelChild(origin);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput(2000000);
        origin.serialize(writer);
        try {
            SerializableHelper.deserialize(new SerializedByteBufferInput(writer.toByteArray()), ZarcelChild.CREATOR, Logger.getInstance());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void debugTest2() {
        ZarcelChild origin = new ZarcelChild();
        setZarcelChild(origin);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput(1000000000);
        origin.serialize(writer);
        try {
            SerializableHelper.deserialize(new SerializedByteBufferInput(writer.toByteArray()), ZarcelPig.CREATOR, Logger.getInstance());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        Assert.fail();
    }
}
