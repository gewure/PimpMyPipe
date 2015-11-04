package indsys.filter;

import indsys.entity.Word;
import thirdparty.filter.DataEnrichmentFilter;
import thirdparty.interfaces.Readable;
import thirdparty.interfaces.Writable;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class WordShiftFilter extends DataEnrichmentFilter<List<Word>, List<List<Word>>> {

    public WordShiftFilter(Readable<List<Word>> input, Writable<List<List<Word>>> output)
    throws InvalidParameterException {
        super(input, output);
    }

    @Override
    protected boolean fillEntity(List<Word> wordList, List<List<Word>> entity) {
        if (wordList != null) {
            LinkedList<Word> tempList = (LinkedList<Word>) wordList;

            if (tempList.isEmpty()) {
                return true;
            }

            //saving one iteration
            entity.add(cloneList(tempList));

            //shifting iterations
            for (int shift = 0; shift < tempList.size() - 1; ++shift) {
                Word tempWord = tempList.removeFirst();
                tempList.addLast(tempWord);

                entity.add(cloneList(tempList));
            }

            return true;
        }

        return false;
    }

    @Override
    protected List<List<Word>> getNewEntityObject() {
        return new LinkedList<>();
    }

    private List<Word> cloneList(List<Word> list) {
        return new LinkedList<>(list);
    }
}
