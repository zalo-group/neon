//package com.zing.zalo.neon.test.nullable;
//
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import com.zing.neon.data.serialization.SerializedInput;
//import com.zing.neon.data.serialization.SerializedOutput;
//import com.zing.zalo.neon.annotations.Neon;
//import com.zing.zalo.neon.helper.DebugBuilder;
//import com.zing.zalo.neon.helper.NeonSerializable;
//import com.zing.zalo.neon.test.primitive.NeonPrimitive;
//
//@Neon
//public class NeonNullable implements NeonSerializable {
//
//    @NonNull
//    public NeonPrimitive mPet;
//
//    @Nullable
//    public NeonPrimitive mOptionalPet;
//
//    @Override
//    public void serialize(SerializedOutput serializedOutput) {
//        NeonNullable__Neon.serialize(this, serializedOutput);
//    }
//
//    public static NeonSerializable.Creator<NeonNullable> CREATOR =
//            new NeonSerializable.Creator<NeonNullable>() {
//                @Nullable
//                @Override
//                public NeonNullable createFromSerialized(SerializedInput input,
//                        DebugBuilder builder) {
//                    try {
//                        NeonNullable result = new NeonNullable();
//                        NeonNullable__Neon.createFromSerialized(result, input, builder);
//                        return result;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }
//            };
//}
