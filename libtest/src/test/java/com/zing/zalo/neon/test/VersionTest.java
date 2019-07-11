package com.zing.zalo.neon.test;

import com.zing.neon.data.serialization.SerializedByteBufferInput;
import com.zing.neon.data.serialization.SerializedByteBufferOutput;
import com.zing.zalo.neon.helper.SerializableHelper;
import com.zing.zalo.neon.test.log.Logger;
import com.zing.zalo.neon.test.version.Data;
import com.zing.zalo.neon.test.version.NeonBaseVersion;
import com.zing.zalo.neon.test.version.NeonNewVersion;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class VersionTest extends BaseTest {
    @Test
    public void versionTest() throws Exception {
        NeonBaseVersion baseVersion = new NeonBaseVersion();
        setNeonVersionProperty(baseVersion);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        baseVersion.serialize(writer);
        NeonNewVersion origin = NeonNewVersion.createFromBaseVersion(baseVersion);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        NeonNewVersion result =
                SerializableHelper.deserialize(input, NeonNewVersion.CREATOR, Logger.getInstance());
        assertNeonVersion(origin, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void newVersion() {
        NeonNewVersion newVersion = new NeonNewVersion();
        newVersion.mCats = 2;
        newVersion.mRadius = 2.2f;
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        newVersion.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        try {
            SerializableHelper.deserialize(input, NeonBaseVersion.CREATOR, Logger.getInstance());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getCause() instanceof IllegalArgumentException) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @Test
    public void outdatedBinary() {
        Data.DataOld old = new Data.DataOld();
        old.y = 4;
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        old.serialize(writer);
        try {
            Data.DataNewest newest = Data.DataNewest.CREATOR.createFromSerialized(
                    new SerializedByteBufferInput(writer.toByteArray()), null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Binary data of "
                    + "Data.DataNewest"
                    + " is outdated. You must re-serialize latest data.", e.getMessage());
        }
    }
}
