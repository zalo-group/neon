package com.example.libtest;

import com.zalo.zing.extendClass.ZarcelChild;
import com.zalo.zing.extendClass.ZarcelParent;
import com.zing.zalo.data.serialization.SerializableHelper;
import com.zing.zalo.data.serialization.SerializedByteBufferInput;
import com.zing.zalo.data.serialization.SerializedByteBufferOutput;
import org.junit.Test;

import java.util.Map;

public class ParentTest extends BaseTest {
    @Test
    public void parent() throws Exception {
        ZarcelParent origin = new ZarcelParent();
        setZarcelRoot(origin);

        SerializedByteBufferOutput writer = new SerializedByteBufferOutput(2000000);
        origin.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        Map.Entry<ZarcelParent, String> log = SerializableHelper.deserialize(input, ZarcelParent.CREATOR, true);
        System.out.println(log.getValue());
        assertZarcelRoot(origin, log.getKey());
    }

    @Test
    public void child() throws Exception {
        ZarcelChild origin = new ZarcelChild();
        setZarcelChild(origin);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput(2000000);
        origin.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        Map.Entry<ZarcelChild, String> log = SerializableHelper.deserialize(input, ZarcelChild.CREATOR, true);
        System.out.println(log.getValue());
        assertZarcelChild(origin, log.getKey());
    }
}
