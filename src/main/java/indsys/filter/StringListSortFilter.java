package indsys.filter;

import indsys.entity.StringLine;
import indsys.entity.Word;
import thirdparty.filter.DataEnrichmentFilter;
import thirdparty.filter.DataTransformationFilter;
import thirdparty.interfaces.Readable;
import thirdparty.interfaces.Writable;

import java.security.InvalidParameterException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class StringListSortFilter extends DataTransformationFilter<List<StringLine>> {

    public StringListSortFilter(Readable<List<StringLine>> input, Writable<List<StringLine>> output)
    throws InvalidParameterException {
        super(input, output);
    }

    @Override
    protected void process(List<StringLine> stringLineList) {
        if (stringLineList != null && !stringLineList.isEmpty()) {
            stringLineList.sort(
                (l1, l2) -> String.CASE_INSENSITIVE_ORDER.compare(l1.getValue(), l2.getValue())
            );
        }
    }
}
