package indsys.entity;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class WordList extends GenericToken<Integer, List<Word>> {
    public WordList() {
        super();
    }

    public WordList(Integer lineIndex){
        super(lineIndex);
    }

    public WordList(Integer lineIndex, List<Word> words) {
        super(lineIndex, words);
    }

    public WordList(Integer lineIndex, Word... words) {
        super(lineIndex, Arrays.asList(words));
    }
}
