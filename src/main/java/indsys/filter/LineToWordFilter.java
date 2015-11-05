package indsys.filter;

import indsys.entity.StringLine;
import indsys.entity.Word;
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
public class LineToWordFilter extends DataEnrichmentFilter<StringLine, List<Word>> {
    private static final String TEXT_SEPARATORS = "[ \r\n\t.,;:'\"()?!]";

    public LineToWordFilter(Readable<StringLine> input, Writable<List<Word>> output)
    throws InvalidParameterException {
        super(input, output);
    }

    @Override
    protected boolean fillEntity(StringLine stringLine, List<Word> entity)  {

        if (stringLine != null && stringLine.getValue() != null) {
            List<Word> words = Stream.of(stringLine.getValue().split(TEXT_SEPARATORS))
                .map(word -> new Word(stringLine.getLineIndex(), word))
                .collect(Collectors.toList());

            if(words.isEmpty()) {
                return true;
            }

            entity.addAll(words);
            return true;
        }

        return false;
    }

    @Override
    protected List<Word> getNewEntityObject() {
        return new LinkedList<>();
    }
}
