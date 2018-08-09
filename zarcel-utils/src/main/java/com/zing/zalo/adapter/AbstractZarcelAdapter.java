package com.zing.zalo.adapter;

import com.zing.zalo.Exception.ZarcelDuplicateException;
import com.zing.zalo.Exception.ZarcelNotFoundException;
import com.zing.zalo.Exception.ZarcelRuntimeException;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractZarcelAdapter<T> implements ZarcelAdapter<T> {

    private ArrayList<Map.Entry<Integer, Class>> children;

    public enum RegisterType {
        ADD, UPDATE_CLASS, UPDATE_TYPE
    }

    protected abstract void onRegisterChildClasses();

    protected AbstractZarcelAdapter() {
        children = new ArrayList<>();
        onRegisterChildClasses();
    }

    protected void register(int type, Class child, RegisterType registerType) throws ZarcelDuplicateException, ZarcelNotFoundException {
        int index;
        switch (registerType) {
            case ADD:
                index = haveType(type);
                if (index != -1) {
                    throw new ZarcelDuplicateException("Class " + child.getName() + " have already registered.");
                } else {
                    children.add(new AbstractMap.SimpleEntry<>(type, child));
                }
                break;
            case UPDATE_TYPE:
                index = haveType(type);
                if (index == -1) {
                    throw new ZarcelNotFoundException("Class " + child.getName() + " does not exist.");
                } else {
                    children.get(index).setValue(child);
                }
                break;
            case UPDATE_CLASS:
                index = haveClass(child);
                if (index == -1) {
                    throw new ZarcelNotFoundException("Class " + child.getName() + " does not exist.");
                } else {
                    children.remove(index);
                    children.add(index, new AbstractMap.SimpleEntry<>(type, child));
                }
                break;
        }
    }

    private int haveType(int type) {
        for (int index = 0; index < children.size(); index++) {
            if (children.get(index).getKey() == type)
                return index;
        }
        return -1;
    }

    private int haveClass(Class child) {
        for (int index = 0; index < children.size(); index++) {
            if (children.get(index).getValue().equals(child))
                return index;
        }
        return -1;
    }

    @Override
    public void serialize(T object, SerializedOutput writer) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method;
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getValue().isInstance(object)) {
                writer.writeInt32(children.get(i).getKey());
                method = children.get(i).getValue().getMethod("serialize", SerializedOutput.class);
                method.invoke(object, writer);
                return;
            }
        }
        throw new ZarcelRuntimeException(object.getClass().getName() + "does not exist in Abstract Adapter.");
    }

    @Override
    public T createFromSerialized(SerializedInput reader) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Method method;

        int type = reader.readInt32();
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getKey() == type) {
                method = children.get(i).getValue().
                        getField("CREATOR")
                        .getType()
                        .getMethod("createFromSerialized", SerializedInput.class);
                Object creator = children.get(i).getValue().newInstance().getClass().getDeclaredField("CREATOR");
                T childObject = (T) method.invoke(((Field) creator).get("CREATOR"), reader);
                return childObject;
            }
        }
        return null;
    }
}
