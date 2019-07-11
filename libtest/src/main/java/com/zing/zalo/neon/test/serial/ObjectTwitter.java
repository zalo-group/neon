package com.zing.zalo.neon.test.serial;

import android.support.annotation.NonNull;
import com.twitter.serial.serializer.ObjectSerializer;
import com.twitter.serial.serializer.SerializationContext;
import com.twitter.serial.stream.SerializerInput;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;

public class ObjectTwitter {

    // Object

    public ObjectOneTwitter mFirstValue;
    public ObjectTwoTwitter mSecondValue;
    public String status;

    // Array

    public ObjectOneTwitter[] mArrayPet;
    public ObjectTwoTwitter[] mArrayCats;

    // Primitive Array

    public int[] mSize;
    public float[] mPoint;
    public double[] mSeconds;
    public boolean[] mSizeWrong;

    public static class ObjectTwitterSerializer extends ObjectSerializer<ObjectTwitter> {

        @Override
        protected void serializeObject(@NonNull SerializationContext context,
                @NonNull SerializerOutput output, @NonNull ObjectTwitter object)
                throws IOException {
            ObjectOneTwitter.NeonObjectOneTwitterSerializer serializerObjectOne =
                    new ObjectOneTwitter.NeonObjectOneTwitterSerializer();
            ObjectTwoTwitter.NeonObjectTwoTwitterSerializer serializerObjectTwo =
                    new ObjectTwoTwitter.NeonObjectTwoTwitterSerializer();
            //------------ NeonObject version------------//
            output.writeInt(0);
            //========================== Version 0 ==========================//
            //------------ mFirstValue ------------//
            if (object.mFirstValue != null) {
                output.writeBoolean(true);
                output.writeObject(context, object.mFirstValue, serializerObjectOne);
            } else {
                output.writeBoolean(false);
            }
            //------------ mSecondValue ------------//
            if (object.mSecondValue != null) {
                output.writeBoolean(true);
                output.writeObject(context, object.mSecondValue, serializerObjectTwo);
            } else {
                output.writeBoolean(false);
            }
            //------------ status ------------//
            output.writeString(object.status);
            //------------ mArrayPet ------------//
            if (object.mArrayPet != null) {
                output.writeBoolean(true);
                output.writeInt(object.mArrayPet.length);
                for (ObjectOneTwitter i : object.mArrayPet) {
                    output.writeObject(context, i, serializerObjectOne);
                }
            } else {
                output.writeBoolean(false);
            }
            //------------ mArrayCats ------------//
            if (object.mArrayCats != null) {
                output.writeBoolean(true);
                output.writeInt(object.mArrayCats.length);
                for (ObjectTwoTwitter i : object.mArrayCats) {
                    output.writeObject(context, i, serializerObjectTwo);
                }
            } else {
                output.writeBoolean(false);
            }
            //------------ mSize ------------//
            if (object.mSize != null) {
                output.writeBoolean(true);
                output.writeInt(object.mSize.length);
                for (int i = 0; i < object.mSize.length; i++) {
                    output.writeInt(object.mSize[i]);
                }
            } else {
                output.writeBoolean(false);
            }
            //------------ mPoint ------------//
            if (object.mPoint != null) {
                output.writeBoolean(true);
                output.writeInt(object.mPoint.length);
                for (int i = 0; i < object.mPoint.length; i++) {
                    output.writeDouble(object.mPoint[i]);
                }
            } else {
                output.writeBoolean(false);
            }
            //------------ mSeconds ------------//
            if (object.mSeconds != null) {
                output.writeBoolean(true);
                output.writeInt(object.mSeconds.length);
                for (int i = 0; i < object.mSeconds.length; i++) {
                    output.writeDouble(object.mSeconds[i]);
                }
            } else {
                output.writeBoolean(false);
            }
            //------------ mSizeWrong ------------//
            if (object.mSizeWrong != null) {
                output.writeBoolean(true);
                output.writeInt(object.mSizeWrong.length);
                for (int i = 0; i < object.mSizeWrong.length; i++) {
                    output.writeBoolean(object.mSizeWrong[i]);
                }
            } else {
                output.writeBoolean(false);
            }
            //===============================================================//
        }

        @Override
        protected ObjectTwitter deserializeObject(@NonNull SerializationContext context,
                @NonNull SerializerInput input, int versionNumber)
                throws IOException, ClassNotFoundException {
            ObjectOneTwitter.NeonObjectOneTwitterSerializer serializerObjectOne =
                    new ObjectOneTwitter.NeonObjectOneTwitterSerializer();
            ObjectTwoTwitter.NeonObjectTwoTwitterSerializer serializerObjectTwo =
                    new ObjectTwoTwitter.NeonObjectTwoTwitterSerializer();
            ObjectTwitter object = new ObjectTwitter();

            int version = input.readInt();
            if (version > 0) {
                throw new IllegalArgumentException(
                        "NeonObject is outdated. Update NeonObject to deserialize newest binary data.");
            }
            if (version >= 0) {
                if (input.readBoolean()) {
                    object.mFirstValue = input.readObject(context, serializerObjectOne);
                }
                if (input.readBoolean()) {
                    object.mSecondValue = input.readObject(context, serializerObjectTwo);
                }
                object.status = input.readString();
                if (input.readBoolean()) {
                    int sizemArrayPet = input.readInt();
                    object.mArrayPet = new ObjectOneTwitter[sizemArrayPet];
                    for (int i = 0; i < sizemArrayPet; i++) {
                        object.mArrayPet[i] = input.readObject(context, serializerObjectOne);
                    }
                }
                if (input.readBoolean()) {
                    int sizemArrayCats = input.readInt();
                    object.mArrayCats = new ObjectTwoTwitter[sizemArrayCats];
                    for (int i = 0; i < sizemArrayCats; i++) {
                        object.mArrayCats[i] = input.readObject(context, serializerObjectTwo);
                    }
                }
                if (input.readBoolean()) {
                    int sizePrimitive = input.readInt();
                    object.mSize = new int[sizePrimitive];
                    for (int i = 0; i < sizePrimitive; i++) {
                        object.mSize[i] = input.readInt();
                    }
                }
                if (input.readBoolean()) {
                    int sizePrimitive = input.readInt();
                    object.mPoint = new float[sizePrimitive];
                    for (int i = 0; i < sizePrimitive; i++) {
                        object.mPoint[i] = (float) input.readDouble();
                    }
                }
                if (input.readBoolean()) {
                    int sizePrimitive = input.readInt();
                    object.mSeconds = new double[sizePrimitive];
                    for (int i = 0; i < sizePrimitive; i++) {
                        object.mSeconds[i] = input.readDouble();
                    }
                }
                if (input.readBoolean()) {
                    int sizePrimitive = input.readInt();
                    object.mSizeWrong = new boolean[sizePrimitive];
                    for (int i = 0; i < sizePrimitive; i++) {
                        object.mSizeWrong[i] = input.readBoolean();
                    }
                }
            }
            return object;
        }
    }
}

