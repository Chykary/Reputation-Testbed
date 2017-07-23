package com.ma.Misc;


public class KeyValuePairWithLabel<K extends Comparable<K>, V> implements Comparable<KeyValuePairWithLabel<K, V>> {

    private final K key;
    private final V value;
    private final String s;

    public KeyValuePairWithLabel(K key, V value, String s) {
        this.key = key;
        this.value = value;
        this.s = s;
    }

    // Arbitrarily decide to sort null keys first.  Values are disregarded for
    // the purposes of comparison.
    @Override
    public int compareTo(KeyValuePairWithLabel<K, V> item) {
        if (this.key == null) {
            return (item.key == null) ? 0 : -1;
        } else {
            return this.key.compareTo(item.key);
        }
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public String getString() {
        return this.s;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !this.getClass().equals(other.getClass())) {
            return false;
        }
        KeyValuePairWithLabel item = (KeyValuePairWithLabel) other;
        return (this.key == null ? item.key == null : this.key.equals(item.key)) &&
                (this.value == null ? item.value == null : this.value.equals(item.value));
    }

    @Override
    public int hashCode() {
        return ((this.key == null) ? 0 : this.key.hashCode()) ^
                ((this.value == null) ? 0 : this.value.hashCode());
    }
}