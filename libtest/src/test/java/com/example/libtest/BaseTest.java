package com.example.libtest;

import com.zalo.zing.abstractAdapter.ZarcelBike;
import com.zalo.zing.abstractAdapter.ZarcelCar;
import com.zalo.zing.abstractAdapter.ZarcelVehicle;
import com.zalo.zing.customadapter.*;
import com.zalo.zing.extendClass.ZarcelChild;
import com.zalo.zing.extendClass.ZarcelRoot;
import com.zalo.zing.nullable.ZarcelNullable;
import com.zalo.zing.object.ZarcelObject;
import com.zalo.zing.object.ZarcelObjectOne;
import com.zalo.zing.object.ZarcelObjectTwo;
import com.zalo.zing.primitive.ZarcelPrimitive;
import com.zalo.zing.version.ZarcelBaseVersion;
import com.zalo.zing.version.ZarcelNewVersion;

import java.util.Random;

import static org.junit.Assert.*;

public class BaseTest {

    private static final double DOUBLE_DELTA = 1e-16;
    private static final float FLOAT_DELTA = 1e-5f;


    private Random random = new Random();

    private int getInt() {
        return Math.abs(random.nextInt());
    }

    void assertZarcelPrimitive(ZarcelPrimitive origin, ZarcelPrimitive result) {
        if (origin == null && result == null)
            return;
        assertEquals(origin.mCats, result.mCats);
        assertEquals(origin.mDogs, result.mDogs);
        assertEquals(origin.mRadius, result.mRadius, FLOAT_DELTA);
        assertEquals(origin.mTimer, result.mTimer, DOUBLE_DELTA);
        assertEquals(origin.mName, result.mName);
    }

    void setZarcelPrimitiveProperty(ZarcelPrimitive zarcelPrimitive) {
        zarcelPrimitive.mCats = 58751;
        zarcelPrimitive.mDogs = 872102;
        zarcelPrimitive.mRadius = 48.41282f;
        zarcelPrimitive.mTimer = 223.221582;
        zarcelPrimitive.mName = "KittyDogCatMeoMeo";
    }

    void assertZarcelVersion(ZarcelNewVersion origin, ZarcelNewVersion result) {
        if ((origin == null && result != null) || (origin != null && result == null))
            fail("ZarcelVersion object is not equal");
        assertEquals(origin.mCats, result.mCats);
        assertEquals(origin.mPetName, result.mPetName);
        assertEquals(origin.mRadius, result.mRadius, FLOAT_DELTA);
        assertEquals(origin.mVersionName, result.mVersionName);
    }

    void setZarcelVersionProperty(ZarcelBaseVersion baseVersion) {
        baseVersion.mCats = 82753;
        baseVersion.mRadius = 22.135274f;
        baseVersion.versionName = "Zarcel Base Testing";
    }

    void assertZarcelNullable(ZarcelNullable origin, ZarcelNullable result) {
        if (origin == null && result == null)
            return;
        assertNotNull(origin.mPet);
        assertNotNull(result.mPet);
        assertZarcelPrimitive(origin.mPet, result.mPet);
        assertZarcelPrimitive(origin.mOptionalPet, result.mOptionalPet);
    }

    void setZarcelNullable(ZarcelNullable nullable, boolean petNull) {
        if (!petNull) {
            nullable.mPet = new ZarcelPrimitive();
            setZarcelPrimitiveProperty(nullable.mPet);
        }
    }

    void setZarcelObjectOneValue(ZarcelObjectOne objectOne) {
        objectOne.xPosition = new float[10];
        objectOne.yPosition = new float[10];

        for (int i = 0; i < 10; i++) {
            objectOne.xPosition[i] = Math.abs(random.nextFloat());
            objectOne.yPosition[i] = Math.abs(random.nextFloat());
        }
    }

    void assertZarcelObjectOne(ZarcelObjectOne origin, ZarcelObjectOne result) {
        if (origin == null && result == null)
            return;
        assertArrayEquals(origin.xPosition, result.xPosition, FLOAT_DELTA);
        assertArrayEquals(origin.yPosition, result.yPosition, FLOAT_DELTA);
    }

    void setZarcelObjectTwoValue(ZarcelObjectTwo objectTwo) {
        objectTwo.mSize = getInt() % 100 + 10;
        objectTwo.mElement = new int[objectTwo.mSize];
        for (int i = 0; i < objectTwo.mSize; i++) {
            objectTwo.mElement[i] = Math.abs(random.nextInt());
        }
    }

    void assertZarcelObjectTwo(ZarcelObjectTwo origin, ZarcelObjectTwo result) {
        if (origin == null && result == null)
            return;
        assertEquals(origin.mSize, result.mSize);
        assertArrayEquals(origin.mElement, result.mElement);
    }

