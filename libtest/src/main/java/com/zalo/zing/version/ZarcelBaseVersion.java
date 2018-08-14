package com.zalo.zing.version;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel(version = 2)
public class ZarcelBaseVersion implements Serializable {

    public int mCats;

    @Zarcel.Property(sinceVersion = 1)
    public float mRadius;

    @Zarcel.Property(sinceVersion = 2)
    public String versionName;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelBaseVersion__Zarcel.serialize(this, serializedOutput);
    }

    public static Serializable.Creator<ZarcelBaseVersion> CREATOR = new Serializable.Creator<ZarcelBaseVersion>() {
        @Nullable
        @Override
        public ZarcelBaseVersion createFromSerialized(SerializedInput input) {
            ZarcelBaseVersion result = new ZarcelBaseVersion();
            ZarcelBaseVersion__Zarcel.createFromSerialized(result, input);
            return result;
        }
    };
}
