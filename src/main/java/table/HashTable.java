package table;

import org.jetbrains.annotations.NotNull;

import java.util.*;

class HTable<K, V> extends Hashtable<K, V> implements Map<K, V> {
    private int numPair = 5; // максимальное кол-во пар (по умолчанию в Java 11)
    private int mod; //счетчик изменения таблицы
    private int size = 0;
    private Pair<K, V>[] table;
    private double load; // степень заполнения
    private int prime;

    //конструктор
    public HTable() {
        load = 0.75;
        table = new Pair[numPair];
        prime = getPrime();
        mod = 0;
    }

    //получение простого числа меньшего размера таблицы для HashCode2
    private int getPrime() {
        int limit = numPair - 1;
        if (limit == 2) return numPair;
        boolean[] a = new boolean[numPair];
        Arrays.fill(a, 2, numPair, true);
        for (int i = 2; i < limit; i++)
            if (a[i]) {
                int j = 2;
                while (j * i < limit) {
                    a[i * j] = false;
                    j++;
                }
            }
        int res = 3;
        for (int i = limit - 1; i >= 0; i--)
            if (a[i]) {
                res = i;
                break;
            }
        return res;
    }

    // увеличение массива пар если size == numPair * load
    @Override
    protected void rehash() {
        Map<K, V> map = toMap();
        numPair = (table.length << 1) + 1;
        table = new Pair[numPair];
        prime = getPrime();
        putAll(map);
        size = map.size();
        mod++;
    }

    // основная хэш-фунция для вычисления хэш-кода
    private int hashFunction1(Object key) {
        return key.hashCode() % table.length;
    }

    // дополнительная хэш-функция для вычисления хэш-кода
    private int hashFunction2(Object key) {
        return prime - key.hashCode() % prime;
    }

    //добавлене в таблицу пары ключ значение
    @Override
    public V put(K key, V value) {
        int hash1 = hashFunction1(key);
        int hash2 = hashFunction2(key);

        if (value == null) {
            throw new NullPointerException();
        }
        // добавление пары, если ячейка по данному hash1 == null
        if (table[hash1] == null) {
            table[hash1] = new Pair<>(key, value);
            size++;
            // перезапись значения при одинаковом ключе.(поиск ключа по hash1]
        } else if (table[hash1].getKey().equals(key)) {
            V res = table[hash1].getValue();
            table[hash1] = new Pair<>(key, value);
            return res;
        } else {
            int i = 1;
            while (true) {
                int index = (hash1 + i++ * hash2) % numPair; // формула для вычисления новой хэш-функции
                // добавление пары в таблицу.
                // Если при добавлении пары место в таблице по ее hash1 занято, то происходит добавление по index.
                if (table[index] == null) {
                    table[index] = new Pair<>(key, value);
                    size++;
                    break;
                    // перезапись значения при одинковом ключе.
                    // (Если ключ не нашелся по hash1, то мы производим поиск по index)
                } else if (table[index].getKey().equals(key)) {
                    V res = table[index].getValue();
                    table[index] = new Pair<>(key, value);
                    return res;
                }
            }
        }
        // увеличение массива пар
        if (size == new Double(numPair * load).intValue())
            rehash();
        mod++;
        return null;
    }

    // получение значение по ключу
    @Override
    public V get(Object key) {
        for (Map.Entry<K, V> pair : entrySet()) {
            if (pair.getKey().equals(key))
                return pair.getValue();
        }
        return null;
    }

    //удаление значения по ключу
    @Override
    public V remove(Object key) {
        if (keySet().contains(key)) {
            int i = 0;
            int hash1 = hashFunction1(key);
            int hash2 = hashFunction2(key);
            while (!table[(hash1 + i * hash2) % numPair].getKey().equals(key))
                i++;
            V tmp = table[(hash1 + i * hash2) % numPair].getValue();
            table[(hash1 + i * hash2) % numPair] = null;
            size--;
            mod++;
            return tmp;
        }
        return null;
    }

    //получение количество пар ключ-значение
    public int size() {
        return this.size;
    }

    // проверка наличия пар в таблице
    public boolean isEmpty() {
        return this.size == 0;
    }

    // очистить таблицу
    public void clear() {
        table = new Pair[numPair];
    }

    // содержит ли таблица ключ
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    // содержит ли таблица значение
    public boolean containsValue(Object value) {
        return values().contains(value);
    }


    public void putAll(Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    // возвращение всех ключей
    @NotNull
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (Map.Entry<K, V> pair : entrySet())
            keys.add(pair.getKey());
        return keys;
    }

    // возвращение всех значений
    @NotNull
    public Collection<V> values() {
        Set<V> vals = new HashSet<>();
        for (Map.Entry<K, V> pair : entrySet())
            vals.add(pair.getValue());
        return vals;
    }

    // возвращает все элементы в виде объектов
    @NotNull
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> set = new HashSet<>();
        for (Pair<K, V> pair : table) {
            if (pair != null)
                set.add(new Pair<>(pair.getKey(), pair.getValue()));
        }
        return set;
    }

    private Map<K, V> toMap() {
        Map<K, V> map = new HashMap<>();
        for (Pair<K, V> pair : table) {
            if (pair != null)
                map.put(pair.getKey(), pair.getValue());
        }
        return map;
    }

    public String toString() {
        if (size == 0) {
            return "{}";
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append('{');
            int i = 0;
            for (Map.Entry<K, V> e : entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                sb.append(key == this ? "(this Map)" : key.toString());
                sb.append('=');
                sb.append(value == this ? "(this Map)" : value.toString());
                if (++i < size)
                    sb.append(", ");
            }
            return sb.append('}').toString();
        }
    }

    private class Iter<T> implements Iterator<T> {
        int index = 0, mod = 0;
        T cur; //текущий элемент перебора таблицы

        Iter() {
            this.mod = HTable.this.mod; // копируем значение счетчика в счетчик итератора
        }

        @Override
        public boolean hasNext() {
            do {
                cur = (T) HTable.this.table[index++];
            } while (cur == null && index < HTable.this.numPair);
            return cur != null;
        }

        @Override
        public T next() {
            if (mod != HTable.this.mod)
                throw new ConcurrentModificationException();
            return cur;
        }

        @Override
        public void remove() {
            if (mod != HTable.this.mod)
                throw new ConcurrentModificationException();
            HTable.this.remove(cur);
            cur = null;
            mod++;
        }
    }

    public Iter iterator() {
        return new Iter<Pair<K, V>>();
    }
}
