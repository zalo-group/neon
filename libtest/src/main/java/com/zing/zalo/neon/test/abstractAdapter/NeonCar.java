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
//public class NeonCar extends NeonVehicle implements NeonSerializable {
//
//    public int maxSpeed;
//    public int numberOfSeat;
//
//    @Override
//    public void serialize(SerializedOutput serializedOutput) {
//        NeonCar__Neon.serialize(this, serializedOutput);
//    }
//
//    public static NeonSerializable.Creator<NeonCar> CREATOR =
//            new NeonSerializable.Creator<NeonCar>() {
//                @Nullable
//                @Override
//                public NeonCar createFromSerialized(SerializedInput input, DebugBuilder builder) {
//                    try {
//                        NeonCar result = new NeonCar();
//                        NeonCar__Neon.createFromSerialized(result, input, builder);
//                        return result;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }
//            };
//}
