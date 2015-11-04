package indsys.filter;

import indsys.entity.StringLine;
import thirdparty.filter.DataTransformationFilter;
import thirdparty.interfaces.Readable;
import thirdparty.interfaces.Writable;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class LineSortFilter extends DataTransformationFilter<List<StringLine>> {

    public LineSortFilter(Readable<List<StringLine>> input, Writable<List<StringLine>> output)
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
