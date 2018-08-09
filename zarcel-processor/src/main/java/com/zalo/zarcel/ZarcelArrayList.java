package com.zalo.zarcel;

import java.util.ArrayList;

public class ZarcelArrayList<T> extends ArrayList<T> {

    private ZarcelArrayList(ArrayList<T> m) {
        super(m);
    }

    public static final class Builder<U> {
        private ArrayList<U> mProp = new ArrayList<>();

        public Builder add(U property) {
            mProp.add(property);
            return this;
        }

        public ZarcelArrayList<U> build() {
            ZarcelArrayList<U> result = new ZarcelArrayList<>(mProp);
            mProp.clear();
            return result;
        }
    }
}
