package indsys.executable;

import indsys.entity.StringLine;
import indsys.entity.TextAlignment;
import indsys.entity.Word;
import indsys.filter.*;
import indsys.pipes.OutputFileSink;
import thirdparty.pipes.BufferedSyncPipe;

import java.util.List;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class MainApp {
    private static final int BUFFER_SIZE = 8;
    private static final String SOURCE_SHORT_FILE_PATH = "shortText.txt";
    private static final String SOURCE_CHAR_FILE_PATH = "charText.txt";
    private static final String SOURCE_TEXT_FILE_PATH = "shortText.txt";
    private static final String FORMATED_OUTPUT_FILE_PATH = "formated_out.txt";
    private static final String INDEX_OUTPUT_FILE_PATH = "index_out.txt";

    public static void main(String[] args) {
//        runTaskA(SOURCE_TEXT_FILE_PATH);

        runTaskB(SOURCE_SHORT_FILE_PATH);
    }

    private static void runTaskA(String pathToSourceFile) {
        TextToLineFilter textToLineFilter = new TextToLineFilter(pathToSourceFile);

        BufferedSyncPipe<List<Word>> wordPipe = new BufferedSyncPipe<>(BUFFER_SIZE * BUFFER_SIZE);
        BufferedSyncPipe<List<List<Word>>> wordShiftedPipe = new BufferedSyncPipe<>(BUFFER_SIZE);
        BufferedSyncPipe<List<List<Word>>> dictionaryCleanedPipe = new BufferedSyncPipe<>(BUFFER_SIZE);
        BufferedSyncPipe<List<StringLine>> stringLinePipe = new BufferedSyncPipe<>(BUFFER_SIZE);

        OutputFileSink outputFileSink = new OutputFileSink(INDEX_OUTPUT_FILE_PATH);


        // 1. Text -> List<Sentences> -> List<Word>
        new Thread(
            new LineToWordFilter(textToLineFilter, wordPipe)
        ).start();

        // 2. List<Word> -> Shifted (List<Word>)
        new Thread(
            new WordShiftFilter(wordPipe, wordShiftedPipe)
        ).start();

        // 3. Shifted (List<Word>) -> Filtered with Dictionary (List<Word>)
        new Thread(
            new WordDictionaryFilter(wordShiftedPipe, dictionaryCleanedPipe)
        ).start();

        // 4. Filtered with Dictionary (List<Word>) -> List<StringLine>
        new Thread(
            new WordsToLineFilter(dictionaryCleanedPipe, stringLinePipe)
        ).start();

        // 5. List<StringLine> -> Sorted alphabetically (List<StringLine>) -> Sink
        new Thread(
            new LineSortFilter(stringLinePipe, outputFileSink)
        ).start();
    }

    private static void runTaskB(String pathToSourceFile) {
        TextToCharFilter textToCharFilter = new TextToCharFilter(pathToSourceFile);

        BufferedSyncPipe<Word> wordPipe = new BufferedSyncPipe<>(BUFFER_SIZE);
        BufferedSyncPipe<StringLine> stringLinePipe = new BufferedSyncPipe<>(BUFFER_SIZE);

        new Thread(
            new CharToWordBuilderFilter(textToCharFilter, wordPipe)
        ).start();

        new Thread(
            new WordToLineBuilderFilter(wordPipe, stringLinePipe, TextAlignment.NONE, 23)
        ).start();

        //TODO sub pipe which will delegate stream to output sink and line to word filter
//        new Thread(
//            new (textToCharFilter, wordPipe)
//        ).start();



        BufferedSyncPipe<List<Word>> indexWordPipe = new BufferedSyncPipe<>(BUFFER_SIZE * BUFFER_SIZE);
        BufferedSyncPipe<List<List<Word>>> indexWordShiftedPipe = new BufferedSyncPipe<>(BUFFER_SIZE);
        BufferedSyncPipe<List<List<Word>>> indexDictionaryCleanedPipe = new BufferedSyncPipe<>(BUFFER_SIZE);
        BufferedSyncPipe<List<StringLine>> indexStringLinePipe = new BufferedSyncPipe<>(BUFFER_SIZE);

        OutputFileSink indexOutputFileSink = new OutputFileSink(INDEX_OUTPUT_FILE_PATH);


        // 1. Text -> List<Sentences> -> List<Word>
        new Thread(
            new LineToWordFilter(stringLinePipe, indexWordPipe)
        ).start();

        // 2. List<Word> -> Shifted (List<Word>)
        new Thread(
            new WordShiftFilter(indexWordPipe, indexWordShiftedPipe)
        ).start();

        // 3. Shifted (List<Word>) -> Filtered with Dictionary (List<Word>)
        new Thread(
            new WordDictionaryFilter(indexWordShiftedPipe, indexDictionaryCleanedPipe)
        ).start();

        // 4. Filtered with Dictionary (List<Word>) -> List<StringLine>
        new Thread(
            new WordsToLineFilter(indexDictionaryCleanedPipe, indexStringLinePipe)
        ).start();

        // 5. List<StringLine> -> Sorted alphabetically (List<StringLine>) -> Sink
        new Thread(
            new LineSortFilter(indexStringLinePipe, indexOutputFileSink)
        ).start();
    }
}
