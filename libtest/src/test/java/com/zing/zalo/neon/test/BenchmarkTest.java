package com.zing.zalo.neon.test;

import com.twitter.serial.stream.Serial;
import com.twitter.serial.stream.bytebuffer.ByteBufferSerial;
import com.zing.neon.data.serialization.SerializedByteBufferInput;
import com.zing.neon.data.serialization.SerializedByteBufferOutput;
import com.zing.zalo.neon.helper.SerializableHelper;
import com.zing.zalo.neon.test.javaobject.JavaObject;
import com.zing.zalo.neon.test.javaobject.JavaOne;
import com.zing.zalo.neon.test.javaobject.JavaTwo;
import com.zing.zalo.neon.test.object.NeonObject;
import com.zing.zalo.neon.test.serial.ObjectOneTwitter;
import com.zing.zalo.neon.test.serial.ObjectTwitter;
import com.zing.zalo.neon.test.serial.ObjectTwoTwitter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.Test;

public class BenchmarkTest extends BaseTest {
    @Test
    public void javaSerializer() throws Exception {
        JavaObject obj = new JavaObject();
        setJavaObjectValue(obj);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeObject(obj);
        out.close();

        ObjectInputStream inputStream =
                new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        JavaObject newObj = (JavaObject) inputStream.readObject();
    }

    @Test
    public void twitterSerial() throws IOException, ClassNotFoundException {
        ObjectTwitter.ObjectTwitterSerializer serializer =
                new ObjectTwitter.ObjectTwitterSerializer();
        ObjectTwitter obj = new ObjectTwitter();
        setObjectTwitterValue(obj);

        final Serial serial = new ByteBufferSerial();
        final byte[] serializedData = serial.toByteArray(obj, serializer);

        final ObjectTwitter newObj = serial.fromByteArray(serializedData, serializer);
    }

    @Test
    public void neon() throws Exception {
        NeonObject origin = new NeonObject();
        setNeonObjectValue(origin);
        SerializedByteBufferOutput writer = new SerializedByteBufferOutput();
        origin.serialize(writer);
        SerializedByteBufferInput input = new SerializedByteBufferInput(writer.toByteArray());
        NeonObject result = SerializableHelper.deserialize(input, NeonObject.CREATOR, null);
    }

    @Test
    public void benchmark() throws Exception {
        long totalTimeJavaSerializer = 0;
        long totalTimeTwitterSerial = 0;
        long totalTimeNeon = 0;
        final int LAPS = 5000;
        for (int i = 0; i < LAPS / 5; i++) {
            // warm up
            javaSerializer();
            twitterSerial();
            neon();
        }

        for (int i = 0; i < LAPS; i++) {
            long time = System.currentTimeMillis();
            javaSerializer();
            totalTimeJavaSerializer += System.currentTimeMillis() - time;

            time = System.currentTimeMillis();
            twitterSerial();
            totalTimeTwitterSerial += System.currentTimeMillis() - time;

            time = System.currentTimeMillis();
            neon();
            totalTimeNeon += System.currentTimeMillis() - time;
        }

        System.out.println("Java Serializer: \t\t" + (totalTimeJavaSerializer * 1.0 / LAPS) + "ms");
        System.out.println("Twitter Serial: \t\t" + (totalTimeTwitterSerial * 1.0 / LAPS) + "ms");
        System.out.println("Neon: \t\t" + (totalTimeNeon * 1.0 / LAPS) + "ms");
    }

    void setJavaOneValue(JavaOne objectOne) {
        objectOne.xPosition = new float[10];
        objectOne.yPosition = new float[10];

        for (int i = 0; i < 10; i++) {
            objectOne.xPosition[i] = Math.abs(random.nextFloat());
            objectOne.yPosition[i] = Math.abs(random.nextFloat());
        }
    }

