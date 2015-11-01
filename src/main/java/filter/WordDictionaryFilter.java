package filter;

import entity.Word;
import entity.WordCounter;
import thirdparty.filter.DataEnrichmentFilter;
import thirdparty.interfaces.Readable;
import thirdparty.interfaces.Writable;

import java.security.InvalidParameterException;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class WordDictionaryFilter extends DataEnrichmentFilter<Word, WordCounter> {

    public WordDictionaryFilter(Readable<Word> input, Writable<WordCounter> output) throws InvalidParameterException {
        super(input, output);
    }

    @Override
    protected boolean fillEntity(Word nextVal, WordCounter entity) {
        return false;
    }

    @Override
    protected WordCounter getNewEntityObject() {
        return null;
    }
}
