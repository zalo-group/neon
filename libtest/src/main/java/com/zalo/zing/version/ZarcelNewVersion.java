package com.zalo.zing.version;

import android.support.annotation.Nullable;
import com.zing.zalo.annotations.Zarcel;
import com.zing.zalo.data.serialization.Serializable;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

@Zarcel(version = 3)
public class ZarcelNewVersion implements Serializable {

    public int mCats;

    @Zarcel.Property(sinceVersion = 1)
    public float mRadius;

    @Zarcel.Property(sinceVersion = 2)
    public String mVersionName;

    @Zarcel.Property(sinceVersion = 3)
    public String mPetName;

    public static ZarcelNewVersion createFromBaseVersion(ZarcelBaseVersion baseVersion) {
        ZarcelNewVersion newVersion = new ZarcelNewVersion();
        newVersion.mCats = baseVersion.mCats;
        newVersion.mRadius = baseVersion.mRadius;
        newVersion.mVersionName = baseVersion.versionName;
        return newVersion;
    }

    @Override
    public void serialize(SerializedOutput serializedOutput) {
        ZarcelNewVersion$Zarcel.serialize(this, serializedOutput);
    }

    public static Serializable.Creator<ZarcelNewVersion> CREATOR = new Serializable.Creator<ZarcelNewVersion>() {
        @Nullable
        @Override
        public ZarcelNewVersion createFromSerialized(SerializedInput input) {
            try {
                ZarcelNewVersion result = new ZarcelNewVersion();
                ZarcelNewVersion$Zarcel.createFromSerialized(result, input);
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    };
}