//package com.zing.zalo.neon.test.abstractAdapter;
//
//import android.support.annotation.Nullable;
//import com.zing.neon.data.serialization.SerializedInput;
//import com.zing.neon.data.serialization.SerializedOutput;
//import com.zing.zalo.neon.annotations.Neon;
//import com.zing.zalo.neon.helper.DebugBuilder;
//import com.zing.zalo.neon.helper.NeonSerializable;
//
//@Neon
//public class NeonBike extends NeonVehicle implements NeonSerializable {
//
//    public int maxSpeed;
//
//    @Override
//    public void serialize(SerializedOutput serializedOutput) {
//        NeonBike__Neon.serialize(this, serializedOutput);
//    }
//
//    public static NeonSerializable.Creator<NeonBike> CREATOR =
//            new NeonSerializable.Creator<NeonBike>() {
//                @Nullable
//                @Override
//                public NeonBike createFromSerialized(SerializedInput input, DebugBuilder builder) {
//                    try {
//                        NeonBike result = new NeonBike();
//                        NeonBike__Neon.createFromSerialized(result, input, builder);
//                        return result;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }
//            };
//}
