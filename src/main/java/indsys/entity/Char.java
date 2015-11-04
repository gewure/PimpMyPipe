package indsys.entity;

/**
 * Created by sereGkaluv on 04-Nov-15.
 */
public class Char extends GenericToken<Integer, Character> {
    public Char(Integer lineIndex){
        super(lineIndex);
    }

    public Char(Integer lineIndex, Character value) {
        super(lineIndex, value);
    }
}
