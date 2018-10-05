package com.example.libtest;

import com.zalo.zing.migrate.MigratorClass;
import com.zalo.zing.primitive.ZarcelPrimitive;
import com.zing.zalo.data.serialization.SerializedByteArrayInput;
import com.zing.zalo.data.serialization.SerializedByteArrayOutput;
import org.junit.Assert;
import org.junit.Test;

public class MigratorTest extends BaseTest {
    @Test
    public void migrator() {
        MigratorClass origin = new MigratorClass();
        origin.color = 100;
        SerializedByteArrayOutput writer = new SerializedByteArrayOutput();
        origin.serialize(writer);
        // Migrator set color = MAX_VALUE
        MigratorClass result =
                MigratorClass.CREATOR.createFromSerialized(new SerializedByteArrayInput(writer.toByteArray()));
        Assert.assertEquals(Integer.MAX_VALUE, result.color);
    }
}
