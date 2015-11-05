package indsys.pipes;

import indsys.entity.StringLine;
import thirdparty.interfaces.Writable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class OutputFileSink implements Writable<List<StringLine>>{
    private static final String DEFAULT_OUTPUT_FILE_PATH = "out.txt";

    private Path _outputFilePath;
    private BufferedWriter _bw;

    public OutputFileSink() {
        this(DEFAULT_OUTPUT_FILE_PATH);
    }

    public OutputFileSink(String outputFilePath) {

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
    public void write(List<StringLine> sortedList) throws StreamCorruptedException {
        if(_bw != null) {

            try {

                if (sortedList != null) {

                    for (StringLine stringLine : sortedList) {
                        _bw.write(stringLine.getValue() + "\n");
                    }

                    _bw.flush();

                } else {
                    _bw.close();
                    _bw = null;

                    System.out.println("Output file \"" + _outputFilePath.getFileName() + "\" was updated.");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
