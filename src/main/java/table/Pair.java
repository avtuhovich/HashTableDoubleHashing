package table;

import java.util.Map;

public class Pair<K, V> implements Map.Entry<K, V> {
    private K key;
    private V value;

    // конструктор создания пары
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;

    }

    // получение ключа
    public K getKey() {
        return key;
    }

    // получение значения
    public V getValue() {
        return value;
    }

    // перезапись значения
    public V setValue(V newValue) {
        value = newValue;
        return value;
    }

    // перезапись ключа
    public K setKey(K newKey) {
        key = newKey;
        return key;
    }

    public String toString() {
        return "<" + this.key.toString() + ", " + this.value.toString() + ">";
    }
}
