package entity;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public interface IGenericToken<T> {
    T getValue();
    void setValue(T value);
}
