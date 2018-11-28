package com.example.libtest;

import com.zalo.zing.log.Logger;
import com.zalo.zing.version.Data;
import com.zalo.zing.version.ZarcelBaseVersion;
import com.zalo.zing.version.ZarcelNewVersion;
import com.zing.zalo.helper.SerializableHelper;
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
        ZarcelNewVersion result = SerializableHelper.deserialize(input, ZarcelNewVersion.CREATOR, Logger.getInstance());
        assertZarcelVersion(origin, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void newVersion() {
        ZarcelNewVersion newVersion = new ZarcelNewVersion();
        newVersion.mCats = 2;
        newVersion.mRadius = 2.2f;
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        newVersion.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        try {
            SerializableHelper.deserialize(input, ZarcelBaseVersion.CREATOR, Logger.getInstance());
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
