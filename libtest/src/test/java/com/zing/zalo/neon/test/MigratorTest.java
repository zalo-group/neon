package com.zing.zalo.neon.test;

import com.zing.neon.data.serialization.SerializedByteBufferInput;
import com.zing.neon.data.serialization.SerializedByteBufferOutput;
import com.zing.zalo.neon.test.migrate.MigratorClass;
import org.junit.Assert;
import org.junit.Test;

public class MigratorTest extends BaseTest {
    @Test
    public void migrator() {
        MigratorClass origin = new MigratorClass();
        origin.color = 100;
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        origin.serialize(writer);
        // Migrator set color = MAX_VALUE
        MigratorClass result =
                MigratorClass.CREATOR.createFromSerialized(
                        new SerializedByteBufferInput(writer.toByteArray()), null);
        Assert.assertEquals(Integer.MAX_VALUE, result.color);
    }
}
