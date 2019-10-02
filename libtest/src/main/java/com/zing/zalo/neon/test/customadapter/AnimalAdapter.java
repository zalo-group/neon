//package com.zing.zalo.neon.test.customadapter;
//
//import com.zing.neon.data.serialization.SerializedInput;
//import com.zing.neon.data.serialization.SerializedOutput;
//import com.zing.zalo.neon.adapter.NeonAdapter;
//import com.zing.zalo.neon.helper.DebugBuilder;
//
//public class AnimalAdapter implements NeonAdapter<NeonAnimal[]> {
//    @Override
//    public void serialize(NeonAnimal[] object, SerializedOutput writer) {
//        writer.writeInt32(object.length);
//        for (int i = 0; i < object.length; i++) {
//            NeonAnimal animal = object[i];
//            writer.writeInt32(animal.mType);
//            if (animal instanceof NeonPig) {
//                ((NeonPig) animal).serialize(writer);
//            } else if (animal instanceof NeonDog) {
//                ((NeonDog) animal).serialize(writer);
//            } else if (animal instanceof NeonCat) {
//                ((NeonCat) animal).serialize(writer);
//            }
//        }
//    }
//
//    @Override
//    public NeonAnimal[] createFromSerialized(SerializedInput reader, DebugBuilder builder) {
//        int size = reader.readInt32();
//        NeonAnimal[] result = new NeonAnimal[size];
//        for (int i = 0; i < size; i++) {
//            int type = reader.readInt32();
//            if (builder != null) {
//                builder.addCustomAttr("type", String.valueOf(type), 4);
//                builder.addObjectAttrName("NeonAnimal[" + i + "]");
//            }
//            if (type == NeonAnimal.CAT) {
//                result[i] = NeonCat.CREATOR.createFromSerialized(reader, builder);
//            } else if (type == NeonAnimal.DOG) {
//                result[i] = NeonDog.CREATOR.createFromSerialized(reader, builder);
//            } else if (type == NeonAnimal.PIG) {
//                result[i] = NeonPig.CREATOR.createFromSerialized(reader, builder);
//            }
//        }
//        return result;
//    }
//}
