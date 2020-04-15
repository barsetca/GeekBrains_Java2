package java3lesson07reflection;

import java3lesson07reflection.utilannotation.AfterSuite;
import java3lesson07reflection.utilannotation.BeforeSuite;
import java3lesson07reflection.utilannotation.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TestClassesRunner {

    public static void start(Class testClass) {

//        String simpleName = testClass.getSimpleName();

        System.out.println("////////////////////////////////////////////////////////////////////////////////////");
        System.out.println("Запускаем класс тестов:" + testClass.getName());
        System.out.println("////////////////////////////////////////////////////////////////////////////////////");
//
//        int modifiers = testClass.getModifiers();
//        if (Modifier.isAbstract(modifiers)) {
//            System.out.println(simpleName + "  -  " + "abstract");
//        }
//        if (Modifier.isPublic(modifiers)) {
//            System.out.println(simpleName + "  -  " + "public");
//        }
//
//        if (Modifier.isInterface(modifiers)) {
//            System.out.println(simpleName + "  -  " + "interface");
//        }
//        Annotation[] annotationsClass = testClass.getAnnotations();
//        for (Annotation annotation : annotationsClass) {
//            System.out.println("Аннотация класса " + testClass.getSimpleName() + " - " + annotation.annotationType());
//        }
//
//        Constructor[] constructors = testClass.getDeclaredConstructors();
//
//        for (Constructor constructor : constructors) {
//            System.out.println(constructor);
//            Annotation[] annotationsConstructor = constructor.getAnnotations();
//            for (Annotation annotation : annotationsConstructor) {
//                System.out.println("Аннотация - " + annotation.toString());
//            }
//        }
//
//        Field[] fields = testClass.getDeclaredFields();
//        for (Field field : fields) {
//            System.out.println("Тип поля  - Имя поля " + field.getType().getSimpleName() + " - " + field.getName() + "\n" +
//                    "Полная информция поля " + field.getName() + " класса " + testClass.getSimpleName() + ": " + field);
//            Annotation[] annotationsField = field.getAnnotations();
//            for (Annotation annotation : annotationsField) {
//                System.out.println("Аннотация - " + annotation.toString());
//            }
//        }

        Method[] methods = testClass.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("Метод: " + method.getReturnType() + " | " + method.getName() + " | " + Arrays.toString(method.getParameterTypes()) + "\n" +
                    "Полная информция по методу " + method.getName() + " класса " + testClass.getSimpleName() + ": " + method);
            Annotation[] annotationsMethod = method.getAnnotations();
            for (Annotation annotation : annotationsMethod) {
                System.out.println("Аннотация - " + annotation.toString());
            }
        }

        Object object;
        try {
            object = testClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            System.out.println("Ошибка: у класса - " + testClass.getSimpleName() + " нет конструктора без параметров");
            return;
        }

        Method afterFinishTests = null;
        Map<Integer, List<Method>> mapPriorityMethods = new TreeMap<>();

        for (Method method : methods) {
            if (method.getAnnotation(BeforeSuite.class) != null) {

                try {
                    //method = testClass.getDeclaredMethod("init", null);
                    method.setAccessible(true);
                    method.invoke(testClass);
                    method.setAccessible(false);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            if (method.getAnnotation(AfterSuite.class) != null) {
//                try {
                afterFinishTests = method;
                //testClass.getMethod("printResult", null);
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                }
            }

            Test annotation = method.getAnnotation(Test.class);
            if (annotation != null) {
                int priority = annotation.priority();
                mapPriorityMethods.putIfAbsent(priority, new ArrayList<>());
                mapPriorityMethods.get(priority).add(method);
            }
        }

        for (Map.Entry<Integer, List<Method>> pair : mapPriorityMethods.entrySet()) {
            int priority = pair.getKey();
            List<Method> methodsList = pair.getValue();

            for (Method method : methodsList) {
                System.out.println(method.getName() + " с приоритетом " + priority + " запущен!");
                try {
                    method.setAccessible(true);
                    method.invoke(object);
                    method.setAccessible(false);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        if (afterFinishTests != null) {
            try {
                afterFinishTests.setAccessible(true);
                afterFinishTests.invoke(testClass);
                afterFinishTests.setAccessible(false);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }
}