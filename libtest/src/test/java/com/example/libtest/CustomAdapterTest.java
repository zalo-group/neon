package com.example.libtest;

import com.zalo.zing.abstractAdapter.*;
import com.zalo.zing.customadapter.ZarcelCustomAnimal;
import com.zing.zalo.data.serialization.SerializedByteArrayInput;
import com.zing.zalo.data.serialization.SerializedByteArrayOutput;
import org.junit.Test;

public class CustomAdapterTest extends BaseTest {
    @Test
    public void customAdapter() {
        ZarcelCustomAnimal origin = new ZarcelCustomAnimal();
        setZarcelCustomAnimal(origin);

        SerializedByteArrayOutput writer = new SerializedByteArrayOutput();
        origin.serialize(writer);
        ZarcelCustomAnimal result =
                ZarcelCustomAnimal.CREATOR.createFromSerialized(new SerializedByteArrayInput(writer.toByteArray()));
        assertZarcelCustomAnimal(origin, result);
    }

    @Test
    public void abstractAdapter() {
        ZarcelAbstract origin = new ZarcelAbstract();
        origin.vehicle = new ZarcelCar();
        ((ZarcelCar) origin.vehicle).numberOfSeat = 100;
        ((ZarcelCar) origin.vehicle).maxSpeed = 50;
        //origin.vehicle.mType = ZarcelVehicle.CAR;

        SerializedByteArrayOutput writer = new SerializedByteArrayOutput();
        origin.serialize(writer);
        ZarcelAbstract result =
                ZarcelAbstract.CREATOR.createFromSerialized(new SerializedByteArrayInput(writer.toByteArray()));
        assertZarcelVehicle(origin.vehicle, result.vehicle);
    }
}
