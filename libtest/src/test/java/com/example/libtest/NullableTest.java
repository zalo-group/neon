package com.example.libtest;

import com.zalo.zing.nullable.ZarcelNullable;
import com.zing.zalo.data.serialization.SerializedByteArrayInput;
import com.zing.zalo.data.serialization.SerializedByteArrayOutput;
import org.junit.Test;

public class NullableTest extends BaseTest{
    @Test
    public void nullable_nonError() {
        ZarcelNullable origin = new ZarcelNullable();
        setZarcelNullable(origin,false);
        SerializedByteArrayOutput writer = new SerializedByteArrayOutput();
        origin.serialize(writer);
        ZarcelNullable result =
                ZarcelNullable.CREATOR.createFromSerialized(new SerializedByteArrayInput(writer.toByteArray()),null);
        assertZarcelNullable(origin,result);
    }

    @Test(expected = NullPointerException.class)
    public void nullable_error() {
        ZarcelNullable origin = new ZarcelNullable();
        setZarcelNullable(origin,true);
        SerializedByteArrayOutput writer = new SerializedByteArrayOutput();
        origin.serialize(writer);
        ZarcelNullable result =
                ZarcelNullable.CREATOR.createFromSerialized(new SerializedByteArrayInput(writer.toByteArray()),null);
        assertZarcelNullable(origin,result);
    }
}
