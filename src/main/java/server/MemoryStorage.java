package server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryStorage {
    private static final Map<String, String> storage = new ConcurrentHashMap<>();

    public static void save(String key, String value) {
        storage.put(key, value);
    }

    public static String get(String key) {
        if (!storage.containsKey(key)) {
            return "NULL";
        }
        return storage.get(key);
    }

    public static void clear() {
        storage.clear();
    }

}
