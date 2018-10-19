package com.zalo.zing.customadapter;

import com.zing.zalo.adapter.ZarcelAdapter;
import com.zing.zalo.data.serialization.DebugBuilder;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

public class AnimalAdapter implements ZarcelAdapter<ZarcelAnimal[]> {
    @Override
    public void serialize(ZarcelAnimal[] object, SerializedOutput writer) {
        writer.writeInt32(object.length);
        for (int i = 0; i < object.length; i++) {
            ZarcelAnimal animal = object[i];
            writer.writeInt32(animal.mType);
            if (animal instanceof ZarcelPig) {
                ((ZarcelPig) animal).serialize(writer);
            } else if (animal instanceof ZarcelDog) {
                ((ZarcelDog) animal).serialize(writer);
            } else if (animal instanceof ZarcelCat) {
                ((ZarcelCat) animal).serialize(writer);
            }
        }
    }

    @Override
    public ZarcelAnimal[] createFromSerialized(SerializedInput reader, DebugBuilder builder) {
        int size = reader.readInt32();
        ZarcelAnimal[] result = new ZarcelAnimal[size];
        for (int i = 0; i < size; i++) {
            int type = reader.readInt32();
            if (builder != null) {
                builder.addObject("ZarcelAnimal[" + i + "]");
            }
            if (type == ZarcelAnimal.CAT) {
                result[i] = ZarcelCat.CREATOR.createFromSerialized(reader, builder);
            } else if (type == ZarcelAnimal.DOG) {
                result[i] = ZarcelDog.CREATOR.createFromSerialized(reader, builder);
            } else if (type == ZarcelAnimal.PIG) {
                result[i] = ZarcelPig.CREATOR.createFromSerialized(reader, builder);
            }
        }
        return result;
    }
}
