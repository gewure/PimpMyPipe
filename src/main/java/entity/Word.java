package entity;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class Word extends GenericToken<String> {
    private final StringBuilder _builder;

    public Word(){
        super();

        _builder = new StringBuilder();
    }

    public Word(String value) {
        super(value);

        _builder = new StringBuilder();
        _builder.append(value);
    }

    @Override
    public String getValue() {
        return _builder.toString();
    }

    @Override
    public void setValue(String value) {
        _builder.append(value);
    }

    public void appendToken(Token token) {
        _builder.append(token.getValue());
    }
}
