//package com.zing.zalo.neon.adapter;
//
//import com.zing.neon.data.serialization.SerializedInput;
//import com.zing.neon.data.serialization.SerializedOutput;
//import com.zing.zalo.neon.annotations.NonNull;
//import com.zing.zalo.neon.exception.NeonDuplicateException;
//import com.zing.zalo.neon.exception.NeonNotFoundException;
//import com.zing.zalo.neon.exception.NeonRuntimeException;
//import com.zing.zalo.neon.helper.DebugBuilder;
//import java.lang.reflect.Field;
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//import java.util.Map;
//import java.util.TreeMap;
//
//public abstract class PolymorphismNeonAdapter<T> implements NeonAdapter<T> {
//
//    private TreeMap<Integer, Class> children;
//
//    protected abstract void onRegisterChildClasses();
//
//    protected PolymorphismNeonAdapter() {
//        children = new TreeMap<>();
//        onRegisterChildClasses();
//    }
//
//    protected void registryAdd(int type, Class child) throws NeonDuplicateException {
//        if (children.containsKey(type)) {
//            throw new NeonDuplicateException(
//                    "Class " + child.getName() + " have already registered.");
//        } else {
//            children.put(type, child);
//        }
//    }
//
//    protected void registryUpdate(int type, Class child) throws NeonNotFoundException {
//        if (!children.containsKey(type)) {
//            throw new NeonNotFoundException("Class " + child.getName() + " does not exist.");
//        } else {
//            children.put(type, child);
//        }
//    }
//
//    protected void registryRemove(int type, Class child) throws NeonNotFoundException {
//        if (!children.containsKey(type)) {
//            throw new NeonNotFoundException("Class " + child.getName() + " does not exist.");
//        } else {
//            children.remove(type);
//        }
//    }
//
//    protected boolean isRegistered(int type) {
//        return children.containsKey(type);
//    }
//
//    @Override
//    public void serialize(@NonNull T object, SerializedOutput writer)
//            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        Method method;
//        for (Map.Entry<Integer, Class> child : children.entrySet()) {
//            if (child.getValue().isInstance(object)) {
//                writer.writeInt32(child.getKey());
//                method = child.getValue().getMethod("serialize", SerializedOutput.class);
//                method.invoke(object, writer);
//                return;
//            }
//        }
//        throw new NeonRuntimeException(
//                object.getClass().getName() + "does not exist in Polymorphism Adapter.");
//    }
//
//    @Override
//    public T createFromSerialized(SerializedInput reader, DebugBuilder builder)
//            throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException,
//            InstantiationException, InvocationTargetException {
//        Method method;
//
//        int type = reader.readInt32();
//        if (builder != null) {
//            builder.addCustomAttr("type", String.valueOf(type), 4);
//        }
//        for (Map.Entry<Integer, Class> child : children.entrySet()) {
//            if (child.getKey() == type) {
//                if (builder != null) {
//                    builder.addObjectAttrName(child.getValue().getSimpleName().toLowerCase());
//                }
//                method = child.getValue().
//                        getField("CREATOR")
//                        .getType()
//                        .getMethod("createFromSerialized", SerializedInput.class,
//                                DebugBuilder.class);
//                Object creator =
//                        child.getValue().newInstance().getClass().getDeclaredField("CREATOR");
//                T childObject =
//                        (T) method.invoke(((Field) creator).get("CREATOR"), reader, builder);
//                return childObject;
//            }
//        }
//        return null;
//    }
//}
