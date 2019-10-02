//package com.zing.zalo.neon.test.version;
//
//import android.support.annotation.Nullable;
//import com.zing.neon.data.serialization.SerializedInput;
//import com.zing.neon.data.serialization.SerializedOutput;
//import com.zing.zalo.neon.annotations.Neon;
//import com.zing.zalo.neon.helper.DebugBuilder;
//import com.zing.zalo.neon.helper.NeonSerializable;
//
//@Neon(version = 3)
//public class NeonNewVersion implements NeonSerializable {
//
//    public int mCats;
//
//    @Neon.Property(sinceVersion = 1)
//    public float mRadius;
//
//    @Neon.Property(sinceVersion = 2)
//    public String mVersionName;
//
//    @Neon.Property(sinceVersion = 3)
//    public String mPetName;
//
//    public static NeonNewVersion createFromBaseVersion(NeonBaseVersion baseVersion) {
//        NeonNewVersion newVersion = new NeonNewVersion();
//        newVersion.mCats = baseVersion.mCats;
//        newVersion.mRadius = baseVersion.mRadius;
//        newVersion.mVersionName = baseVersion.versionName;
//        return newVersion;
//    }
//
//    @Override
//    public void serialize(SerializedOutput serializedOutput) {
//        NeonNewVersion__Neon.serialize(this, serializedOutput);
//    }
//
//    public static NeonSerializable.Creator<NeonNewVersion> CREATOR =
//            new NeonSerializable.Creator<NeonNewVersion>() {
//                @Nullable
//                @Override
//                public NeonNewVersion createFromSerialized(SerializedInput input,
//                        DebugBuilder builder) {
//                    try {
//                        NeonNewVersion result = new NeonNewVersion();
//                        NeonNewVersion__Neon.createFromSerialized(result, input, builder);
//                        return result;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }
//            };
//}