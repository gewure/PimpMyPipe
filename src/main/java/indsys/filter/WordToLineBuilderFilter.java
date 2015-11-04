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

    private int lineIndex = -1;
    private StringBuilder _sb;

    public WordToLineBuilderFilter(Readable<Word> input, Writable<StringLine> output, TextAlignment type, int lineLength)
    throws InvalidParameterException {
        super(input, output);

        _lineLength = lineLength;
        _alignment = type;
    }

    @Override
    protected boolean fillEntity(Word word, StringLine entity) {

        if (word == null && _sb == null) {
            //nothing to do here
            return true;
        }

        //pushing buffered words
        if (_sb != null && _sb.length() > _lineLength){
            //setting buffered word as a value of the entity (preparing for push)
            setEntityValues(entity, _sb.toString());

            //buffering new word
            if (word != null) bufferWord(word.getValue());

            return true;
        }

        //normal approach
        if (word != null) {

            if (word.getValue() == null) {
                return true;
            }

            int wordLength = word.getValue().length();

            //if word is bigger than line length simply push it
            if (wordLength > _lineLength) {
                //just append one word (line should contain at least one word)
                //setting value of the entity (preparing for push)
                setEntityValues(entity, word.getValue());

                return true;
            } else {

                if (_sb == null) {
                    _sb = new StringBuilder(_lineLength);
                }

                int producedLineLength = _sb.length() + DELIMITER.length() + wordLength;

                if (producedLineLength <= _lineLength) {
                    //produced line length is acceptable append word to buffer
                    appendWord(word.getValue());

                } else {
                    //produced line length is not acceptable pushing buffer and buffering current (new) word

                    int difference = producedLineLength - _lineLength;

                    //formatting and writing old buffered value, preparing entity values for push
                    setEntityValues(
                        entity,
                        setAlignment(difference, _alignment, _sb.toString())
                    );

                    //buffering current (new) word
                    bufferWord(word.getValue());

                    return true;
                }

                //probably we can add one more word in the next invocation
                return false;
            }
        } else if (_sb != null && _sb.length() > 0){
            //ok word is null, probably we can write buffered word
            //setting buffered word as a value of the entity (preparing for push)
            setEntityValues(entity, _sb.toString());

            return true;
        }


        return false;
    }

    @Override
    protected StringLine getNewEntityObject() {
        return new StringLine();
    }

    private void setEntityValues(StringLine entity, String value) {
        entity.setLineIndex(++lineIndex);
        entity.setValue(value);
    }

    private void appendWord(String word) {
        if (_sb == null) {
            _sb = new StringBuilder(_lineLength);
        }

        if(_sb.length() > 0) {
            //add space if it will be not a first append
            _sb.append(DELIMITER);
        }

        _sb.append(word);
    }

    private void bufferWord(String word) {
        //old buffer removed;
        _sb = null;

        //buffering new word
        appendWord(word);
    }

    private String setAlignment(int difference, TextAlignment alignment, String s) {
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
        return length != 0 ? String.format("%0" + length + "d", 0).replaceAll("0", " ") : "";
    }
}