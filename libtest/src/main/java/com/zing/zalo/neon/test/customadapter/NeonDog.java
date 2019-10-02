//package com.zing.zalo.neon.test.customadapter;
//
//import android.support.annotation.Nullable;
//import com.zing.neon.data.serialization.SerializedInput;
//import com.zing.neon.data.serialization.SerializedOutput;
//import com.zing.zalo.neon.annotations.Neon;
//import com.zing.zalo.neon.helper.DebugBuilder;
//import com.zing.zalo.neon.helper.NeonSerializable;
//import com.zing.zalo.neon.test.primitive.NeonPrimitive;
//
//@Neon
//public class NeonDog extends NeonAnimal implements NeonSerializable {
//
//    public String foods;
//    public NeonPrimitive eatAny;
//
//    @Override
//    public void serialize(SerializedOutput serializedOutput) {
//        NeonDog__Neon.serialize(this, serializedOutput);
//    }
//
//    public static NeonSerializable.Creator<NeonDog> CREATOR =
//            new NeonSerializable.Creator<NeonDog>() {
//                @Nullable
//                @Override
//                public NeonDog createFromSerialized(SerializedInput input, DebugBuilder builder) {
//                    try {
//                        NeonDog result = new NeonDog();
//                        NeonDog__Neon.createFromSerialized(result, input, builder);
//                        return result;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }
//            };
//}
