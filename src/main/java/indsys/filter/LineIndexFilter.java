package indsys.filter;

import indsys.entity.StringLine;
import indsys.pipes.TextLineSupplierPipe;
import thirdparty.filter.DataTransformationFilter;
import thirdparty.interfaces.Readable;
import thirdparty.interfaces.Writable;

import java.security.InvalidParameterException;


/**
 * Created by sereGkaluv on 04-Nov-15.
 */
public class LineIndexFilter extends DataTransformationFilter<StringLine> {
    private int lineIndex = -1;

    public LineIndexFilter(Readable<StringLine> input, Writable<StringLine> output)
    throws InvalidParameterException {
        //Pull strategy
        super(input, output);
    }

    public LineIndexFilter(String sourceFilePath, Writable<StringLine> output) {
        //Push strategy
        super(new TextLineSupplierPipe(sourceFilePath), output);
    }

    @Override
    protected void process(StringLine entity) {
        entity.setLineIndex(++lineIndex);
    }
}
