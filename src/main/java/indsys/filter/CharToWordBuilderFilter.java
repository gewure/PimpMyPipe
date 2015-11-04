package indsys.filter;

import indsys.entity.Char;
import indsys.entity.Word;
import thirdparty.filter.DataEnrichmentFilter;
import thirdparty.interfaces.Readable;
import thirdparty.interfaces.Writable;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * Created by sereGkaluv on 04-Nov-15.
 */
public class CharToWordBuilderFilter extends DataEnrichmentFilter<Char, Word> {
    private static final String TEXT_SEPARATORS = "[ \\r\\n\\t.,;:'\"()?!]";
    private static final Set<Character> SEPARATORS_SET = new HashSet<>();

    private Character _lastCharacter;
    private StringBuilder _sb;

    public CharToWordBuilderFilter(Readable<Char> input, Writable<Word> output)
    throws InvalidParameterException {
        super(input, output);

        prepareSeparatorsSet(TEXT_SEPARATORS);
    }

    @Override
    protected boolean fillEntity(Char character, Word entity) {
        if (character != null) {

            Character ch = character.getValue();

            if (ch != null && !createsSequenceOfSeparators(ch)) {

                if (_sb == null) {
                    _sb = new StringBuilder();
                }

                _sb.append(ch);

                if (SEPARATORS_SET.contains(ch)) {
                    entity.setValue(_sb.toString().trim());

                    _sb = null;
                    return true;
                }

                return false;

            } else if (_sb != null) {
                entity.setValue(_sb.toString());

                _sb = null;
                return true;
            }
        }

        return false;
    }

    private boolean createsSequenceOfSeparators(Character ch) {
        if (_lastCharacter != null && SEPARATORS_SET.contains(_lastCharacter)) {
            //probably a sequence

            _lastCharacter = ch;
            return SEPARATORS_SET.contains(ch);
        }

        _lastCharacter = ch;
        return false;
    }

    @Override
    protected Word getNewEntityObject() {
        return new Word();
    }

    private static void prepareSeparatorsSet(String separators) {
        separators.chars().forEach(c -> SEPARATORS_SET.add((char) c));
    }
}
