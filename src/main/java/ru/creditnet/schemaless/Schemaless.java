package ru.creditnet.schemaless;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * @author antivoland
 */
public class Schemaless {
    protected final SortedMap<String, Object> data = new TreeMap<>();

    protected Object get(String key) {
        return data.get(key);
    }

    protected void set(String key, Object value) {
        if (value == null) {
            data.remove(key);
        } else {
            data.put(key, sortedData(value));
        }
    }

    protected boolean has(String key) {
        return data.containsKey(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schemaless s = (Schemaless) o;
        return data.equals(s.data);
    }

    @Override
    public int hashCode() {
        return data.hashCode();
    }

    @SuppressWarnings("unchecked")
    private static Object sortedData(Object value) {
        if (value instanceof Map) {
            return new TreeMap<>(
                    ((Map<?, ?>) value).entrySet().stream()
                            .filter(e -> e.getValue() != null)
                            .collect(toMap(e -> String.valueOf(e.getKey()), e -> sortedData(e.getValue())))
            );
        } else if (value instanceof Collection) {
            return ((Collection<?>) value).stream()
                    .map(Schemaless::sortedData)
                    .sorted(Comparator.comparing(Object::hashCode))
                    .collect(toList());
        } else {
            return value;
        }
    }
}
