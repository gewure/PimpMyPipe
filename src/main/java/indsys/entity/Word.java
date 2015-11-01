package indsys.entity;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class Word extends GenericToken<Integer, String> {
    public Word(Integer lineIndex){
        super(lineIndex);
    }

    public Word(Integer lineIndex, String value) {
        super(lineIndex, value);
    }
}
