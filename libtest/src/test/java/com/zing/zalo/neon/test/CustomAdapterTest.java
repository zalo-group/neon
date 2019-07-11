package com.zing.zalo.neon.test;

import com.zing.neon.data.serialization.SerializedByteBufferInput;
import com.zing.neon.data.serialization.SerializedByteBufferOutput;
import com.zing.zalo.neon.test.abstractAdapter.NeonAbstract;
import com.zing.zalo.neon.test.abstractAdapter.NeonCar;
import com.zing.zalo.neon.test.customadapter.NeonCustomAnimal;
import com.zing.zalo.neon.helper.SerializableHelper;
import com.zing.zalo.neon.test.log.Logger;
import org.junit.Test;

public class CustomAdapterTest extends BaseTest {
    @Test
    public void customAdapter() throws Exception {
        NeonCustomAnimal origin = new NeonCustomAnimal();
        setNeonCustomAnimal(origin);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput(2000000);
        origin.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        NeonCustomAnimal
                result = SerializableHelper.deserialize(input, NeonCustomAnimal.CREATOR,
                Logger.getInstance());
        assertNeonCustomAnimal(origin, result);
    }

    @Test
    public void abstractAdapter() throws Exception {
        NeonAbstract origin = new NeonAbstract();
        origin.vehicle = new NeonCar();
        ((NeonCar) origin.vehicle).numberOfSeat = 100;
        ((NeonCar) origin.vehicle).maxSpeed = 50;
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        origin.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        NeonAbstract result =
                SerializableHelper.deserialize(input, NeonAbstract.CREATOR, Logger.getInstance());
        assertNeonVehicle(origin.vehicle, result.vehicle);
    }
}
