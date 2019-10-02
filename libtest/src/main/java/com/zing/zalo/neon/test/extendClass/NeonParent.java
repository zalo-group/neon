//package com.zing.zalo.neon.test.extendClass;
//
//import android.support.annotation.Nullable;
//import com.zing.neon.data.serialization.SerializedInput;
//import com.zing.neon.data.serialization.SerializedOutput;
//import com.zing.zalo.neon.annotations.Neon;
//import com.zing.zalo.neon.test.customadapter.AnimalAdapter;
//import com.zing.zalo.neon.test.customadapter.NeonAnimal;
//import com.zing.zalo.neon.helper.DebugBuilder;
//import com.zing.zalo.neon.helper.NeonSerializable;
//
//@Neon(version = 3)
//public class NeonParent implements NeonSerializable {
//
//    @Neon.Property(sinceVersion = 1)
//    public int x;
//
//    @Neon.Property(sinceVersion = 1)
//    public int y;
//
//    @Neon.Property(sinceVersion = 1)
//    public int z;
//
//    @Neon.Property(sinceVersion = 3)
//    @Neon.Custom(adapter = AnimalAdapter.class)
//    public NeonAnimal[] animals;
//
//    @Override
//    public void serialize(SerializedOutput serializedOutput) {
//        NeonParent__Neon.serialize(this, serializedOutput);
//    }
//
//    public static NeonSerializable.Creator<NeonParent> CREATOR =
//            new NeonSerializable.Creator<NeonParent>() {
//                @Nullable
//                @Override
//                public NeonParent createFromSerialized(SerializedInput input,
//                        DebugBuilder builder) {
//                    try {
//                        NeonParent result = new NeonParent();
//                        NeonParent__Neon.createFromSerialized(result, input, builder);
//                        return result;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }
//            };
//}
