package com.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.stream.Stream;

public class EnumeratedHashMap<T extends Enum> extends HashMap<T, String> {

    private final Class<T> enumerationType;

    public EnumeratedHashMap(final Class<T> enumClazz) {
        this.enumerationType = enumClazz;
    }

    @Override
    public String put(T key, String value) {
        return super.put(key, getEnumValue(enumerationType, key));
    }

    static <T extends Enum> String getEnumValue(Class<T> enumerationType, T key) {
        return Stream.of(enumerationType.getMethods())
                .filter(EnumeratedHashMap::isPublicMethod)
                .filter(EnumeratedHashMap::hasJsonValueAnnotation)
                .filter(EnumeratedHashMap::hasZeroArguments)
                .filter(EnumeratedHashMap::hasStringOutput)
                .findFirst()
                .map(method -> {
                    try {
                        return (String) method.invoke(key);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        return null;
                    }
                })
                .orElse(null);
    }

    private static boolean isPublicMethod(Method method) {
        return Modifier.isPublic(method.getModifiers());
    }

    private static boolean hasStringOutput(Method method) {
        return method.getGenericReturnType().equals(String.class);
    }

    private static boolean hasZeroArguments(Method method) {
        return method.getParameterCount() == 0;
    }

    private static boolean hasJsonValueAnnotation(Method method) {
        return Stream.of(method.getAnnotations())
                .anyMatch(annotation -> annotation.annotationType().equals(JsonValue.class));
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.METHOD})
    public @interface JsonValue {
    }

}
