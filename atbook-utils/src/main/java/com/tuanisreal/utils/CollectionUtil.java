package com.tuanisreal.utils;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class CollectionUtil {
    private CollectionUtil() {
    }

    public static boolean isEmpty(Collection<?> collection) {
        return CollectionUtils.isEmpty(collection);
    }

    public static boolean isEmpty(Stream<?> stream) {
        return stream == null;
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return CollectionUtils.isEmpty(map);
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static <K, E> Map<K, E> toMap(Collection<E> collection, Function<E, K> getKeyFunc) {
        return (Map)(isEmpty(collection) ? new HashMap() : toMap(collection.stream(), getKeyFunc));
    }

    public static <E, K, V> Map<K, V> toMap(Collection<E> collection, Function<E, K> getKeyFunction, Function<E, V> getValueFunction) {
        return (Map)(isEmpty(collection) ? new HashMap() : toMap(collection.stream(), getKeyFunction, getValueFunction));
    }

    public static <E, K, V> Map<K, V> toMap(Collection<E> collection, Function<E, K> getKeyFunction, Function<E, V> getValueFunction, Supplier<Map<K, V>> mapSupplier) {
        return isEmpty(collection) ? (Map)mapSupplier.get() : toMap(collection.stream(), getKeyFunction, getValueFunction, mapSupplier);
    }

    public static <K, E> Map<K, E> toMap(Stream<E> stream, Function<E, K> getKeyFunction) {
        if (isEmpty(stream)) {
            return new HashMap();
        } else {
            Map<K, E> result = new HashMap();
            stream.forEach((element) -> {
                result.put(getKeyFunction.apply(element), element);
            });
            return result;
        }
    }

    public static <E, K, V> Map<K, V> toMap(Stream<E> stream, Function<E, K> getKeyFunction, Function<E, V> getValueFunction) {
        return toMap(stream, getKeyFunction, getValueFunction, HashMap::new);
    }

    public static <E, K, V> Map<K, V> toMap(Stream<E> stream, Function<E, K> getKeyFunction, Function<E, V> getValueFunction, Supplier<Map<K, V>> mapSupplier) {
        if (isEmpty(stream)) {
            return (Map)mapSupplier.get();
        } else {
            Map<K, V> result = (Map)mapSupplier.get();
            stream.forEach((element) -> {
                result.put(getKeyFunction.apply(element), getValueFunction.apply(element));
            });
            mapSupplier.get();
            return result;
        }
    }
}
