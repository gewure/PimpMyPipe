package filter;

import entity.Token;
import entity.Word;
import thirdparty.filter.DataEnrichmentFilter;
import thirdparty.interfaces.Readable;
import thirdparty.interfaces.Writable;

import java.security.InvalidParameterException;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class StringToWordFilter extends DataEnrichmentFilter<Token, Word> {
    private static final String TEXT_SEPARATORS = "[ \\r\\n\\t.,;:'\"()?!]";

    public StringToWordFilter(Readable<Token> input, Writable<Word> output) throws InvalidParameterException {
        super(input, output);
    }

    @Override
    protected boolean fillEntity(Token nextVal, Word entity) {
        String strToken = nextVal.getValue();

        if (strToken != null && strToken.split(TEXT_SEPARATORS).length == 0) {
            return true;
        }

        entity.appendToken(nextVal);
        return false;
    }

    @Override
    protected Word getNewEntityObject() {
        return new Word();
    }
}
