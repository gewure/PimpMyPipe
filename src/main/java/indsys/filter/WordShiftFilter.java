package indsys.filter;

import indsys.entity.Word;
import indsys.entity.WordList;
import thirdparty.filter.DataEnrichmentFilter;
import thirdparty.interfaces.Readable;
import thirdparty.interfaces.Writable;

import java.io.StreamCorruptedException;
import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class WordShiftFilter extends DataEnrichmentFilter<WordList, List<WordList>> {

    public WordShiftFilter(Readable<WordList> input, Writable<List<WordList>> output)
    throws InvalidParameterException {
        super(input, output);
    }

    @Override
    protected boolean fillEntity(WordList wordList, List<WordList> entity) {
        if (wordList != null) {
            int id = wordList.getId();
            LinkedList<Word> tempList = new LinkedList<>(wordList.getValue());

            if (tempList.isEmpty()) {
                return true;
            }

            entity.add(wordList);

            int shiftSteps = tempList.size() - 1;
            for (int shift = 0; shift < shiftSteps; ++shift) {
                Word tempWord = tempList.removeFirst();
                tempList.addLast(tempWord);

                entity.add(listToWordList(id, tempList));
            }

            try {
                sendEndSignal();
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    protected List<WordList> getNewEntityObject() {
        return new LinkedList<>();
    }

    private WordList listToWordList(int id, List<Word> list) {
        return new WordList(id, new LinkedList<>(list));
    }
}
