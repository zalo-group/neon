package com.example.libtest;

import com.zalo.zing.customadapter.Human;
import com.zing.zalo.data.serialization.SerializedByteArrayInput;
import com.zing.zalo.data.serialization.SerializedByteArrayOutput;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class HumanTest extends BaseTest {

    Human createHuman(String name, int age, Date birthdate, Human... children) {
        Human human = new Human();
        human.name = name;
        human.age = age;
        human.birthdate = birthdate;
        human.children = children;
        return human;
    }

    private void assertHuman(Human origin, Human result) {
        assertEquals(origin.age, result.age);
        assertEquals(origin.name, result.name);
        assertEquals(origin.birthdate.getTime(), result.birthdate.getTime());
        if ((origin.children == null && result.children != null) || (origin.children != null && result.children == null)) {
            fail();
        }
        if (origin.children != null) {
            assertEquals(origin.children.length, result.children.length);
            for (int i = 0; i < origin.children.length; i++) {
                assertHuman(origin.children[i], result.children[i]);
            }
        }
    }

    @Test
    public void customAdapter() {
        Human human1 = createHuman("Lac Long Quan", 1000, new Date(0),
                createHuman("Con 1", 10, new Date(2000), (Human[]) null),
                createHuman("Con 2", 20, new Date(3000),
                        createHuman("Con 21", 30, new Date(5000))));

        SerializedByteArrayOutput writer = new SerializedByteArrayOutput();
        human1.serialize(writer);
        Human human2 = Human.CREATOR.createFromSerialized(new SerializedByteArrayInput(writer.toByteArray()));
        assertHuman(human1, human2);
    }

    @Test
    public void cyclicDependencies() {
        Human cha = createHuman("Cha", 50, new Date(0));
        Human con = createHuman("Con", 33, new Date(2000));
        cha.children = new Human[]{con};
        con.father = cha;

        SerializedByteArrayOutput writer = new SerializedByteArrayOutput();
        cha.serialize(writer);
        Human result = Human.CREATOR.createFromSerialized(new SerializedByteArrayInput(writer.toByteArray()));
        assertHuman(cha, result);
    }
}
