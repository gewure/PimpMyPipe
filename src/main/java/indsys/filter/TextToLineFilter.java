package indsys.filter;

import indsys.entity.StringLine;
import thirdparty.interfaces.Readable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextToLineFilter implements Readable<StringLine> {
    private static final String DEFAULT_SOURCE_FILE_PATH = "in.txt";

    private BufferedReader _br;
    private static int lineIndex = -1;

    public TextToLineFilter() {
        this(DEFAULT_SOURCE_FILE_PATH);
    }

    public TextToLineFilter(String sourceFilePath) {
        Path _sourceFilePath;

        if(sourceFilePath != null) {
            _sourceFilePath = Paths.get(sourceFilePath);
        } else {
            _sourceFilePath = Paths.get(DEFAULT_SOURCE_FILE_PATH);
        }

        try {
            _br = Files.newBufferedReader(_sourceFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	@Override
	public StringLine read() throws StreamCorruptedException {
        if (_br != null) {
            try {

                String line = _br.readLine();
                if (line != null) {
                    return new StringLine(++lineIndex, line);
                } else {
                    _br.close();
                    _br = null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
