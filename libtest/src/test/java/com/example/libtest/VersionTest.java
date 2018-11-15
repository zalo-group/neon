package com.example.libtest;

import com.zalo.zing.version.Data;
import com.zalo.zing.version.ZarcelBaseVersion;
import com.zalo.zing.version.ZarcelNewVersion;
import com.zing.zalo.data.serialization.SerializableHelper;
import com.zing.zalo.data.serialization.SerializedByteBufferInput;
import com.zing.zalo.data.serialization.SerializedByteBufferOutput;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class VersionTest extends BaseTest {
    @Test
    public void versionTest() throws Exception {
        ZarcelBaseVersion baseVersion = new ZarcelBaseVersion();
        setZarcelVersionProperty(baseVersion);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        baseVersion.serialize(writer);
        ZarcelNewVersion origin = ZarcelNewVersion.createFromBaseVersion(baseVersion);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        Map.Entry<ZarcelNewVersion, String> log = SerializableHelper.deserialize(input, ZarcelNewVersion.CREATOR, true);
        System.out.println(log.getValue());
        assertZarcelVersion(origin, log.getKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void newVersion() {
        ZarcelNewVersion newVersion = new ZarcelNewVersion();
        newVersion.mCats = 2;
        newVersion.mRadius = 2.2f;
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        newVersion.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        Map.Entry<ZarcelBaseVersion, String> log = null;
        try {
            SerializableHelper.deserialize(input, ZarcelBaseVersion.CREATOR, true);
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
            Data.DataNewest newest = Data.DataNewest.CREATOR.createFromSerialized(new SerializedByteBufferInput(writer.toByteArray()), null);
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Binary data of " + "Data.DataNewest" + " is outdated. You must re-serialize latest data.", e.getMessage());
        }
    }
}
