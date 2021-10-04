import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {

    @Test
    public void test1InitialCapacityIsNegative() {
        assertThrows(AssertionError.class, () -> {
            LRUCache<Integer, Integer> cache = new LRUCache<>(-10);
        });
    }

    @Test
    public void test2TopKey() {
        LRUCache<Integer, Integer> cache = new LRUCache<>(5);
        assertThrows(AssertionError.class, () -> System.out.println(cache.getTopKey()));
    }

    @Test
    public void test3Add() {
        LRUCache<Integer, Integer> cache = new LRUCache<>(3);
        cache.add(1, 1);
        cache.add(2, 2);
        cache.add(3, 3);
        cache.add(4, 4);
        assertEquals(cache.getSize(), 3);
        assertEquals(cache.get(2), 2);
        assertEquals(cache.get(3), 3);
        assertEquals(cache.get(4), 4);
        assertThrows(AssertionError.class, () -> System.out.println(cache.get(1)));
    }

    @Test
    public void test4AddWithRequest() {
        LRUCache<Integer, Integer> cache = new LRUCache<>(3);
        cache.add(1, 1);
        cache.add(2, 2);
        cache.add(3, 3);
        cache.request(1);
        cache.add(4, 4);
        assertEquals(cache.getSize(), 3);
        assertEquals(cache.get(3), 3);
        assertEquals(cache.get(1), 1);
        assertEquals(cache.get(4), 4);
        assertThrows(AssertionError.class, () -> System.out.println(cache.get(2)));
    }

    @Test
    public void test5AddAndSetValue() {
        LRUCache<Integer, Integer> cache = new LRUCache<>(3);
        cache.add(1, 1);
        cache.add(2, 2);
        cache.add(1, 10);
        cache.add(3, 3);
        cache.add(4, 4);
        cache.request(3);
        assertEquals(cache.getSize(), 3);
        assertEquals(cache.get(1), 10);
        assertEquals(cache.get(3), 3);
        assertEquals(cache.get(4), 4);
        assertThrows(AssertionError.class, () -> cache.request(2));
    }

    @Test
    public void test6WithoutInitialCapacity() {
        LRUCache<Integer, Integer> cache = new LRUCache<>();
        cache.add(1, 1);
        cache.add(2, 2);
        cache.request(1);
        cache.add(3, 3);
        cache.add(4, 4);
        cache.request(3);
        cache.add(5, 5);
        cache.add(6, 6);
        cache.request(5);
        assertEquals(cache.getSize(), 6);
        assertEquals(cache.get(1), 1);
        assertEquals(cache.get(2), 2);
        assertEquals(cache.get(3), 3);
        assertEquals(cache.get(4), 4);
        assertEquals(cache.get(5), 5);
        assertEquals(cache.get(6), 6);
        assertEquals(cache.getTopKey(), 5);
    }
}