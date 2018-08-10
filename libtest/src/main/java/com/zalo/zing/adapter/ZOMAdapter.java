package com.zalo.zing.adapter;

import com.zalo.zing.node.*;
import com.zing.zalo.Exception.ZarcelDuplicateException;
import com.zing.zalo.Exception.ZarcelNotFoundException;
import com.zing.zalo.adapter.PolymorphismZarcelAdapter;

public class ZOMAdapter extends PolymorphismZarcelAdapter<ZOM> {
    @Override
    protected void onRegisterChildClasses() {
        try {
            register(1, ZOMText.class, RegisterType.ADD);
            register(2, ZOMImage.class, RegisterType.ADD);
            register(3, ZOMVideo.class, RegisterType.ADD);
            register(4, ZOMButton.class, RegisterType.ADD);
            register(5, ZOMContainer.class, RegisterType.ADD);
            register(6, ZOMSlider.class, RegisterType.ADD);
        } catch (ZarcelDuplicateException e) {
            e.printStackTrace();
        } catch (ZarcelNotFoundException e) {
            e.printStackTrace();
        }
    }
}
