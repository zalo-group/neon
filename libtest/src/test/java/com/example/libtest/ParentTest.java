package com.example.libtest;

import com.zalo.zing.extendClass.ZarcelChild;
import com.zalo.zing.extendClass.ZarcelParent;
import com.zing.zalo.data.serialization.SerializedByteArrayInput;
import com.zing.zalo.data.serialization.SerializedByteArrayOutput;
import org.junit.Test;

public class ParentTest extends BaseTest {
    @Test
    public void parent() {
        ZarcelParent origin = new ZarcelParent();
        setZarcelRoot(origin);

        SerializedByteArrayOutput writer = new SerializedByteArrayOutput();
        origin.serialize(writer);
        ZarcelParent result =
                ZarcelParent.CREATOR.createFromSerialized(new SerializedByteArrayInput(writer.toByteArray()));
        assertZarcelRoot(origin, result);
    }

    @Test
    public void child() {
        ZarcelChild origin = new ZarcelChild();
        setZarcelChild(origin);
        SerializedByteArrayOutput writer = new SerializedByteArrayOutput();
        origin.serialize(writer);
        ZarcelChild result =
                ZarcelChild.CREATOR.createFromSerialized(new SerializedByteArrayInput(writer.toByteArray()));
        assertZarcelChild(origin, result);
    }
}
