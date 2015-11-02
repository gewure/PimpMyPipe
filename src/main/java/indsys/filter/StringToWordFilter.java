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
import java.util.stream.Stream;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class StringToWordFilter extends DataEnrichmentFilter<StringLine, WordList> {
    private static final String TEXT_SEPARATORS = "[ \\r\\n\\t.,;:'\"()?!]";

    public StringToWordFilter(Readable<StringLine> input, Writable<WordList> output)
    throws InvalidParameterException {
        super(input, output);
    }

    @Override
    protected boolean fillEntity(StringLine stringLine, WordList entity) {
        if (stringLine.getValue() != null) {
            List<Word> words = Stream.of(stringLine.getValue().split(TEXT_SEPARATORS))
                .map(word -> new Word(stringLine.getId(), word))
                .collect(Collectors.toList());

            if(words.isEmpty()) {
                return true;
            }

            entity.setId(stringLine.getId());
            entity.setValue(words);
        }

        return false;
    }

    @Override
    protected WordList getNewEntityObject() {
        return new WordList();
    }
}
