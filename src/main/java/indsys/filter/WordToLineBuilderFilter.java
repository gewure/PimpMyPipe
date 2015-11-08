package indsys.filter;

import indsys.entity.StringLine;
import indsys.entity.TextAlignment;
import indsys.entity.Word;
import thirdparty.filter.DataEnrichmentFilter;
import thirdparty.interfaces.Readable;
import thirdparty.interfaces.Writable;

import java.security.InvalidParameterException;

/**
 * Created by sereGkaluv on 04-Nov-15.
 */
public class WordToLineBuilderFilter extends DataEnrichmentFilter<Word, StringLine> {
    private static final String DELIMITER = " ";

    private final int _lineLength;
    private final TextAlignment _alignment;

    private StringBuilder _sb;

    public WordToLineBuilderFilter(Readable<Word> input, Writable<StringLine> output, TextAlignment type, int lineLength)
    throws InvalidParameterException {
        super(input, output);

        _lineLength = lineLength;
        _alignment = type;
    }

    @Override
    protected boolean fillEntity(Word word, StringLine entity) {
        if (isBufferEmpty() && isWordEmpty(word)) return true; //nothing to do here

        if (isBufferEmpty() && !isWordEmpty(word)) {
            createNewBuffer(word);
            return false;
        }

        if (!isBufferEmpty() && !isWordEmpty(word)) {

            if (isBufferFull() || !isEnoughBufferSpaceForWord(word)) {

                flushBuffer(entity);
                createNewBuffer(word);
                return true;

            } else {
                appendWordToBuffer(word);
                return false;
            }
        }

        if (!isBufferEmpty() && isWordEmpty(word)) {
            flushBuffer(entity);
            return true;
        }

        return false;
    }

    @Override
    protected StringLine getNewEntityObject() {
        return new StringLine();
    }

    private boolean isBufferFull() {
        return _sb == null || _sb.length() >= _lineLength;
    }

    private boolean isBufferEmpty() {
        return _sb == null || _sb.length() == 0;
    }

    private boolean isEnoughBufferSpaceForWord(Word word) {
        return (_sb.length() + DELIMITER.length() + word.getValue().length()) <= _lineLength;
    }

    private boolean isWordEmpty(Word word) {
        return word == null || word.getValue() == null || word.getValue().isEmpty();
    }

    private void createNewBuffer(Word word) {
        //old buffer removed;
        _sb = new StringBuilder(_lineLength);

        //buffering new word
        appendWordToBuffer(word);
    }

    private void appendWordToBuffer(Word word) {
        if(_sb.length() > 0) {
            //add space if it will be not a first append
            _sb.append(DELIMITER);
        }

        _sb.append(word.getValue());
    }

    private void flushBuffer(StringLine entity) {
        int difference = _lineLength - _sb.length();
        String line = applyAlignment(difference, _alignment, _sb.toString());

        entity.setValue(line);
    }

    private String applyAlignment(int difference, TextAlignment alignment, String s) {
        if (difference <= 0) return s;

        switch (alignment) {
            case LEFT: {
                return s + getPaddingString(difference);
            }

            case CENTERED: {
                String tempPadding = getPaddingString(difference / 2);
                return tempPadding + s + tempPadding;
            }

            case RIGHT: {
                return getPaddingString(difference) + s;
            }

            default: return s;
        }
    }

    private String getPaddingString(int length) {
        return length != 0 ? new String(new char[length]).replace('\0', ' ') : "";
    }

}