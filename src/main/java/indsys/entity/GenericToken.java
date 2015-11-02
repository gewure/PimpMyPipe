package indsys.entity;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class GenericToken<T, U> implements IGenericToken<T, U> {
    protected T _id;  //counts linenumber
    protected U _value;

    protected GenericToken() {
    }

    protected GenericToken(T id) {
        _id = id;
    }

    protected GenericToken(T id, U value) {
        _id = id;
        _value = value;
    }

    @Override
    public T getId() {
        return _id;
    }

    @Override
    public void setId(T id) {
        _id = id;
    }

    @Override
    public U getValue() {
        return _value;
    }

    @Override
    public void setValue(U value) {
        _value = value;
    }
}
