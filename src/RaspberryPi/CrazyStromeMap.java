package RaspberryPi;

import java.util.*;

public class CrazyStromeMap<K, V> {
    public Map<K, V> map = Collections.synchronizedMap(new HashMap<>());

    public synchronized void removeByValue(Object value) {
        for (Object key : map.keySet()) {
            if (map.get(key) == value) {
                map.remove(key);
                break;
            }
        }
    }
    public synchronized Set<V> valueSet() {
        Set<V> result = new HashSet<>();
        map.forEach((key, value) -> result.add(value));
        return result;
    }
    public synchronized K getKeyByValue(V val) {
        for (K key: map.keySet()) {
            if (map.get(key) == val || map.get(key).equals(val)) {
                return key;
            }
        }
        return null;
    }
    public synchronized V put(K key, V value) {
        for (V val : valueSet()) {
            if (val.equals(value) && val.hashCode() == value.hashCode()) {
                throw new RuntimeException("MyMap中不允许有重复Value");
            }
        }
        return map.put(key, value);
    }
}