    void setZarcelObjectValue(ZarcelObject object) {

        object.mFirstValue = new ZarcelObjectOne();
        object.mSecondValue = new ZarcelObjectTwo();

        setZarcelObjectOneValue(object.mFirstValue);
        setZarcelObjectTwoValue(object.mSecondValue);
        object.status = "incoming";

        object.mArrayPet = new ZarcelObjectOne[getInt() % 100 + 10];
        for (int i = 0; i < object.mArrayPet.length; i++) {
            object.mArrayPet[i] = new ZarcelObjectOne();
            setZarcelObjectOneValue(object.mArrayPet[i]);
        }

        object.mArrayCats = new ZarcelObjectTwo[getInt() % 100 + 10];
        for (int i = 0; i < object.mArrayCats.length; i++) {
            object.mArrayCats[i] = new ZarcelObjectTwo();
            setZarcelObjectTwoValue(object.mArrayCats[i]);
        }

        object.mSize = new int[getInt() % 100 + 10];
        for (int i = 0; i < object.mSize.length; i++) {
            object.mSize[i] = Math.abs(random.nextInt());
        }

        object.mPoint = new float[getInt() % 100 + 10];
        for (int i = 0; i < object.mPoint.length; i++) {
            object.mPoint[i] = Math.abs(random.nextFloat());
        }

        object.mSeconds = new double[getInt() % 100 + 10];
        for (int i = 0; i < object.mSeconds.length; i++) {
            object.mSeconds[i] = Math.abs(random.nextDouble());
        }

        object.mSizeWrong = new boolean[getInt() % 100 + 10];
        for (int i = 0; i < object.mSizeWrong.length; i++) {
            object.mSizeWrong[i] = random.nextBoolean();
        }
    }

    void assertZarcelObject(ZarcelObject origin, ZarcelObject result) {
        if (origin == null && result == null)
            return;
        assertArrayEquals(origin.mSize, result.mSize);
        assertArrayEquals(origin.mPoint, result.mPoint, FLOAT_DELTA);
        assertArrayEquals(origin.mSeconds, result.mSeconds, DOUBLE_DELTA);
        assertArrayEquals(origin.mSizeWrong, result.mSizeWrong);
        assertZarcelObjectOne(origin.mFirstValue, result.mFirstValue);
        assertZarcelObjectTwo(origin.mSecondValue, result.mSecondValue);
        assertEquals(origin.status, result.status);

        assertEquals(origin.mArrayPet.length, result.mArrayPet.length);
        for (int i = 0; i < origin.mArrayPet.length; i++) {
            assertZarcelObjectOne(origin.mArrayPet[i], result.mArrayPet[i]);
        }
        assertEquals(origin.mArrayCats.length, result.mArrayCats.length);
        for (int i = 0; i < origin.mArrayCats.length; i++) {
            assertZarcelObjectTwo(origin.mArrayCats[i], result.mArrayCats[i]);
        }
    }

    void setZarcelPig(ZarcelPig pig) {
        pig.mType = ZarcelAnimal.PIG;
        pig.weight = new ZarcelObjectOne();
        setZarcelObjectOneValue(pig.weight);
        pig.height = new ZarcelObjectTwo[getInt() % 100 + 10];
        for (int i = 0; i < pig.height.length; i++) {
            pig.height[i] = new ZarcelObjectTwo();
            setZarcelObjectTwoValue(pig.height[i]);
        }
    }

    void assertZarcelPig(ZarcelPig origin, ZarcelPig result) {
        assertZarcelObjectOne(origin.weight, result.weight);
        assertEquals(origin.height.length, result.height.length);
        for (int i = 0; i < origin.height.length; i++) {
            assertZarcelObjectTwo(origin.height[i], result.height[i]);
        }
    }

    void setZarcelDog(ZarcelDog dog) {
        dog.mType = ZarcelAnimal.DOG;
        dog.foods = "dogFood";
        dog.eatAny = new ZarcelPrimitive();
        setZarcelPrimitiveProperty(dog.eatAny);
    }

    void assertZarcelDog(ZarcelDog origin, ZarcelDog result) {
        assertZarcelPrimitive(origin.eatAny, result.eatAny);
        assertEquals(origin.foods, result.foods);
    }

    void setZarcelCat(ZarcelCat cat) {
        cat.mType = ZarcelAnimal.CAT;
        cat.foods = new int[getInt() % 100 + 10];
        for (int i = 0; i < cat.foods.length; i++) {
            cat.foods[i] = getInt();
        }
        cat.playable = new ZarcelObjectOne[getInt() % 100 + 10];
        for (int i = 0; i < cat.playable.length; i++) {
            cat.playable[i] = new ZarcelObjectOne();
            setZarcelObjectOneValue(cat.playable[i]);
        }
    }

