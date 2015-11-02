package indsys.pipes;

import indsys.entity.StringLine;
import thirdparty.interfaces.Writable;

import java.io.StreamCorruptedException;
import java.util.List;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class OutputConsoleSink implements Writable<List<StringLine>>{
    public OutputConsoleSink() {
    }

    @Override
    public void write(List<StringLine> sortedList) throws StreamCorruptedException {
        if (sortedList != null) {
            for (StringLine stringLine : sortedList) {
                System.out.println(stringLine.getValue());
            }
        }
    }
}
