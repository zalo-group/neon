package com.example.libtest;

import com.zalo.zing.abstractAdapter.ZarcelAbstract;
import com.zalo.zing.abstractAdapter.ZarcelCar;
import com.zalo.zing.abstractAdapter.ZarcelVehicle;
import com.zalo.zing.customadapter.ZarcelCustomAnimal;
import com.zalo.zing.log.Logger;
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
        ZarcelCustomAnimal result = SerializableHelper.deserialize(input, ZarcelCustomAnimal.CREATOR, Logger.getInstance());
        assertZarcelCustomAnimal(origin, result);
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
        ZarcelAbstract result = SerializableHelper.deserialize(input, ZarcelAbstract.CREATOR, Logger.getInstance());
        assertZarcelVehicle(origin.vehicle, result.vehicle);
    }
}
