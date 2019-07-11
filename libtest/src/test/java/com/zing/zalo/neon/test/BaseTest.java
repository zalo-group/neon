package com.zing.zalo.neon.test;

import com.zing.zalo.neon.test.abstractAdapter.NeonBike;
import com.zing.zalo.neon.test.abstractAdapter.NeonCar;
import com.zing.zalo.neon.test.abstractAdapter.NeonVehicle;
import com.zing.zalo.neon.test.customadapter.NeonAnimal;
import com.zing.zalo.neon.test.customadapter.NeonCat;
import com.zing.zalo.neon.test.customadapter.NeonCustomAnimal;
import com.zing.zalo.neon.test.customadapter.NeonDog;
import com.zing.zalo.neon.test.customadapter.NeonPig;
import com.zing.zalo.neon.test.extendClass.NeonChild;
import com.zing.zalo.neon.test.extendClass.NeonParent;
import com.zing.zalo.neon.test.nullable.NeonNullable;
import com.zing.zalo.neon.test.object.NeonObject;
import com.zing.zalo.neon.test.object.NeonObjectOne;
import com.zing.zalo.neon.test.object.NeonObjectTwo;
import com.zing.zalo.neon.test.primitive.NeonPrimitive;
import com.zing.zalo.neon.test.version.NeonBaseVersion;
import com.zing.zalo.neon.test.version.NeonNewVersion;
import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class BaseTest {

    static final double DOUBLE_DELTA = 1e-16;
    static final float FLOAT_DELTA = 1e-5f;

    Random random = new Random();

    int getInt() {
        return Math.abs(random.nextInt());
    }

    void assertNeonPrimitive(NeonPrimitive origin, NeonPrimitive result) {
        if (origin == null && result == null) {
            return;
        }
        assertEquals(origin.mCats, result.mCats);
        assertEquals(origin.mDogs, result.mDogs);
        assertEquals(origin.mRadius, result.mRadius, FLOAT_DELTA);
        assertEquals(origin.mTimer, result.mTimer, DOUBLE_DELTA);
        assertEquals(origin.mName, result.mName);
    }

    void setNeonPrimitiveProperty(NeonPrimitive primitive) {
        primitive.mCats = 58751;
        primitive.mDogs = 872102;
        primitive.mRadius = 48.41282f;
        primitive.mTimer = 223.221582;
        primitive.mName = "KittyDogCatMeoMeo";
    }

    void assertNeonVersion(NeonNewVersion origin, NeonNewVersion result) {
        if ((origin == null && result != null) || (origin != null && result == null)) {
            fail("NeonVersion object is not equal");
        }
        assertEquals(origin.mCats, result.mCats);
        assertEquals(origin.mPetName, result.mPetName);
        assertEquals(origin.mRadius, result.mRadius, FLOAT_DELTA);
        assertEquals(origin.mVersionName, result.mVersionName);
    }

    void setNeonVersionProperty(NeonBaseVersion baseVersion) {
        baseVersion.mCats = 82753;
        baseVersion.mRadius = 22.135274f;
        baseVersion.versionName = "Neon Base Testing";
    }

    void assertNeonNullable(NeonNullable origin, NeonNullable result) {
        if (origin == null && result == null) {
            return;
        }
        assertNotNull(origin.mPet);
        assertNotNull(result.mPet);
        assertNeonPrimitive(origin.mPet, result.mPet);
        assertNeonPrimitive(origin.mOptionalPet, result.mOptionalPet);
    }

    void setNeonNullable(NeonNullable nullable, boolean petNull) {
        if (!petNull) {
            nullable.mPet = new NeonPrimitive();
            setNeonPrimitiveProperty(nullable.mPet);
        }
    }

    void setNeonObjectOneValue(NeonObjectOne objectOne) {
        objectOne.xPosition = new float[10];
        objectOne.yPosition = new float[10];

        for (int i = 0; i < 10; i++) {
            objectOne.xPosition[i] = Math.abs(random.nextFloat());
            objectOne.yPosition[i] = Math.abs(random.nextFloat());
        }
    }

    void assertNeonObjectOne(NeonObjectOne origin, NeonObjectOne result) {
        if (origin == null && result == null) {
            return;
        }
        assertArrayEquals(origin.xPosition, result.xPosition, FLOAT_DELTA);
        assertArrayEquals(origin.yPosition, result.yPosition, FLOAT_DELTA);
    }

    void setNeonObjectTwoValue(NeonObjectTwo objectTwo) {
        objectTwo.mSize = 100;
        objectTwo.mElement = new int[objectTwo.mSize];
        for (int i = 0; i < objectTwo.mSize; i++) {
            objectTwo.mElement[i] = Math.abs(random.nextInt());
        }
    }

    void assertNeonObjectTwo(NeonObjectTwo origin, NeonObjectTwo result) {
        if (origin == null && result == null) {
            return;
        }
        assertEquals(origin.mSize, result.mSize);
        assertArrayEquals(origin.mElement, result.mElement);
    }

    void setNeonObjectValue(NeonObject object) {
        object.mFirstValue = new NeonObjectOne();
        object.mSecondValue = new NeonObjectTwo();

        setNeonObjectOneValue(object.mFirstValue);
        setNeonObjectTwoValue(object.mSecondValue);
        object.status = "incoming";

        object.mArrayPet = new NeonObjectOne[100];
        for (int i = 0; i < object.mArrayPet.length; i++) {
            object.mArrayPet[i] = new NeonObjectOne();
            setNeonObjectOneValue(object.mArrayPet[i]);
        }

        object.mArrayCats = new NeonObjectTwo[100];
        for (int i = 0; i < object.mArrayCats.length; i++) {
            object.mArrayCats[i] = new NeonObjectTwo();
            setNeonObjectTwoValue(object.mArrayCats[i]);
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

    void assertNeonObject(NeonObject origin, NeonObject result) {
        if (origin == null && result == null) {
            return;
        }
        assertArrayEquals(origin.mSize, result.mSize);
        assertArrayEquals(origin.mPoint, result.mPoint, FLOAT_DELTA);
        assertArrayEquals(origin.mSeconds, result.mSeconds, DOUBLE_DELTA);
        assertArrayEquals(origin.mSizeWrong, result.mSizeWrong);
        assertNeonObjectOne(origin.mFirstValue, result.mFirstValue);
        assertNeonObjectTwo(origin.mSecondValue, result.mSecondValue);
        assertEquals(origin.status, result.status);

        assertEquals(origin.mArrayPet.length, result.mArrayPet.length);
        for (int i = 0; i < origin.mArrayPet.length; i++) {
            assertNeonObjectOne(origin.mArrayPet[i], result.mArrayPet[i]);
        }
        assertEquals(origin.mArrayCats.length, result.mArrayCats.length);
        for (int i = 0; i < origin.mArrayCats.length; i++) {
            assertNeonObjectTwo(origin.mArrayCats[i], result.mArrayCats[i]);
        }
    }

    void setNeonPig(NeonPig pig) {
        pig.mType = NeonAnimal.PIG;
        pig.weight = new NeonObjectOne();
        setNeonObjectOneValue(pig.weight);
        pig.height = new NeonObjectTwo[getInt() % 100 + 10];
        for (int i = 0; i < pig.height.length; i++) {
            pig.height[i] = new NeonObjectTwo();
            setNeonObjectTwoValue(pig.height[i]);
        }
    }

    void assertNeonPig(NeonPig origin, NeonPig result) {
        assertNeonObjectOne(origin.weight, result.weight);
        assertEquals(origin.height.length, result.height.length);
        for (int i = 0; i < origin.height.length; i++) {
            assertNeonObjectTwo(origin.height[i], result.height[i]);
        }
    }

    void setNeonDog(NeonDog dog) {
        dog.mType = NeonAnimal.DOG;
        dog.foods = "dogFood";
        dog.eatAny = new NeonPrimitive();
        setNeonPrimitiveProperty(dog.eatAny);
    }

    void assertNeonDog(NeonDog origin, NeonDog result) {
        assertNeonPrimitive(origin.eatAny, result.eatAny);
        assertEquals(origin.foods, result.foods);
    }

    void setNeonCat(NeonCat cat) {
        cat.mType = NeonAnimal.CAT;
        cat.foods = new int[getInt() % 100 + 10];
        for (int i = 0; i < cat.foods.length; i++) {
            cat.foods[i] = getInt();
        }
        cat.playable = new NeonObjectOne[getInt() % 100 + 10];
        for (int i = 0; i < cat.playable.length; i++) {
            cat.playable[i] = new NeonObjectOne();
            setNeonObjectOneValue(cat.playable[i]);
        }
    }

    void assertNeonCat(NeonCat origin, NeonCat result) {
        assertArrayEquals(origin.foods, result.foods);
        assertEquals(origin.playable.length, result.playable.length);
        for (int i = 0; i < origin.playable.length; i++) {
            assertNeonObjectOne(origin.playable[i], result.playable[i]);
        }
    }

    void setNeonCustomAnimal(NeonCustomAnimal base) {
        base.animals = new NeonAnimal[getInt() % 100 + 10];
        for (int i = 0; i < base.animals.length; i++) {
            int rand = Math.abs(random.nextInt()) % 3;
            switch (rand) {
                case 0:
                    base.animals[i] = new NeonCat();
                    setNeonCat((NeonCat) base.animals[i]);
                    break;
                case 1:
                    base.animals[i] = new NeonPig();
                    setNeonPig((NeonPig) base.animals[i]);
                    break;
                case 2:
                    base.animals[i] = new NeonDog();
                    setNeonDog((NeonDog) base.animals[i]);
                    break;
            }
        }
    }

    void assertNeonCustomAnimal(NeonCustomAnimal origin, NeonCustomAnimal result) {
        assertEquals(origin.animals.length, result.animals.length);
        for (int i = 0; i < origin.animals.length; i++) {
            if (origin.animals[i] instanceof NeonCat) {
                assertNeonCat((NeonCat) origin.animals[i], (NeonCat) result.animals[i]);
            } else if (origin.animals[i] instanceof NeonDog) {
                assertNeonDog((NeonDog) origin.animals[i], (NeonDog) result.animals[i]);
            } else if (origin.animals[i] instanceof NeonPig) {
                assertNeonPig((NeonPig) origin.animals[i], (NeonPig) result.animals[i]);
            }
        }
    }

    void setNeonRoot(NeonParent root) {
        root.x = getInt();
        root.y = getInt();
        root.z = getInt();

        root.animals = new NeonAnimal[getInt() % 100 + 10];
        for (int i = 0; i < root.animals.length; i++) {
            int rand = Math.abs(random.nextInt()) % 3;
            switch (rand) {
                case 0:
                    root.animals[i] = new NeonCat();
                    setNeonCat((NeonCat) root.animals[i]);
                    break;
                case 1:
                    root.animals[i] = new NeonPig();
                    setNeonPig((NeonPig) root.animals[i]);
                    break;
                case 2:
                    root.animals[i] = new NeonDog();
                    setNeonDog((NeonDog) root.animals[i]);
                    break;
            }
        }
    }

    void assertNeonRoot(NeonParent origin, NeonParent result) {
        assertEquals(origin.x, result.x);
        assertEquals(origin.y, result.y);
        assertEquals(origin.z, result.z);

        assertEquals(origin.animals.length, result.animals.length);
        for (int i = 0; i < origin.animals.length; i++) {
            if (origin.animals[i] instanceof NeonCat) {
                assertNeonCat((NeonCat) origin.animals[i], (NeonCat) result.animals[i]);
            } else if (origin.animals[i] instanceof NeonDog) {
                assertNeonDog((NeonDog) origin.animals[i], (NeonDog) result.animals[i]);
            } else if (origin.animals[i] instanceof NeonPig) {
                assertNeonPig((NeonPig) origin.animals[i], (NeonPig) result.animals[i]);
            }
        }
    }

    void setNeonChild(NeonChild child) {
        setNeonRoot(child);
        child.daddyName = "Khung Long Bao Chua";
    }

    void assertNeonChild(NeonChild origin, NeonChild result) {
        assertNeonRoot(origin, result);
        assertEquals(origin.daddyName, result.daddyName);
    }

    void assertNeonBike(NeonBike origin, NeonBike result) {
        assertEquals(origin.maxSpeed, result.maxSpeed);
    }

    void assertNeonCar(NeonCar origin, NeonCar result) {
        assertEquals(origin.numberOfSeat, result.numberOfSeat);
        assertEquals(origin.maxSpeed, result.maxSpeed);
    }

    void assertNeonVehicle(NeonVehicle origin, NeonVehicle result) {
        if (origin instanceof NeonBike && result instanceof NeonBike) {
            assertNeonBike((NeonBike) origin, (NeonBike) result);
        } else if (origin instanceof NeonCar && result instanceof NeonCar) {
            assertNeonCar((NeonCar) origin, (NeonCar) result);
        } else {
            fail();
        }
    }
}