    void assertZarcelCat(ZarcelCat origin, ZarcelCat result) {
        assertArrayEquals(origin.foods, result.foods);
        assertEquals(origin.playable.length, result.playable.length);
        for (int i = 0; i < origin.playable.length; i++) {
            assertZarcelObjectOne(origin.playable[i], result.playable[i]);
        }
    }

    void setZarcelCustomAnimal(ZarcelCustomAnimal base) {

        base.animals = new ZarcelAnimal[getInt() % 100 + 10];
        for (int i = 0; i < base.animals.length; i++) {
            int rand = Math.abs(random.nextInt()) % 3;
            switch (rand) {
                case 0:
                    base.animals[i] = new ZarcelCat();
                    setZarcelCat((ZarcelCat) base.animals[i]);
                    break;
                case 1:
                    base.animals[i] = new ZarcelPig();
                    setZarcelPig((ZarcelPig) base.animals[i]);
                    break;
                case 2:
                    base.animals[i] = new ZarcelDog();
                    setZarcelDog((ZarcelDog) base.animals[i]);
                    break;
            }
        }
    }

    void assertZarcelCustomAnimal(ZarcelCustomAnimal origin, ZarcelCustomAnimal result) {

        assertEquals(origin.animals.length, result.animals.length);
        for (int i = 0; i < origin.animals.length; i++) {
            if (origin.animals[i] instanceof ZarcelCat) {
                assertZarcelCat((ZarcelCat) origin.animals[i], (ZarcelCat) result.animals[i]);
            } else if (origin.animals[i] instanceof ZarcelDog) {
                assertZarcelDog((ZarcelDog) origin.animals[i], (ZarcelDog) result.animals[i]);
            } else if (origin.animals[i] instanceof ZarcelPig) {
                assertZarcelPig((ZarcelPig) origin.animals[i], (ZarcelPig) result.animals[i]);
            }
        }
    }

    void setZarcelRoot(ZarcelRoot root) {
        root.x = getInt();
        root.y = getInt();
        root.z = getInt();

        root.animals = new ZarcelAnimal[getInt() % 100 + 10];
        for (int i = 0; i < root.animals.length; i++) {
            int rand = Math.abs(random.nextInt()) % 3;
            switch (rand) {
                case 0:
                    root.animals[i] = new ZarcelCat();
                    setZarcelCat((ZarcelCat) root.animals[i]);
                    break;
                case 1:
                    root.animals[i] = new ZarcelPig();
                    setZarcelPig((ZarcelPig) root.animals[i]);
                    break;
                case 2:
                    root.animals[i] = new ZarcelDog();
                    setZarcelDog((ZarcelDog) root.animals[i]);
                    break;
            }
        }
    }

    void assertZarcelRoot(ZarcelRoot origin, ZarcelRoot result) {
        assertEquals(origin.x, result.x);
        assertEquals(origin.y, result.y);
        assertEquals(origin.z, result.z);

        assertEquals(origin.animals.length, result.animals.length);
        for (int i = 0; i < origin.animals.length; i++) {
            if (origin.animals[i] instanceof ZarcelCat) {
                assertZarcelCat((ZarcelCat) origin.animals[i], (ZarcelCat) result.animals[i]);
            } else if (origin.animals[i] instanceof ZarcelDog) {
                assertZarcelDog((ZarcelDog) origin.animals[i], (ZarcelDog) result.animals[i]);
            } else if (origin.animals[i] instanceof ZarcelPig) {
                assertZarcelPig((ZarcelPig) origin.animals[i], (ZarcelPig) result.animals[i]);
            }
        }

    }

    void setZarcelChild(ZarcelChild child) {
        setZarcelRoot(child);
        child.daddyName = "Khung Long Bao Chua";
    }

    void assertZarcelChild(ZarcelChild origin, ZarcelChild result) {
        assertZarcelRoot(origin, result);
        assertEquals(origin.daddyName, result.daddyName);
    }

    void assertZarcelBike(ZarcelBike origin, ZarcelBike result) {
        assertEquals(origin.maxSpeed, result.maxSpeed);
    }

    void assertZarcelCar(ZarcelCar origin, ZarcelCar result) {
        assertEquals(origin.numberOfSeat, result.numberOfSeat);
        assertEquals(origin.maxSpeed, result.maxSpeed);
    }

    void assertZarcelVehicle(ZarcelVehicle origin, ZarcelVehicle result) {
        if (origin instanceof ZarcelBike && result instanceof ZarcelBike) {
            assertZarcelBike((ZarcelBike) origin, (ZarcelBike) result);
        } else if (origin instanceof ZarcelCar && result instanceof ZarcelCar) {
            assertZarcelCar((ZarcelCar) origin, (ZarcelCar) result);
        } else {
            fail();
        }
    }
}
