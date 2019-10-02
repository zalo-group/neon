//package com.zing.zalo.neon.test.object;
//
//import android.support.annotation.Nullable;
//import com.zing.neon.data.serialization.SerializedInput;
//import com.zing.neon.data.serialization.SerializedOutput;
//import com.zing.zalo.neon.annotations.Neon;
//import com.zing.zalo.neon.helper.DebugBuilder;
//import com.zing.zalo.neon.helper.NeonSerializable;
//
//@Neon(version = 2)
//public class NeonObjectTwo implements NeonSerializable {
//
//    @Neon.Property(sinceVersion = 1)
//    public int mSize;
//
//    @Neon.Property(sinceVersion = 2)
//    public int[] mElement;
//
//    @Override
//    public void serialize(SerializedOutput serializedOutput) {
//        NeonObjectTwo__Neon.serialize(this, serializedOutput);
//    }
//
//    public static Creator<NeonObjectTwo> CREATOR = new Creator<NeonObjectTwo>() {
//        @Nullable
//        @Override
//        public NeonObjectTwo createFromSerialized(SerializedInput input, DebugBuilder builder) {
//            try {
//                NeonObjectTwo result = new NeonObjectTwo();
//                NeonObjectTwo__Neon.createFromSerialized(result, input, builder);
//                return result;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    };
//}
