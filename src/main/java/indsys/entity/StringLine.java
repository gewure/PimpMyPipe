package indsys.entity;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class StringLine extends GenericToken<Integer, String> {
    public StringLine(Integer lineIndex) {
        super(lineIndex);
    }

    public StringLine(Integer lineIndex, String value) {
        super(lineIndex, value);
    }
}
