package com.zalo.zarcel.adapter;

import com.zalo.zarcel.Exception.ZarcelDuplicateException;
import com.zalo.zarcel.Exception.ZarcelNotFoundException;
import com.zalo.zarcel.Exception.ZarcelRuntimeException;
import com.zing.zalo.data.serialization.SerializedInput;
import com.zing.zalo.data.serialization.SerializedOutput;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

public abstract class AbstractAdapter<T> implements ZarcelAdapter<T> {

    private ArrayList<Map.Entry<Integer, Class>> children;

    public enum RegisterType {ADD, UPDATE_CLASS, UPDATE_TYPE}

    protected abstract void onRegisterChildClasses();

    protected AbstractAdapter() {
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
    public void serialize(T object, SerializedOutput writer) {
        Method method;
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getValue().isInstance(object)) {
                try {
                    writer.writeInt32(children.get(i).getKey());
                    method = children.get(i).getValue().getMethod("serialize", SerializedOutput.class);
                    method.invoke(object, writer);
                    return;
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e.toString());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.toString());
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e.toString());
                }
            }
        }
        throw new ZarcelRuntimeException(object.getClass().getName() + "does not exist in Abstract Adapter.");
    }

    @Override
    public T createFromSerialized(SerializedInput reader) {
        Method method;
        int type = reader.readInt32();
        for (int i = 0; i < children.size(); i++) {
            if (children.get(i).getKey() == type) {
                try {
                    method = children.get(i).getValue().
                            getField("CREATOR")
                            .getType()
                            .getMethod("createFromSerialized", SerializedInput.class);
                    Object creator = children.get(i).getValue().newInstance().getClass().getDeclaredField("CREATOR");
                    T result = (T) method.invoke(((Field) creator).get("CREATOR"), reader);
                    return result;
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e.toString());
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.toString());
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e.toString());
                } catch (NoSuchFieldException e) {
                    throw new RuntimeException(e.toString());
                } catch (InstantiationException e) {
                    throw new RuntimeException(e.toString());
                }
            }
        }

        return null;
    }
}
