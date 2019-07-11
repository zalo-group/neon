package com.zing.zalo.neon.test;

import com.twitter.serial.stream.Serial;
import com.twitter.serial.stream.bytebuffer.ByteBufferSerial;
import com.zing.zalo.neon.test.serial.ObjectOneTwitter;
import com.zing.zalo.neon.test.serial.ObjectTwitter;
import com.zing.zalo.neon.test.serial.ObjectTwoTwitter;
import java.io.IOException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class TwitterSerializerTest extends BaseTest {
    private final long NUM_LOOP = 2000;

    @Test
    public void twitterObject() throws IOException, ClassNotFoundException {
        ObjectTwitter.ObjectTwitterSerializer serializer =
                new ObjectTwitter.ObjectTwitterSerializer();
        ObjectTwitter obj = new ObjectTwitter();
        setObjectTwitterValue(obj);

        final Serial serial = new ByteBufferSerial();
        final byte[] serializedData = serial.toByteArray(obj, serializer);

        final ObjectTwitter newObj = serial.fromByteArray(serializedData, serializer);
        assertObject(obj, newObj);
    }

    @Test
    public void timing() throws IOException, ClassNotFoundException {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < NUM_LOOP; i++) {
            twitterObject();
        }
        long end = System.currentTimeMillis() - begin;
        System.out.println("Converted: " + NUM_LOOP);
        System.out.println("Total time: " + String.valueOf(end) + "ms");
        System.out.println("Average time: " + String.valueOf(end / (float) NUM_LOOP) + "ms.");
    }

    void setObjectOneTwitterValue(ObjectOneTwitter objectOne) {
        objectOne.xPosition = new float[10];
        objectOne.yPosition = new float[10];

        for (int i = 0; i < 10; i++) {
            objectOne.xPosition[i] = Math.abs(random.nextFloat());
            objectOne.yPosition[i] = Math.abs(random.nextFloat());
        }
    }

    void assertObjectOneTwitter(ObjectOneTwitter origin, ObjectOneTwitter result) {
        if (origin == null && result == null) {
            return;
        }
        assertArrayEquals(origin.xPosition, result.xPosition, FLOAT_DELTA);
        assertArrayEquals(origin.yPosition, result.yPosition, FLOAT_DELTA);
    }

    void setObjectTwoTwitterValue(ObjectTwoTwitter objectTwo) {
        objectTwo.mSize = 100;
        objectTwo.mElement = new int[objectTwo.mSize];
        for (int i = 0; i < objectTwo.mSize; i++) {
            objectTwo.mElement[i] = Math.abs(random.nextInt());
        }
    }

    void assertObjectTwoTwitter(ObjectTwoTwitter origin, ObjectTwoTwitter result) {
        if (origin == null && result == null) {
            return;
        }
        assertEquals(origin.mSize, result.mSize);
        assertArrayEquals(origin.mElement, result.mElement);
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

    void assertObject(ObjectTwitter origin, ObjectTwitter result) {
        if (origin == null && result == null) {
            return;
        }
        assertArrayEquals(origin.mSize, result.mSize);
        assertArrayEquals(origin.mPoint, result.mPoint, FLOAT_DELTA);
        assertArrayEquals(origin.mSeconds, result.mSeconds, DOUBLE_DELTA);
        assertArrayEquals(origin.mSizeWrong, result.mSizeWrong);
        assertObjectOneTwitter(origin.mFirstValue, result.mFirstValue);
        assertObjectTwoTwitter(origin.mSecondValue, result.mSecondValue);
        assertEquals(origin.status, result.status);

        assertEquals(origin.mArrayPet.length, result.mArrayPet.length);
        for (int i = 0; i < origin.mArrayPet.length; i++) {
            assertObjectOneTwitter(origin.mArrayPet[i], result.mArrayPet[i]);
        }
        assertEquals(origin.mArrayCats.length, result.mArrayCats.length);
        for (int i = 0; i < origin.mArrayCats.length; i++) {
            assertObjectTwoTwitter(origin.mArrayCats[i], result.mArrayCats[i]);
        }
    }
}
