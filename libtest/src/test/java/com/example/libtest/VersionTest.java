package com.example.libtest;

import android.support.annotation.Nullable;
import com.zalo.zing.version.Data;
import com.zalo.zing.version.ZarcelBaseVersion;
import com.zalo.zing.version.ZarcelNewVersion;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.*;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class VersionTest extends BaseTest {
    @Test
    public void versionTest() {
        ZarcelBaseVersion baseVersion = new ZarcelBaseVersion();
        setZarcelVersionProperty(baseVersion);

        SerializedByteArrayOutput writer = new SerializedByteArrayOutput();
        baseVersion.serialize(writer);

        ZarcelNewVersion origin = ZarcelNewVersion.createFromBaseVersion(baseVersion);
        ZarcelNewVersion result =
                ZarcelNewVersion.CREATOR.createFromSerialized(new SerializedByteArrayInput(writer.toByteArray()));
        assertZarcelVersion(origin, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void newVersion() {
        ZarcelNewVersion newVersion = new ZarcelNewVersion();
        newVersion.mCats = 2;
        newVersion.mRadius = 2.2f;
        SerializedByteArrayOutput writer = new SerializedByteArrayOutput();
        newVersion.serialize(writer);
        ZarcelBaseVersion result =
                ZarcelBaseVersion.CREATOR.createFromSerialized(new SerializedByteArrayInput(writer.toByteArray()));
    }

    @Test
    public void outdatedBinary() {
        Data.DataOld old = new Data.DataOld();
        old.y = 4;
        SerializedByteArrayOutput writer = new SerializedByteArrayOutput();
        old.serialize(writer);
        try {
            Data.DataNewest newest = Data.DataNewest.CREATOR.createFromSerialized(new SerializedByteArrayInput(writer.toByteArray()));
            fail();
        } catch (IllegalArgumentException e) {
            assertEquals("Binary data of " + "Data.DataNewest" + " is outdated. You must re-serialize latest data.", e.getMessage());
        }
    }
}
