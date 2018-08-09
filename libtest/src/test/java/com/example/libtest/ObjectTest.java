package com.example.libtest;

import com.zalo.zing.object.ZarcelObject;
import com.zing.zalo.data.serialization.SerializedByteArrayInput;
import com.zing.zalo.data.serialization.SerializedByteArrayOutput;
import org.junit.Test;

public class ObjectTest extends BaseTest {
    @Test
    public void object() {
        ZarcelObject origin = new ZarcelObject();
        setZarcelObjectValue(origin);

        SerializedByteArrayOutput writer = new SerializedByteArrayOutput();
        origin.serialize(writer);
        ZarcelObject result =
                ZarcelObject.CREATOR.createFromSerialized(new SerializedByteArrayInput(writer.toByteArray()));
        assertZarcelObject(origin, result);
    }

}
