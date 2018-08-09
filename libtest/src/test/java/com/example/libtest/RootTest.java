package com.example.libtest;

import com.zalo.zing.extendClass.ZarcelChild;
import com.zalo.zing.extendClass.ZarcelRoot;
import com.zing.zalo.data.serialization.SerializedByteArrayInput;
import com.zing.zalo.data.serialization.SerializedByteArrayOutput;
import org.junit.Test;

public class RootTest extends BaseTest {
    @Test
    public void root() {
        ZarcelRoot origin = new ZarcelRoot();
        setZarcelRoot(origin);

        SerializedByteArrayOutput writer = new SerializedByteArrayOutput();
        origin.serialize(writer);
        ZarcelRoot result =
                ZarcelRoot.CREATOR.createFromSerialized(new SerializedByteArrayInput(writer.toByteArray()));
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
