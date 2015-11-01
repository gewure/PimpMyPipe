package indsys.filter;

import indsys.entity.Word;
import indsys.entity.WordList;
import thirdparty.filter.DataEnrichmentFilter;
import thirdparty.interfaces.Readable;
import thirdparty.interfaces.Writable;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class WordShiftFilter extends DataEnrichmentFilter<List<Word>, List<WordList>> {

    public WordShiftFilter(Readable<List<Word>> input, Writable<List<WordList>> output)
    throws InvalidParameterException {
        super(input, output);
    }

    @Override
    protected boolean fillEntity(List<Word> wordList, List<WordList> entity) {
        if (wordList != null) {

            if (wordList.isEmpty()) {
                return true;
            }

            LinkedList<Word> tempList = new LinkedList<>(wordList);
            for (int shift = 0; shift <= wordList.size(); ++shift) {
                Word tempWord = tempList.removeFirst();
                tempList.addLast(tempWord);

                entity.add(new WordList(tempWord.getId(), tempList));
            }
        }

        return false;
    }

    @Override
    protected List<WordList> getNewEntityObject() {
        return new LinkedList<>();
    }
}
