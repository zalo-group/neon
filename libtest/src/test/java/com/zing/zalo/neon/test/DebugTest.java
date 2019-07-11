package com.zing.zalo.neon.test;

import com.zing.neon.data.serialization.SerializedByteBufferInput;
import com.zing.neon.data.serialization.SerializedByteBufferOutput;
import com.zing.zalo.neon.test.customadapter.NeonPig;
import com.zing.zalo.neon.test.extendClass.NeonChild;
import com.zing.zalo.neon.helper.SerializableHelper;
import com.zing.zalo.neon.test.log.Logger;
import org.junit.Assert;
import org.junit.Test;

public class DebugTest extends BaseTest {

    @Test
    public void debugTest() {
        NeonChild origin = new NeonChild();
        setNeonChild(origin);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput(2000000);
        origin.serialize(writer);
        try {
            SerializableHelper.deserialize(new SerializedByteBufferInput(writer.toByteArray()),
                    NeonChild.CREATOR, Logger.getInstance());
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test
    public void debugTest2() {
        NeonChild origin = new NeonChild();
        setNeonChild(origin);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput(1000000000);
        origin.serialize(writer);
        try {
            SerializableHelper.deserialize(new SerializedByteBufferInput(writer.toByteArray()),
                    NeonPig.CREATOR, Logger.getInstance());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }
        Assert.fail();
    }
}
