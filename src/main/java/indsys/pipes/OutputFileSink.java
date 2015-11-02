package indsys.pipes;

import indsys.entity.StringLine;
import thirdparty.interfaces.Writable;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class OutputFileSink implements Writable<List<StringLine>>{
    private static final String DEFAULT_OUTPUT_FILE_PATH = "out.txt";
    private final Path _outputFilePath;

    public OutputFileSink() {
        _outputFilePath = Paths.get(DEFAULT_OUTPUT_FILE_PATH);
    }

    public OutputFileSink(String outputFilePath) {
        if(outputFilePath != null) {
            _outputFilePath = Paths.get(outputFilePath);
        } else {
            _outputFilePath = Paths.get(DEFAULT_OUTPUT_FILE_PATH);
        }
    }

    @Override
    public void write(List<StringLine> sortedList) throws StreamCorruptedException {

        try {

            BufferedWriter bw = Files.newBufferedWriter(_outputFilePath);

            if (sortedList != null) {
                for (StringLine stringLine : sortedList) {
                    bw.write(stringLine.getValue());
                }

                bw.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
