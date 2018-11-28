package com.example.libtest;

import com.zalo.zing.extendClass.ZarcelChild;
import com.zalo.zing.extendClass.ZarcelParent;
import com.zalo.zing.log.Logger;
import com.zing.zalo.helper.SerializableHelper;
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
        ZarcelParent result = SerializableHelper.deserialize(input, ZarcelParent.CREATOR, Logger.getInstance());
        assertZarcelRoot(origin, result);
    }

    @Test
    public void child() throws Exception {
        ZarcelChild origin = new ZarcelChild();
        setZarcelChild(origin);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput(2000000);
        origin.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        ZarcelChild result = SerializableHelper.deserialize(input, ZarcelChild.CREATOR, Logger.getInstance());
        assertZarcelChild(origin, result);
    }
}
