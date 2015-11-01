package entity;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class GenericToken<T> implements IGenericToken<T> {
    protected T _value;

    protected GenericToken() {
    }

    protected GenericToken(T value) {
        _value = value;
    }

    @Override
    public T getValue() {
        return _value;
    }

    @Override
    public void setValue(T value) {
        _value = value;
    }
}
