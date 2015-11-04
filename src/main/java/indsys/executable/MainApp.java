package indsys.executable;

import indsys.entity.StringLine;
import indsys.entity.Word;
import indsys.filter.*;
import indsys.pipes.OutputConsoleSink;
import indsys.pipes.OutputFileSink;
import thirdparty.pipes.BufferedSyncPipe;

import java.util.List;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class MainApp {
    private static final int BUFFER_SIZE = 8;
    private static final String SOURCE_FILE_PATH = "shortTask.txt";
    private static final String OUTPUT_FILE_PATH = "out.txt";

    public static void main(String[] args) {
        StringToLineFilter stringToLineFilter = new StringToLineFilter(SOURCE_FILE_PATH);

        BufferedSyncPipe<List<Word>> wordPipe = new BufferedSyncPipe<>(BUFFER_SIZE * BUFFER_SIZE);
        BufferedSyncPipe<List<List<Word>>> wordShiftedPipe = new BufferedSyncPipe<>(BUFFER_SIZE);
        BufferedSyncPipe<List<List<Word>>> dictionaryCleanedPipe = new BufferedSyncPipe<>(BUFFER_SIZE);
        BufferedSyncPipe<List<StringLine>> stringLinePipe = new BufferedSyncPipe<>(BUFFER_SIZE);

        OutputFileSink outputFileSink = new OutputFileSink(OUTPUT_FILE_PATH);
        OutputConsoleSink outputConsoleSink = new OutputConsoleSink();



        // 1. Text -> List<Sentences> -> List<Word>
        new Thread(
            new StringToWordFilter(stringToLineFilter, wordPipe)
        ).start();

        // 2. List<Word> -> Shifted (List<Word>)
        new Thread(
            new WordShiftFilter(wordPipe, wordShiftedPipe)
        ).start();

        // 3. Shifted (List<Word>) -> Filtered with Dictionary (List<Word>)
        new Thread(
            new DictionaryFilter(wordShiftedPipe, dictionaryCleanedPipe)
        ).start();

        // 4. Filtered with Dictionary (List<Word>) -> List<StringLine>
        new Thread(
            new WordsListToStringLineList(dictionaryCleanedPipe, stringLinePipe)
        ).start();

        // 5. List<StringLine> -> Sorted alphabetically (List<StringLine>) -> Sink
        new Thread(
            new StringListSortFilter(stringLinePipe, outputFileSink)
        ).start();
    }
}
