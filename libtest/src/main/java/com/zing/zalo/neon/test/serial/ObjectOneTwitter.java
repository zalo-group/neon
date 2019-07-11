package com.zing.zalo.neon.test.serial;

import android.support.annotation.NonNull;
import com.twitter.serial.serializer.ObjectSerializer;
import com.twitter.serial.serializer.SerializationContext;
import com.twitter.serial.stream.SerializerInput;
import com.twitter.serial.stream.SerializerOutput;
import java.io.IOException;

public class ObjectOneTwitter {

    public float[] xPosition;
    public float[] yPosition;

    public static final class NeonObjectOneTwitterSerializer
            extends ObjectSerializer<ObjectOneTwitter> {

        @Override
        protected void serializeObject(@NonNull SerializationContext context,
                @NonNull SerializerOutput output, @NonNull ObjectOneTwitter object)
                throws IOException {
            //------------ NeonObjectOne version------------//
            output.writeInt(0);
            //========================== Version 0 ==========================//
            //------------ xPosition ------------//
            if (object.xPosition != null) {
                output.writeBoolean(true);
                output.writeInt(object.xPosition.length);
                for (int i = 0; i < object.xPosition.length; i++) {
                    output.writeDouble(object.xPosition[i]);
                }
            } else {
                output.writeBoolean(false);
            }
            //------------ yPosition ------------//
            if (object.yPosition != null) {
                output.writeBoolean(true);
                output.writeInt(object.yPosition.length);
                for (int i = 0; i < object.yPosition.length; i++) {
                    output.writeDouble(object.yPosition[i]);
                }
            } else {
                output.writeBoolean(false);
            }
            //===============================================================//
        }

        @Override
        protected ObjectOneTwitter deserializeObject(@NonNull SerializationContext context,
                @NonNull SerializerInput input, int versionNumber)
                throws IOException, ClassNotFoundException {
            ObjectOneTwitter oneTwitter = new ObjectOneTwitter();
            int version = input.readInt();
            if (version > 0) {
                throw new IllegalArgumentException(
                        "NeonObjectOne is outdated. Update NeonObjectOne to deserialize newest binary data.");
            }
            if (version >= 0) {
                if (input.readBoolean()) {
                    int sizePrimitive = input.readInt();
                    oneTwitter.xPosition = new float[sizePrimitive];
                    for (int i = 0; i < sizePrimitive; i++) {
                        oneTwitter.xPosition[i] = (float) input.readDouble();
                    }
                }
                if (input.readBoolean()) {
                    int sizePrimitive = input.readInt();
                    oneTwitter.yPosition = new float[sizePrimitive];
                    for (int i = 0; i < sizePrimitive; i++) {
                        oneTwitter.yPosition[i] = (float) input.readDouble();
                    }
                }
            }
            return oneTwitter;
        }
    }
}
