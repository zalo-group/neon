package com.zalo.zing.version;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.DebugBuilder;
import com.zing.zalo.data.serialization.ZarcelSerializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel(version = 2)
public class ZarcelBaseVersion implements ZarcelSerializable {

    public int mCats;

    @Zarcel.Property(sinceVersion = 1)
    public float mRadius;

    @Zarcel.Property(sinceVersion = 2)
    public String versionName;

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelBaseVersion__Zarcel.serialize(this, serializedOutput);
    }

    public static ZarcelSerializable.Creator<ZarcelBaseVersion> CREATOR = new ZarcelSerializable.Creator<ZarcelBaseVersion>() {
        @Nullable
        @Override
        public ZarcelBaseVersion createFromSerialized(SerializedInput input, DebugBuilder builder) {
            ZarcelBaseVersion result = new ZarcelBaseVersion();
            ZarcelBaseVersion__Zarcel.createFromSerialized(result, input, builder);
            return result;
        }
    };
}