    void setJavaTwoValue(JavaTwo objectTwo) {
        objectTwo.mSize = 100;
        objectTwo.mElement = new int[objectTwo.mSize];
        for (int i = 0; i < objectTwo.mSize; i++) {
            objectTwo.mElement[i] = Math.abs(random.nextInt());
        }
    }

    void setJavaObjectValue(JavaObject object) {

        object.mFirstValue = new JavaOne();
        object.mSecondValue = new JavaTwo();

        setJavaOneValue(object.mFirstValue);
        setJavaTwoValue(object.mSecondValue);
        object.status = "incoming";

        object.mArrayPet = new JavaOne[100];
        for (int i = 0; i < object.mArrayPet.length; i++) {
            object.mArrayPet[i] = new JavaOne();
            setJavaOneValue(object.mArrayPet[i]);
        }

        object.mArrayCats = new JavaTwo[100];
        for (int i = 0; i < object.mArrayCats.length; i++) {
            object.mArrayCats[i] = new JavaTwo();
            setJavaTwoValue(object.mArrayCats[i]);
        }

        object.mSize = new int[100];
        for (int i = 0; i < object.mSize.length; i++) {
            object.mSize[i] = Math.abs(random.nextInt());
        }

        object.mPoint = new float[100];
        for (int i = 0; i < object.mPoint.length; i++) {
            object.mPoint[i] = Math.abs(random.nextFloat());
        }

        object.mSeconds = new double[100];
        for (int i = 0; i < object.mSeconds.length; i++) {
            object.mSeconds[i] = Math.abs(random.nextDouble());
        }

        object.mSizeWrong = new boolean[100];
        for (int i = 0; i < object.mSizeWrong.length; i++) {
            object.mSizeWrong[i] = random.nextBoolean();
        }
    }

    void setObjectOneTwitterValue(ObjectOneTwitter objectOne) {
        objectOne.xPosition = new float[10];
        objectOne.yPosition = new float[10];

        for (int i = 0; i < 10; i++) {
            objectOne.xPosition[i] = Math.abs(random.nextFloat());
            objectOne.yPosition[i] = Math.abs(random.nextFloat());
        }
    }

    void setObjectTwoTwitterValue(ObjectTwoTwitter objectTwo) {
        objectTwo.mSize = 100;
        objectTwo.mElement = new int[objectTwo.mSize];
        for (int i = 0; i < objectTwo.mSize; i++) {
            objectTwo.mElement[i] = Math.abs(random.nextInt());
        }
    }

    void setObjectTwitterValue(ObjectTwitter object) {

        object.mFirstValue = new ObjectOneTwitter();
        object.mSecondValue = new ObjectTwoTwitter();

        setObjectOneTwitterValue(object.mFirstValue);
        setObjectTwoTwitterValue(object.mSecondValue);
        object.status = "incoming";

        object.mArrayPet = new ObjectOneTwitter[100];
        for (int i = 0; i < object.mArrayPet.length; i++) {
            object.mArrayPet[i] = new ObjectOneTwitter();
            setObjectOneTwitterValue(object.mArrayPet[i]);
        }

        object.mArrayCats = new ObjectTwoTwitter[100];
        for (int i = 0; i < object.mArrayCats.length; i++) {
            object.mArrayCats[i] = new ObjectTwoTwitter();
            setObjectTwoTwitterValue(object.mArrayCats[i]);
        }

        object.mSize = new int[100];
        for (int i = 0; i < object.mSize.length; i++) {
            object.mSize[i] = Math.abs(random.nextInt());
        }

        object.mPoint = new float[100];
        for (int i = 0; i < object.mPoint.length; i++) {
            object.mPoint[i] = Math.abs(random.nextFloat());
        }

        object.mSeconds = new double[100];
        for (int i = 0; i < object.mSeconds.length; i++) {
            object.mSeconds[i] = Math.abs(random.nextDouble());
        }

        object.mSizeWrong = new boolean[100];
        for (int i = 0; i < object.mSizeWrong.length; i++) {
            object.mSizeWrong[i] = random.nextBoolean();
        }
    }
}
