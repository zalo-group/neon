//package com.zing.zalo.neon.test.version;
//
//import android.support.annotation.Nullable;
//import com.zing.neon.data.serialization.SerializedInput;
//import com.zing.neon.data.serialization.SerializedOutput;
//import com.zing.zalo.neon.annotations.Neon;
//import com.zing.zalo.neon.helper.DebugBuilder;
//import com.zing.zalo.neon.helper.NeonSerializable;
//
//@Neon(version = 2)
//public class NeonBaseVersion implements NeonSerializable {
//
//    public int mCats;
//
//    @Neon.Property(sinceVersion = 1)
//    public float mRadius;
//
//    @Neon.Property(sinceVersion = 2)
//    public String versionName;
//
//    @Override
//    public void serialize(SerializedOutput serializedOutput) {
//        NeonBaseVersion__Neon.serialize(this, serializedOutput);
//    }
//
//    public static NeonSerializable.Creator<NeonBaseVersion> CREATOR =
//            new NeonSerializable.Creator<NeonBaseVersion>() {
//                @Nullable
//                @Override
//                public NeonBaseVersion createFromSerialized(SerializedInput input,
//                        DebugBuilder builder) {
//                    NeonBaseVersion result = new NeonBaseVersion();
//                    NeonBaseVersion__Neon.createFromSerialized(result, input, builder);
//                    return result;
//                }
//            };
//}
