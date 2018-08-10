package com.zalo.zing.abstractAdapter;

import com.zing.zalo.exception.ZarcelDuplicateException;
import com.zing.zalo.exception.ZarcelNotFoundException;
import com.zing.zalo.adapter.PolymorphismZarcelAdapter;

public class VehicleAdapter extends PolymorphismZarcelAdapter<ZarcelVehicle> {

    @Override
    protected void onRegisterChildClasses() {//onRegisterChildClasses
        try {
            register(ZarcelVehicle.CAR, ZarcelCar.class, RegisterType.ADD);
            register(ZarcelVehicle.BIKE, ZarcelBike.class, RegisterType.ADD);
        } catch (ZarcelNotFoundException e) {
            e.printStackTrace();
        } catch (ZarcelDuplicateException e) {
            e.printStackTrace();
        }
    }
}
