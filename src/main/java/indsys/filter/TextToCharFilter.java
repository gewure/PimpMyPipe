package indsys.filter;

import indsys.entity.Char;
import thirdparty.interfaces.Readable;

import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by sereGkaluv on 04-Nov-15.
 */
public class TextToCharFilter implements Readable<Char> {
    private static final String DEFAULT_SOURCE_FILE_PATH = "in.txt";

    private InputStream _is;
    private static int charIndex = -1;

    public TextToCharFilter() {
        this(DEFAULT_SOURCE_FILE_PATH);
    }

    public TextToCharFilter(String sourceFilePath) {
        Path _sourceFilePath;

        if (sourceFilePath != null) {
            _sourceFilePath = Paths.get(sourceFilePath);
        } else {
            _sourceFilePath = Paths.get(DEFAULT_SOURCE_FILE_PATH);
        }

        try {
            _is = Files.newInputStream(_sourceFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Char read() throws StreamCorruptedException {
        if (_is != null) {

            try {

                int r = _is.read();
                if (r != -1) {
                    return new Char(++charIndex, (char) r);
                } else {
                    _is.close();
                    _is = null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
