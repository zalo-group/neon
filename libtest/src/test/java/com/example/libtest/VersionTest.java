package com.example.libtest;

import com.zalo.zing.version.ZarcelBaseVersion;
import com.zalo.zing.version.ZarcelNewVersion;
import com.zing.zalo.data.serialization.SerializedByteArrayInput;
import com.zing.zalo.data.serialization.SerializedByteArrayOutput;
import org.junit.Test;

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
}
