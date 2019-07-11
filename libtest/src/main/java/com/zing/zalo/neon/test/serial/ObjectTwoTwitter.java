package com.zing.zalo.neon.test.serial;

import android.support.annotation.NonNull;
import com.twitter.serial.serializer.ObjectSerializer;
import com.twitter.serial.serializer.SerializationContext;
import com.twitter.serial.stream.SerializerInput;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;

public class ObjectTwoTwitter {

    public int mSize;

    public int[] mElement;

    public static class NeonObjectTwoTwitterSerializer extends ObjectSerializer<ObjectTwoTwitter> {

        @Override
        protected void serializeObject(@NonNull SerializationContext context,
                @NonNull SerializerOutput output, @NonNull ObjectTwoTwitter object)
                throws IOException {
            //------------ NeonObjectTwo version------------//
            output.writeInt(2);
            //========================== Version 1 ==========================//
            //------------ mSize ------------//
            output.writeInt(object.mSize);
            //===============================================================//
            //========================== Version 2 ==========================//
            //------------ mElement ------------//
            if (object.mElement != null) {
                output.writeBoolean(true);
                output.writeInt(object.mElement.length);
                for (int i = 0; i < object.mElement.length; i++) {
                    output.writeInt(object.mElement[i]);
                }
            } else {
                output.writeBoolean(false);
            }
            //===============================================================//
        }

        @Override
        protected ObjectTwoTwitter deserializeObject(@NonNull SerializationContext context,
                @NonNull SerializerInput input, int versionNumber)
                throws IOException, ClassNotFoundException {
            ObjectTwoTwitter twoTwitter = new ObjectTwoTwitter();
            int version = input.readInt();
            if (version > 2) {
                throw new IllegalArgumentException(
                        "NeonObjectTwo is outdated. Update NeonObjectTwo to deserialize newest binary data.");
            }
            if (version >= 0) {
            }
            if (version >= 1) {
                twoTwitter.mSize = input.readInt();
            }
            if (version >= 2) {
                if (input.readBoolean()) {
                    int sizePrimitive = input.readInt();
                    twoTwitter.mElement = new int[sizePrimitive];
                    for (int i = 0; i < sizePrimitive; i++) {
                        twoTwitter.mElement[i] = input.readInt();
                    }
                }
            }
            return twoTwitter;
        }
    }
}
