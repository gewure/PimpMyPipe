package indsys.pipes;

import indsys.entity.StringLine;
import thirdparty.interfaces.Readable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TextLineSupplierPipe implements Readable<StringLine> {
    private static final String DEFAULT_SOURCE_FILE_PATH = "in.txt";

    private BufferedReader _br;

    public TextLineSupplierPipe() {
        this(DEFAULT_SOURCE_FILE_PATH);
    }

    public TextLineSupplierPipe(String sourceFilePath) {
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
                    StringLine stringLine = new StringLine();
                    stringLine.setValue(line);
                    return stringLine;
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
