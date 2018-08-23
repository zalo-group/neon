package com.example.libtest;

import com.zalo.zing.javaobject.JavaObject;
import com.zalo.zing.javaobject.JavaOne;
import com.zalo.zing.javaobject.JavaTwo;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


public class JavaSerializerTest extends BaseTest {

    private final long NUM_LOOP = 2000;

    @Test
    public void javaObject() throws IOException, ClassNotFoundException {
        JavaObject obj = new JavaObject();
        setJavaObjectValue(obj);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeObject(obj);
        out.close();

        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        JavaObject newObj;
        newObj = (JavaObject) inputStream.readObject();

        assertObject(obj, newObj);
    }

    @Test
    @Ignore
    public void timing() throws IOException, ClassNotFoundException {
        long begin = System.currentTimeMillis();
        for (int i = 0; i < NUM_LOOP; i++) {
            javaObject();
        }
        long end = System.currentTimeMillis() - begin;
        System.out.println("Converted: " + NUM_LOOP);
        System.out.println("Total time: " + String.valueOf(end) + "ms");
        System.out.println("Average time: " + String.valueOf(end / (float) NUM_LOOP) + "ms.");
    }

    void setJavaOneValue(JavaOne objectOne) {
        objectOne.xPosition = new float[10];
        objectOne.yPosition = new float[10];

        for (int i = 0; i < 10; i++) {
            objectOne.xPosition[i] = Math.abs(random.nextFloat());
            objectOne.yPosition[i] = Math.abs(random.nextFloat());
        }
    }

    void assertJavaOne(JavaOne origin, JavaOne result) {
        if (origin == null && result == null)
            return;
        Assert.assertArrayEquals(origin.xPosition, result.xPosition, BaseTest.FLOAT_DELTA);
        Assert.assertArrayEquals(origin.yPosition, result.yPosition, BaseTest.FLOAT_DELTA);
    }

    void setJavaTwoValue(JavaTwo objectTwo) {
        objectTwo.mSize = 100;
        objectTwo.mElement = new int[objectTwo.mSize];
        for (int i = 0; i < objectTwo.mSize; i++) {
            objectTwo.mElement[i] = Math.abs(random.nextInt());
        }
    }

    void assertJavaTwo(JavaTwo origin, JavaTwo result) {
        if (origin == null && result == null)
            return;
        assertEquals(origin.mSize, result.mSize);
        assertArrayEquals(origin.mElement, result.mElement);
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

    void assertObject(JavaObject origin, JavaObject result) {
        if (origin == null && result == null)
            return;
        assertArrayEquals(origin.mSize, result.mSize);
        Assert.assertArrayEquals(origin.mPoint, result.mPoint, BaseTest.FLOAT_DELTA);
        Assert.assertArrayEquals(origin.mSeconds, result.mSeconds, BaseTest.DOUBLE_DELTA);
        assertArrayEquals(origin.mSizeWrong, result.mSizeWrong);
        assertJavaOne(origin.mFirstValue, result.mFirstValue);
        assertJavaTwo(origin.mSecondValue, result.mSecondValue);
        assertEquals(origin.status, result.status);

        assertEquals(origin.mArrayPet.length, result.mArrayPet.length);
        for (int i = 0; i < origin.mArrayPet.length; i++) {
            assertJavaOne(origin.mArrayPet[i], result.mArrayPet[i]);
        }
        assertEquals(origin.mArrayCats.length, result.mArrayCats.length);
        for (int i = 0; i < origin.mArrayCats.length; i++) {
            assertJavaTwo(origin.mArrayCats[i], result.mArrayCats[i]);
        }
    }
}