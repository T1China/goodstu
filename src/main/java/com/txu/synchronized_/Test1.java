package com.txu.synchronized_;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @version v1.0
 * @date 2021/5/12
 * @desc
 * @Since 2021/5/12 10:16
 **/
public class Test1 {
    public static void main(String[] args) throws Exception {
        ArrayList<String> strList = new ArrayList<>();
        strList.add("a");
//            strList.add(1);
        Class listClass = strList.getClass();
        Method m = listClass.getMethod("add", Object.class);
        m.invoke(strList, 1);
        for (Object obj : strList) {
            System.out.println(obj instanceof Integer);
        }
    }
}

class Test09 {
    public void test01(Map<String, Test1> map, List<Test1> list) {
        System.out.println("test01");
    }

    public Map<String, Test1> test02() {
        System.out.println("test02");
        return null;
    }

    public static void main(String[] args) throws NoSuchMethodException, SecurityException {
        Method method = Test09.class.getMethod("test01", Map.class, List.class);
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        for (Type genericParameterType : genericParameterTypes) {
            System.out.println("#" + genericParameterType);
            if (genericParameterType instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) genericParameterType).getActualTypeArguments();
                for (Type actualTypeArgument : actualTypeArguments) {
                    System.out.println(actualTypeArgument);
                }
            }
        }

        method = Test09.class.getMethod("test02", null);
        Type genericReturnType = method.getGenericReturnType();
        if (genericReturnType instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
            for (Type actualTypeArgument : actualTypeArguments) {
                System.out.println(actualTypeArgument);
            }
        }
    }
}


class Parent<T> {
    AtomicInteger updateCount = new AtomicInteger(0);
    private T value;

    @Override
    public String toString() {
        return String.format("value: %s updateCount: %d", value, updateCount.get());
    }

    public void setValue(T value) {
        this.value = value;
        System.out.println("Parent.setValue called");
        updateCount.incrementAndGet();
    }
}

class Child1 extends Parent {
    public void setValue(String value) {
        System.out.println("Child.setValue called");
        super.setValue(value);
    }
}

class Child2 extends Parent<String> {
    @Override
    public void setValue(String value) {
        System.out.println("Child.setValue called");
        super.setValue(value);
    }
}

class Main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Child1 child1 = new Child1();
        Child2 child2 = new Child2();

        List<Method> methods1 = Arrays.stream(child1.getClass().getDeclaredMethods())
//        List<Method> methods1 = Arrays.stream(child1.getClass().getMethods())
                .filter(method -> method.getName().equals("setValue")).collect(Collectors.toList());
        List<Method> methods2 = Arrays.stream(child2.getClass().getDeclaredMethods())
//        List<Method> methods2 = Arrays.stream(child2.getClass().getMethods())
                .filter(method -> method.getName().equals("setValue")).collect(Collectors.toList());

                methods1.forEach(method -> {
                    try {
                        method.invoke(child1, "test");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
        System.out.println(child1.toString());
    }
}