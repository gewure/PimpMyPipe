package indsys.executable;

import indsys.entity.Char;
import indsys.entity.StringLine;
import indsys.entity.TextAlignment;
import indsys.entity.Word;
import indsys.filter.*;
import indsys.pipes.GenericSplitPipe;
import indsys.pipes.StringLineFileSink;
import indsys.pipes.StringLinesFileSink;
import indsys.pipes.TextCharSupplierPipe;
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
        StringLinesFileSink aOutputFileSink = new StringLinesFileSink(INDEX_OUTPUT_FILE_PATH);

        runTaskAPush(SOURCE_SHORT_FILE_PATH, aOutputFileSink);


        /* TASK B */
        TextCharSupplierPipe textCharSupplierPipe = new TextCharSupplierPipe(SOURCE_CHAR_FILE_PATH);
        StringLineFileSink bFormattedOutputFileSink = new StringLineFileSink(FORMATTED_OUTPUT_FILE_PATH);
        StringLinesFileSink bIndexOutputFileSink = new StringLinesFileSink(INDEX_OUTPUT_FILE_PATH);

        runTaskB(textCharSupplierPipe, bFormattedOutputFileSink, bIndexOutputFileSink, TextAlignment.CENTERED, 30);
    }

    private static void runTaskAPull(Readable<StringLine> input, Writable<List<StringLine>> outputSink) {

        BufferedSyncPipe<StringLine> stringLinePipe = new BufferedSyncPipe<>(BUFFER_SIZE);

        // 1. String (PULL) -> StringLine
        new Thread(
            new LineIndexFilter(input, stringLinePipe)
        ).start();

        runTaskA(stringLinePipe, outputSink);
    }

    private static void runTaskAPush(String sourceFilePath, Writable<List<StringLine>> outputSink) {

        BufferedSyncPipe<StringLine> stringLinePipe = new BufferedSyncPipe<>(BUFFER_SIZE);

        // 1. Source (PUSH) -> StringLine
        new Thread(
                new LineIndexFilter(sourceFilePath, stringLinePipe)
        ).start();

        runTaskA(stringLinePipe, outputSink);
    }

    private static void runTaskA(Readable<StringLine> input, Writable<List<StringLine>> outputSink) {

        BufferedSyncPipe<List<Word>> wordPipe = new BufferedSyncPipe<>(BUFFER_SIZE * BUFFER_SIZE);
        BufferedSyncPipe<List<List<Word>>> wordShiftedPipe = new BufferedSyncPipe<>(BUFFER_SIZE);
        BufferedSyncPipe<List<List<Word>>> dictionaryCleanedPipe = new BufferedSyncPipe<>(BUFFER_SIZE);
        BufferedSyncPipe<List<StringLine>> sortedLinePipe = new BufferedSyncPipe<>(BUFFER_SIZE);

        // 2. StringLine -> List<Word>
        new Thread(
            new LineToWordFilter(input, wordPipe)
        ).start();

        // 3. List<Word> -> Shifted (List<Word>)
        new Thread(
            new WordShiftFilter(wordPipe, wordShiftedPipe)
        ).start();

        // 4. Shifted (List<Word>) -> Filtered with Dictionary (List<Word>)
        new Thread(
            new WordDictionaryFilter(wordShiftedPipe, dictionaryCleanedPipe)
        ).start();

        // 5. Filtered with Dictionary (List<Word>) -> List<StringLine>
        new Thread(
            new WordsToLinesFilter(dictionaryCleanedPipe, sortedLinePipe)
        ).start();

        // 6. List<StringLine> -> Sorted alphabetically (List<StringLine>) -> outputSink
        new Thread(
            new LineSortFilter(sortedLinePipe, outputSink)
        ).start();
    }

    private static void runTaskB(
        Readable<Char> charPipe,
        Writable<StringLine> formattedOutputSink,
        Writable<List<StringLine>> indexOutputSink,
        TextAlignment textAlignment,
        int lineLength
    ) {

        BufferedSyncPipe<Word> wordPipe = new BufferedSyncPipe<>(BUFFER_SIZE);
        BufferedSyncPipe<StringLine> splitPipe = new GenericSplitPipe<>(BUFFER_SIZE, formattedOutputSink);

        // 1. Source (PULL) Char -> Word
        new Thread(
            new CharToWordBuilderFilter(charPipe, wordPipe)
        ).start();

        // 2. Word -> StringLine
        new Thread(
            new WordToLineBuilderFilter(wordPipe, splitPipe, textAlignment, lineLength)
        ).start();

        // 3. StringLine (split pipe)
        //     ├──> 4.1. Output File Sink
        //     │
        //     └──> 4.2. StringLine -> Delegation to Task A (indexing) (PULL)
        //
        //
        // 4. Delegation to Task A (indexing) (PULL)
        runTaskAPull(splitPipe, indexOutputSink);
    }
}
