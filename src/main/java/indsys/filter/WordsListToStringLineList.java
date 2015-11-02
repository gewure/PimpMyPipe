package indsys.filter;

import indsys.entity.StringLine;
import indsys.entity.Word;
import indsys.entity.WordList;
import thirdparty.filter.DataEnrichmentFilter;
import thirdparty.interfaces.Readable;
import thirdparty.interfaces.Writable;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class WordsListToStringLineList extends DataEnrichmentFilter<List<WordList>, List<StringLine>> {
    private static final String DELIMITER = " ";

    public WordsListToStringLineList(Readable<List<WordList>> input, Writable<List<StringLine>> output)
    throws InvalidParameterException {
        super(input, output);
    }

    @Override
    protected boolean fillEntity(List<WordList> wordsList, List<StringLine> entity) {
        if (wordsList != null) {

            if (wordsList.isEmpty()) {
                return true;
            }

            //FIX ME: Out of memory error;
            for(WordList words : wordsList) {

                int wordsListSize = words.getValue().size();
                StringBuilder sb = new StringBuilder((2 * wordsListSize) - 1);

                for (Word word : words.getValue()) {
                    sb.append(word.getValue());
                    sb.append(DELIMITER);
                }

                sb.append(words.getId()); //adding line number

                entity.add(new StringLine(words.getId(), sb.toString()));
            }
        }

        return false;
    }

    @Override
    protected List<StringLine> getNewEntityObject() {
        return new LinkedList<>();
    }
}
