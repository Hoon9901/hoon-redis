package server;

import annotattion.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryStorage {
    private static final Map<String, Item> storage = new ConcurrentHashMap<>();

    public static void save(String key, String value) {
        storage.put(key, new Item(value));
    }

    public static void save(String key, String value, Integer expireTime) {
        storage.put(key, new Item(value, expireTime));
    }


    @Nullable
    public static String get(String key) {
        if (!storage.containsKey(key)) {
            return null;
        }
        Item get = storage.get(key);
        if (get.isExpired()) {
            return null;
        }
        return get.value;
    }

    public static void clear() {
        storage.clear();
    }

}
