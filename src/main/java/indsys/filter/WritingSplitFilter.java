package indsys.filter;

import indsys.entity.StringLine;
import thirdparty.filter.DataTransformationFilter;
import thirdparty.interfaces.Readable;
import thirdparty.interfaces.Writable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;

/**
 * Created by sereGkaluv on 05-Nov-15.
 */
public class WritingSplitFilter extends DataTransformationFilter<StringLine> {
    private static final String DEFAULT_OUTPUT_FILE_PATH = "split_out.txt";

    private Path _outputFilePath;
    private BufferedWriter _bw;

    public WritingSplitFilter(Readable<StringLine> input, Writable<StringLine> output)
    throws InvalidParameterException {
        super(input, output);

        _outputFilePath = Paths.get(DEFAULT_OUTPUT_FILE_PATH);

        try {
            _bw = Files.newBufferedWriter(_outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public WritingSplitFilter(Readable<StringLine> input, Writable<StringLine> output, String outputFilePath)
    throws InvalidParameterException {
        super(input, output);

        if(outputFilePath != null) {
            _outputFilePath = Paths.get(outputFilePath);
        } else {
            _outputFilePath = Paths.get(DEFAULT_OUTPUT_FILE_PATH);
        }

        try {
            _bw = Files.newBufferedWriter(_outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void process(StringLine stringLine) {
        try {
            if (_bw != null) {
                if (stringLine != null && stringLine.getValue() != null) {

                    _bw.write(stringLine.getValue() + "\n");
                    _bw.flush();

                } else {
                    _bw.close();
                    _bw = null;

                    System.out.println("Output file \"" + _outputFilePath.getFileName() + "\" was updated.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
