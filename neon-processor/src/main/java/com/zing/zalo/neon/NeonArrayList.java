package com.zing.zalo.neon;

import java.util.ArrayList;

public class NeonArrayList<T> extends ArrayList<T> {

    private NeonArrayList(ArrayList<T> m) {
        super(m);
    }

    public static final class Builder<U> {
        private ArrayList<U> mProp = new ArrayList<>();

        public Builder add(U property) {
            mProp.add(property);
            return this;
        }

        public NeonArrayList<U> build() {
            NeonArrayList<U> result = new NeonArrayList<>(mProp);
            mProp.clear();
            return result;
        }
    }
}
