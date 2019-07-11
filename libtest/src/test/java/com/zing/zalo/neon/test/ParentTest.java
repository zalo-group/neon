package com.zing.zalo.neon.test;

import com.zing.neon.data.serialization.SerializedByteBufferInput;
import com.zing.neon.data.serialization.SerializedByteBufferOutput;
import com.zing.zalo.neon.test.extendClass.NeonChild;
import com.zing.zalo.neon.test.extendClass.NeonParent;
import com.zing.zalo.neon.helper.SerializableHelper;
import com.zing.zalo.neon.test.log.Logger;
import org.junit.Test;

public class ParentTest extends BaseTest {
    @Test
    public void parent() throws Exception {
        NeonParent origin = new NeonParent();
        setNeonRoot(origin);

        SerializedByteBufferOutput writer = new SerializedByteBufferOutput(2000000);
        origin.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        NeonParent result =
                SerializableHelper.deserialize(input, NeonParent.CREATOR, Logger.getInstance());
        assertNeonRoot(origin, result);
    }

    @Test
    public void child() throws Exception {
        NeonChild origin = new NeonChild();
        setNeonChild(origin);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput(2000000);
        origin.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        NeonChild result =
                SerializableHelper.deserialize(input, NeonChild.CREATOR, Logger.getInstance());
        assertNeonChild(origin, result);
    }
}
