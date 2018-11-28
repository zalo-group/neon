package com.example.libtest;

import com.zalo.zing.abstractAdapter.ZarcelAbstract;
import com.zalo.zing.abstractAdapter.ZarcelCar;
import com.zalo.zing.customadapter.ZarcelCustomAnimal;
import com.zing.zalo.helper.SerializableHelper;
import com.zing.zalo.data.serialization.SerializedByteBufferInput;
import com.zing.zalo.data.serialization.SerializedByteBufferOutput;
import org.junit.Test;

import java.util.Map;

public class CustomAdapterTest extends BaseTest {
    @Test
    public void customAdapter() throws Exception {
        ZarcelCustomAnimal origin = new ZarcelCustomAnimal();
        setZarcelCustomAnimal(origin);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput(2000000);
        origin.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        Map.Entry<ZarcelCustomAnimal, String> log = SerializableHelper.deserialize(input, ZarcelCustomAnimal.CREATOR, true);
        System.out.println(log.getValue());
        assertZarcelCustomAnimal(origin, log.getKey());
    }

    @Test
    public void abstractAdapter() throws Exception {
        ZarcelAbstract origin = new ZarcelAbstract();
        origin.vehicle = new ZarcelCar();
        ((ZarcelCar) origin.vehicle).numberOfSeat = 100;
        ((ZarcelCar) origin.vehicle).maxSpeed = 50;
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        origin.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        Map.Entry<ZarcelAbstract, String> log = SerializableHelper.deserialize(input, ZarcelAbstract.CREATOR, true);
        System.out.println(log.getValue());
        assertZarcelVehicle(origin.vehicle, log.getKey().vehicle);
    }
}
