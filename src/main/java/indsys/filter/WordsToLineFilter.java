package indsys.filter;

import indsys.entity.StringLine;
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
public class WordsToLineFilter extends DataEnrichmentFilter<List<List<Word>>, List<StringLine>> {
    private static final String DELIMITER = " ";

    public WordsToLineFilter(Readable<List<List<Word>>> input, Writable<List<StringLine>> output)
    throws InvalidParameterException {
        super(input, output);
    }

    @Override
    protected boolean fillEntity(List<List<Word>> wordsLists, List<StringLine> entity) {
        if (wordsLists != null) {

            if (wordsLists.isEmpty()) {
                return true;
            }

            for(List<Word> wordList : wordsLists) {

                int wordListSize = wordList.size();
                int lineNumber = wordList.get(0).getLineIndex();
                StringBuilder sb = new StringBuilder((2 * wordListSize) + 1); //words + delimiters + line number

                for (Word word : wordList) {
                    sb.append(word.getValue());
                    sb.append(DELIMITER);
                }

                sb.append(lineNumber); //adding line number

                entity.add(new StringLine(lineNumber, sb.toString()));
            }

            return true;
        }

        return false;
    }

    @Override
    protected List<StringLine> getNewEntityObject() {
        return new LinkedList<>();
    }
}
