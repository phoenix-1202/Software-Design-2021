import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LRUCache<K, V> {
    private final Map<K, V> map = new HashMap<>();
    private final List<K> list = new LinkedList<>();

    private final int capacity;

    LRUCache() {
        capacity = 100;
    }

    LRUCache(int size) {
        assert (size > 0);
        capacity = size;
    }

    void add(K key, V value) {
        if (map.containsKey(key)) {
            map.replace(key, value);
            request(key);
            return;
        }
        if (list.size() == capacity) {
            K oldest = list.get(0);
            map.remove(oldest);
            list.remove(oldest);
        }
        map.put(key, value);
        list.add(key);
    }

    void request(K key) {
        assert (list.contains(key));
        list.remove(key);
        list.add(key);
    }

    V get(K key) {
        assert (map.containsKey(key));
        return map.get(key);
    }

    int getSize() {
        return list.size();
    }

    K getTopKey() {
        assert (list.size() != 0);
        return list.get(list.size() - 1);
    }
}
