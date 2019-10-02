//package com.zing.zalo.neon.test.customadapter;
//
//import android.support.annotation.Nullable;
//import com.zing.neon.data.serialization.SerializedInput;
//import com.zing.neon.data.serialization.SerializedOutput;
//import com.zing.zalo.neon.annotations.Neon;
//import com.zing.zalo.neon.helper.DebugBuilder;
//import com.zing.zalo.neon.helper.NeonSerializable;
//import com.zing.zalo.neon.test.object.NeonObjectOne;
//
//@Neon
//public class NeonCat extends NeonAnimal implements NeonSerializable {
//
//    public int[] foods;
//    public NeonObjectOne[] playable;
//
//    @Override
//    public void serialize(SerializedOutput serializedOutput) {
//        NeonCat__Neon.serialize(this, serializedOutput);
//    }
//
//    public static NeonSerializable.Creator<NeonCat> CREATOR =
//            new NeonSerializable.Creator<NeonCat>() {
//                @Nullable
//                @Override
//                public NeonCat createFromSerialized(SerializedInput input, DebugBuilder builder) {
//                    try {
//                        NeonCat result = new NeonCat();
//                        NeonCat__Neon.createFromSerialized(result, input, builder);
//                        return result;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }
//            };
//}
