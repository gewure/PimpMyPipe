package indsys.executable;

import indsys.entity.StringLine;
import indsys.entity.WordList;
import indsys.filter.*;
import indsys.pipes.OutputConsoleSink;
import indsys.pipes.OutputFileSink;
import thirdparty.pipes.BufferedSyncPipe;

import java.util.List;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class MainApp {
    private static final String SOURCE_FILE_PATH = "shortTask.txt";
    private static final String OUTPUT_FILE_PATH = "out.txt";

    public static void main(String[] args) {
        StringToLineFilter stringToLineFilter = new StringToLineFilter(SOURCE_FILE_PATH);
        OutputFileSink outputFileSink = new OutputFileSink(OUTPUT_FILE_PATH);
        OutputConsoleSink outputConsoleSink = new OutputConsoleSink();

        BufferedSyncPipe<WordList> wordPipe = new BufferedSyncPipe<>(64);
        BufferedSyncPipe<List<WordList>> wordShiftedPipe = new BufferedSyncPipe<>(4);
        BufferedSyncPipe<List<WordList>> dictionaryCleanedPipe = new BufferedSyncPipe<>(4);
        BufferedSyncPipe<List<StringLine>> stringLinePipe = new BufferedSyncPipe<>(4);

        // 1. Text -> List<Sentences>
        new Thread(
            new StringToWordFilter(stringToLineFilter, wordPipe)
        ).start();

        // 2. List<Sentence> -> shifted(List<Sentence>
        new Thread(
            new WordShiftFilter(wordPipe, wordShiftedPipe)
        ).start();

        // 3. shifted(List<Sentence>) -> firstWordCleanedByDictionary(List<Sentence>)
        new Thread(new DictionaryFilter(wordShiftedPipe, dictionaryCleanedPipe)
        ).start();

        // 4. firstWordCleanedByDictionary(List<Sentence>) -> List<Strings>
        new Thread(
            new WordsListToStringLineList(dictionaryCleanedPipe, stringLinePipe)
        ).start();

        // 5. List<Strings> -> sorted(List<Strings>)
        new Thread(
            new StringListSortFilter(stringLinePipe, outputConsoleSink)
        ).start();
    }
}
