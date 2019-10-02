package com.zing.zalo.neon2.test;

import com.zing.zalo.neon2.export.SerializedInput;
import com.zing.zalo.neon2.export.SerializedOutput;
import com.zing.zalo.neon2.export.TwitterSerializerInput;
import com.zing.zalo.neon2.export.TwitterSerializerOutput;
import com.zing.zalo.neon2.export.exception.NeonException;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by Tien Loc Bui on 02/10/2019.
 */
public class NeonCoreTest {
    @Test
    public void neonParentTest() throws NeonException {
        NeonParent expected = getSample();
        TwitterSerializerOutput output = new TwitterSerializerOutput();
        $NeonNeonParent__.serialize(output, expected);
        byte[] binData = output.toByteArray();
        SerializedInput input = TwitterSerializerInput.fromByteArray(binData);
        NeonParent actual = $NeonNeonParent__.deserialize(input);
        compare(actual, expected);
    }

    private NeonParent getSample() {
        NeonParent result = new NeonParent();
        result.byteA = 2;
        result.booleanA = true;
        result.charA = '2';
        result.shortA = -4;
        result.intA = -8;
        result.longA = 2000000000000L;
        result.floatA = 8.84f;
        result.doubleA = 9.93f;
        result.byteB = new byte[]{2, 4, 2, 1, 127, 0 - 127};
        result.booleanB = new boolean[]{false, true, true, false, true};
        result.charB = new char[]{'a', 'b', 'z', 'k'};
        result.shortB = new short[]{32767, 0, -32767, 0};
        result.intB = new int[]{1, 2, 3, 4, 5, 6, 7};
        result.longB = new long[]{9999, 8888};
        result.floatB = new float[]{1.1f, 2.2f, 3.3f, 4.4f};
        result.doubleB = new double[]{-1.1d, -2.2d, -3.3d, -4.4d};

        NeonBaby baby1 = new NeonBaby(null, 20);
        NeonBaby baby2 = new NeonBaby(null, 8);
        NeonBaby baby3 = new NeonBaby(baby2, 14);
        NeonBaby baby4 = new NeonBaby(null, 14);

        result.babyA = baby1;
        result.babyB = new NeonBaby[]{baby3, baby4};
        return result;
    }

    private void compare(NeonParent actual, NeonParent expected) {
        assertEquals(actual.byteA, expected.byteA);
        assertEquals(actual.booleanA, expected.booleanA);
        assertEquals(actual.charA, expected.charA);
        assertEquals(actual.shortA, expected.shortA);
        assertEquals(actual.intA, expected.intA);
        assertEquals(actual.longA, expected.longA);
        assertEquals(actual.floatA, expected.floatA, 0.001f);
        assertEquals(actual.doubleA, expected.doubleA, 0.001);
        assertEquals(actual.babyA, expected.babyA);
        assertArrayEquals(actual.byteB, expected.byteB);
        assertArrayEquals(actual.booleanB, expected.booleanB);
        assertArrayEquals(actual.charB, expected.charB);
        assertArrayEquals(actual.shortB, expected.shortB);
        assertArrayEquals(actual.intB, expected.intB);
        assertArrayEquals(actual.longB, expected.longB);
        assertArrayEquals(actual.floatB, expected.floatB, 0.001f);
        assertArrayEquals(actual.doubleB, expected.doubleB, 0.001);
        assertArrayEquals(actual.babyB, expected.babyB);
    }
}
