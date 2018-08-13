package com.zalo.zing.abstractAdapter;

import com.zing.zalo.exception.ZarcelDuplicateException;
import com.zing.zalo.exception.ZarcelNotFoundException;
import com.zing.zalo.adapter.PolymorphismZarcelAdapter;

public class VehicleAdapter extends PolymorphismZarcelAdapter<ZarcelVehicle> {

    @Override
    protected void onRegisterChildClasses() {//onRegisterChildClasses
        try {
            registryAdd(ZarcelVehicle.CAR, ZarcelCar.class);
            registryAdd(ZarcelVehicle.BIKE, ZarcelBike.class);
        } catch (ZarcelDuplicateException e) {
            e.printStackTrace();
        }
    }
}
