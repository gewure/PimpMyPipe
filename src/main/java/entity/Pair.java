package entity;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class Pair<T, U> {
    private final T _key;
    private U _value;

    public Pair(T key, U value) {
        _key = key;
        _value = value;
    }

    public T getKey() {
        return _key;
    }

    public U getValue() {
        return _value;
    }

    public void setValue(U value) {
        _value = value;
    }
}
