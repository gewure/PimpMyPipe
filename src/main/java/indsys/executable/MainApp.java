package indsys.executable;

import indsys.entity.StringLine;
import indsys.entity.WordList;
import indsys.filter.*;
import indsys.pipes.OutputFileSink;
import indsys.entity.Word;
import thirdparty.pipes.BufferedSyncPipe;

import java.util.List;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class MainApp {
    private static final int MAX_BUFFER = Integer.MAX_VALUE;
    //Should be changed manually
    private static final String SOURCE_FILE_PATH = "C:\\Users\\sereGkaluv\\IdeaProjects\\PimpMyPipe\\src\\main\\resources\\aliceInWonderland.txt";
    private static final String OUTPUT_FILE_PATH = "out.txt";

    public static void main(String[] args) {
        StringToLineFilter stringToLineFilter = new StringToLineFilter(SOURCE_FILE_PATH);
        OutputFileSink outputFileSink = new OutputFileSink(OUTPUT_FILE_PATH);

        BufferedSyncPipe<List<Word>> wordPipe = new BufferedSyncPipe<>(64);
        BufferedSyncPipe<List<WordList>> wordShiftedPipe = new BufferedSyncPipe<>(4);
        BufferedSyncPipe<List<StringLine>> stringLinePipe = new BufferedSyncPipe<>(4);

        new Thread(
            new StringToWordFilter(stringToLineFilter, wordPipe)
        ).start();

        new Thread(
            new WordShiftFilter(wordPipe, wordShiftedPipe)
        ).start();

        new Thread(
            new WordsListToStringLineList(wordShiftedPipe, stringLinePipe)
        ).start();

        new Thread(
            new StringListSortFilter(stringLinePipe, outputFileSink)
        ).start();
    }
}
