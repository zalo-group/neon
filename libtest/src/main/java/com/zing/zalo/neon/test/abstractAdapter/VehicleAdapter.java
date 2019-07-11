package com.zing.zalo.neon.test.abstractAdapter;

import com.zing.zalo.neon.adapter.PolymorphismNeonAdapter;
import com.zing.zalo.neon.exception.NeonDuplicateException;

public class VehicleAdapter extends PolymorphismNeonAdapter<NeonVehicle> {

    @Override
    protected void onRegisterChildClasses() {//onRegisterChildClasses
        try {
            registryAdd(NeonVehicle.CAR, NeonCar.class);
            registryAdd(NeonVehicle.BIKE, NeonBike.class);
        } catch (NeonDuplicateException e) {
            e.printStackTrace();
        }
    }
}
