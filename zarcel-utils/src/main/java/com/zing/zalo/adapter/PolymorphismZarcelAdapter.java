package com.zing.zalo.adapter;

import com.zing.zalo.helper.DebugBuilder;
import com.zing.zalo.exception.ZarcelDuplicateException;
import com.zing.zalo.exception.ZarcelNotFoundException;
import com.zing.zalo.exception.ZarcelRuntimeException;
import com.zing.zalo.annotations.NonNull;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class PolymorphismZarcelAdapter<T> implements ZarcelAdapter<T> {

    private TreeMap<Integer, Class> children;

    protected abstract void onRegisterChildClasses();

    protected PolymorphismZarcelAdapter() {
        children = new TreeMap<>();
        onRegisterChildClasses();
    }

    protected void registryAdd(int type, Class child) throws ZarcelDuplicateException {
        if (children.containsKey(type)) {
            throw new ZarcelDuplicateException("Class " + child.getName() + " have already registered.");
        } else {
            children.put(type, child);
        }
    }

    protected void registryUpdate(int type, Class child) throws ZarcelNotFoundException {
        if (!children.containsKey(type)) {
            throw new ZarcelNotFoundException("Class " + child.getName() + " does not exist.");
        } else {
            children.put(type, child);
        }
    }

    protected void registryRemove(int type, Class child) throws ZarcelNotFoundException {
        if (!children.containsKey(type)) {
            throw new ZarcelNotFoundException("Class " + child.getName() + " does not exist.");
        } else {
            children.remove(type);
        }
    }

    protected boolean isRegistered(int type) {
        return children.containsKey(type);
    }

    @Override
    public void serialize(@NonNull T object, SerializedOutput writer) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method;
        for (Map.Entry<Integer, Class> child : children.entrySet()) {
            if (child.getValue().isInstance(object)) {
                writer.writeInt32(child.getKey());
                method = child.getValue().getMethod("serialize", SerializedOutput.class);
                method.invoke(object, writer);
                return;
            }
        }
        throw new ZarcelRuntimeException(object.getClass().getName() + "does not exist in Polymorphism Adapter.");
    }

    @Override
    public T createFromSerialized(SerializedInput reader, DebugBuilder builder) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Method method;

        int type = reader.readInt32();
        if (builder != null)
            builder.addIntAttr("type", (type));
        for (Map.Entry<Integer, Class> child : children.entrySet()) {
            if (child.getKey() == type) {
                if (builder != null)
                    builder.addObject(child.getValue().getSimpleName().toLowerCase());
                method = child.getValue().
                        getField("CREATOR")
                        .getType()
                        .getMethod("createFromSerialized", SerializedInput.class, DebugBuilder.class);
                Object creator = child.getValue().newInstance().getClass().getDeclaredField("CREATOR");
                T childObject = (T) method.invoke(((Field) creator).get("CREATOR"), reader, builder);
                return childObject;
            }
        }
        return null;
    }
}
