package com.zalo.zing.abstractAdapter;

import com.zing.zalo.Exception.ZarcelDuplicateException;
import com.zing.zalo.Exception.ZarcelNotFoundException;
import com.zing.zalo.adapter.AbstractZarcelAdapter;

public class VehicleAdapter extends AbstractZarcelAdapter<ZarcelVehicle> {

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
