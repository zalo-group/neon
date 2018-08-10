//package com.zalo.zing.adapter;
//
//import com.zalo.zing.node.*;
//import com.zing.zalo.adapter.ZarcelAdapter;
//import com.zing.zalo.annotations.NonNull;
//import com.zing.zalo.data.serialization.SerializedInput;
//import com.zing.zalo.data.serialization.SerializedOutput;
//
//public class ZOMArrayAdapter implements ZarcelAdapter<ZOM[]> {
//    @Override
//    public void serialize(@NonNull ZOM[] object, SerializedOutput writer) {
//        writer.writeInt32(object.length);
//        for (ZOM zom : object) {
//            writer.writeInt32(zom.mType);
//            zom.serialize(writer);
//        }
//    }
//
//    @Override
//    public ZOM[] createFromSerialized(SerializedInput reader) {
//        int size = reader.readInt32();
//        ZOM[] mChildren = new ZOM[size];
//        for (int i = 0; i < size; i++) {
//            int type = reader.readInt32();
//            switch (type) {
//                case 1:
//                    mChildren[i] = ZOMText.CREATOR.createFromSerialized(reader);
//                    break;
//                case 2:
//                    mChildren[i] = ZOMImage.CREATOR.createFromSerialized(reader);
//                    break;
//                case 3:
//                    mChildren[i] = ZOMVideo.CREATOR.createFromSerialized(reader);
//                    break;
//                case 4:
//                    mChildren[i] = ZOMButton.CREATOR.createFromSerialized(reader);
//                    break;
//                case 5:
//                    mChildren[i] = ZOMContainer.CREATOR.createFromSerialized(reader);
//                    break;
//                case 6:
//                    mChildren[i] = ZOMSlider.CREATOR.createFromSerialized(reader);
//                    break;
//                default:
//                    break;
//            }
//        }
//        return mChildren;
//    }
//}
