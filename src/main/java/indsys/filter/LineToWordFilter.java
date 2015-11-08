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
    private static final String TEXT_SEPARATORS = "[ \r\n\t.,;:'\"()?!\\-_\\]\\[/·\\*]";

    public LineToWordFilter(Readable<StringLine> input, Writable<List<Word>> output)
    throws InvalidParameterException {
        super(input, output);
    }

    @Override
    protected boolean fillEntity(StringLine stringLine, List<Word> entity)  {

        if (stringLine != null && stringLine.getValue() != null) {

            for (String word : stringLine.getValue().split(TEXT_SEPARATORS)) {
                String trimmedWord = word.trim();

                if (!trimmedWord.isEmpty()) {
                    entity.add(new Word(stringLine.getLineIndex(), trimmedWord));
                }
            }

            if (!entity.isEmpty()) return true;
        }

        return false;
    }

    @Override
    protected List<Word> getNewEntityObject() {
        return new LinkedList<>();
    }
}
