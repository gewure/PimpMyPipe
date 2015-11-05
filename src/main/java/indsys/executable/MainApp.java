package indsys.executable;

import indsys.entity.Char;
import indsys.entity.StringLine;
import indsys.entity.TextAlignment;
import indsys.entity.Word;
import indsys.filter.*;
import indsys.pipes.OutputFileSink;
import thirdparty.interfaces.Readable;
import thirdparty.interfaces.Writable;
import thirdparty.pipes.BufferedSyncPipe;

import java.util.List;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class MainApp {
    private static final int BUFFER_SIZE = 8;
    private static final String SOURCE_CHAR_FILE_PATH = "charText.txt";
    private static final String SOURCE_TEXT_FILE_PATH = "aliceInWonderland.txt";
    private static final String SOURCE_SHORT_FILE_PATH = "shortText.txt";
    private static final String FORMATTED_OUTPUT_FILE_PATH = "formatted_out.txt";
    private static final String INDEX_OUTPUT_FILE_PATH = "index_out.txt";

    public static void main(String[] args) {
        /* TASK A */
        TextToLineFilter textToLineFilter = new TextToLineFilter(SOURCE_SHORT_FILE_PATH);
        OutputFileSink aOutputFileSink = new OutputFileSink(INDEX_OUTPUT_FILE_PATH);

        runTaskA(textToLineFilter, aOutputFileSink);


        /* TASK B */
        TextToCharFilter textToCharFilter = new TextToCharFilter(SOURCE_CHAR_FILE_PATH);
        OutputFileSink bOutputFileSink = new OutputFileSink(INDEX_OUTPUT_FILE_PATH);

        runTaskB(textToCharFilter, bOutputFileSink, TextAlignment.NONE, 30);
    }

    private static void runTaskA(Readable<StringLine> sourceStream, Writable<List<StringLine>> outputSink) {

        BufferedSyncPipe<List<Word>> wordPipe = new BufferedSyncPipe<>(BUFFER_SIZE * BUFFER_SIZE);
        BufferedSyncPipe<List<List<Word>>> wordShiftedPipe = new BufferedSyncPipe<>(BUFFER_SIZE);
        BufferedSyncPipe<List<List<Word>>> dictionaryCleanedPipe = new BufferedSyncPipe<>(BUFFER_SIZE);
        BufferedSyncPipe<List<StringLine>> stringLinePipe = new BufferedSyncPipe<>(BUFFER_SIZE);

        // 1. sourceStream -> List<StringLine> -> List<Word>
        new Thread(
            new LineToWordFilter(sourceStream, wordPipe)
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

        // 5. List<StringLine> -> Sorted alphabetically (List<StringLine>) -> outputSink
        new Thread(
            new LineSortFilter(stringLinePipe, outputSink)
        ).start();
    }

    private static void runTaskB(
        Readable<Char> sourceStream,
        Writable<List<StringLine>> indexOutputSink,
        TextAlignment textAlignment,
        int lineLength
    ) {

        BufferedSyncPipe<Word> wordPipe = new BufferedSyncPipe<>(BUFFER_SIZE);
        BufferedSyncPipe<StringLine> stringLinePipe = new BufferedSyncPipe<>(BUFFER_SIZE);
        BufferedSyncPipe<StringLine> secondStreamLinePipe = new BufferedSyncPipe<>(BUFFER_SIZE);

        // 1. sourceStream -> Char -> Word
        new Thread(
            new CharToWordBuilderFilter(sourceStream, wordPipe)
        ).start();

        // 2. Word -> StringLine
        new Thread(
            new WordToLineBuilderFilter(wordPipe, stringLinePipe, textAlignment, lineLength)
        ).start();

        // 3. StringLine
        //     ├──> Output File
        //     │
        //     └──> Delegation to Task A (indexing)
        new Thread(
            new WritingSplitFilter(stringLinePipe, secondStreamLinePipe, FORMATTED_OUTPUT_FILE_PATH)
        ).start();

        // 4. Delegation to Task A (indexing)
        runTaskA(secondStreamLinePipe, indexOutputSink);
    }
}
