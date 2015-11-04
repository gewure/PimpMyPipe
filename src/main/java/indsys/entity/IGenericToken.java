package indsys.entity;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public interface IGenericToken<T, U> {
    T getLineIndex();

    void setId(T id);

    U getValue();

    void setValue(U value);
}
