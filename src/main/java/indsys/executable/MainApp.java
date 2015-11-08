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
    private static final int BUFFER_SIZE = 16;
    private static final String SOURCE_CHAR_FILE_PATH = "charText.txt";
    private static final String SOURCE_TEXT_FILE_PATH = "aliceInWonderland.txt";
    private static final String SOURCE_IN_FILE_PATH = "inFile.txt";
    private static final String FORMATTED_OUTPUT_FILE_PATH = "formatted_out.txt";
    private static final String INDEX_OUTPUT_FILE_PATH = "index_out.txt";
    private static final String DEFAULT_DICT_FILE_PATH = "dict.txt";
    private static final Integer DEFAULT_LINE_LENGTH = 40;
    public static void main(String[] args) {
        String task = null;
        String inFile = null;
        String indexOut = null;
        String formattedOut = null;
        String dictFile = null;
        TextAlignment alignment = null;
        Integer lineLength = null;

        for (String arg : args) {
            String[] argParts = arg.split("=");

            if(argParts.length == 2) {
                switch (argParts[0]) {
                    case "task" : {
                        task = argParts[1];
                        break;
                    }

                    case "inFile": {
                        inFile = argParts[1];
                        break;
                    }

                    case "indexOut": {
                        indexOut = argParts[1];
                        break;
                    }

                    case "formattedOut": {
                        formattedOut = argParts[1];
                        break;
                    }

                    case "dictFile": {
                        dictFile = argParts[1];
                        break;
                    }

                    case "alignment": {
                        alignment = parseTextAlignment(argParts[1]);
                        break;
                    }

                    case "lineLength": {
                        lineLength = parseLineLength(argParts[1]);
                        break;
                    }

                    default: System.out.println("Argument \"" + arg + "\" is not supported.");
                }

            } else {
                System.out.println("Argument \"" + arg + "\" is not supported.");
            }
        }

        if ("A".equalsIgnoreCase(task)) {

            if (inFile == null) {
                inFile = SOURCE_IN_FILE_PATH;
                System.out.println("Input file argument was empty, using default value: " + SOURCE_IN_FILE_PATH);
            }

            if (indexOut == null) {
                indexOut = INDEX_OUTPUT_FILE_PATH;
                System.out.println("Index Output argument was empty, using default value: " + INDEX_OUTPUT_FILE_PATH);
            }

            if (dictFile == null) {
                dictFile = DEFAULT_DICT_FILE_PATH;
                System.out.println("Dictionary argument was empty, using default value: " + DEFAULT_DICT_FILE_PATH);
            }

            /* TASK A */
            StringLinesFileSink aOutputFileSink = new StringLinesFileSink(indexOut);
            runTaskAPush(dictFile, inFile, aOutputFileSink);

        } else if ("B".equalsIgnoreCase(task)) {

            if (inFile == null) {
                inFile = SOURCE_IN_FILE_PATH;
                System.out.println("Input file argument was empty, using default value: " + SOURCE_IN_FILE_PATH);
            }

            if (indexOut == null) {
                indexOut = INDEX_OUTPUT_FILE_PATH;
                System.out.println("Index Output argument was empty, using default value: " + INDEX_OUTPUT_FILE_PATH);
            }

            if (dictFile == null) {
                dictFile = DEFAULT_DICT_FILE_PATH;
                System.out.println("Dictionary argument was empty, using default value: " + DEFAULT_DICT_FILE_PATH);
            }

            if (formattedOut == null) {
                formattedOut = FORMATTED_OUTPUT_FILE_PATH;
                System.out.println("Formatted Output argument was empty, using default value: " + FORMATTED_OUTPUT_FILE_PATH);
            }

            if (alignment == null) {
                alignment = TextAlignment.NONE;
                System.out.println("Text alignment argument was empty, using default value: NONE");
            }

            if (lineLength == null) {
                lineLength = DEFAULT_LINE_LENGTH;
                System.out.println("Line length argument was empty, using default value: " + DEFAULT_LINE_LENGTH);
            }

            /* TASK B */
            TextCharSupplierPipe textCharSupplierPipe = new TextCharSupplierPipe(inFile);
            StringLineFileSink bFormattedOutputFileSink = new StringLineFileSink(formattedOut);
            StringLinesFileSink bIndexOutputFileSink = new StringLinesFileSink(indexOut);
            runTaskB(textCharSupplierPipe, bFormattedOutputFileSink, bIndexOutputFileSink, alignment, lineLength, dictFile);

        } else {
            System.out.println("Task \"" + task + "\" is not supported.");
        }
    }

    private static void runTaskAPull(String dictFile, Readable<StringLine> input, Writable<List<StringLine>> outputSink) {

        BufferedSyncPipe<StringLine> stringLinePipe = new BufferedSyncPipe<>(BUFFER_SIZE);

        // 1. String (PULL) -> StringLine
        new Thread(
            new LineIndexFilter(input, stringLinePipe)
        ).start();

        runTaskA(dictFile, stringLinePipe, outputSink);
    }

    private static void runTaskAPush(String dictFile, String sourceFilePath, Writable<List<StringLine>> outputSink) {

        BufferedSyncPipe<StringLine> stringLinePipe = new BufferedSyncPipe<>(BUFFER_SIZE);

        // 1. Source (PUSH) -> StringLine
        new Thread(
            new LineIndexFilter(sourceFilePath, stringLinePipe)
        ).start();

        runTaskA(dictFile, stringLinePipe, outputSink);
    }

    private static void runTaskA(String dictFile, Readable<StringLine> input, Writable<List<StringLine>> outputSink) {

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
            new WordDictionaryFilter(wordShiftedPipe, dictionaryCleanedPipe, dictFile)
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
        int lineLength,
        String dictFile
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
        runTaskAPull(dictFile, splitPipe, indexOutputSink);
    }

    private static Integer parseLineLength(String s) {
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            System.out.println("Error occurs while parsing line length. (" + s + ")");
            return null;
        }
    }

    private static TextAlignment parseTextAlignment(String s) {
        try {
            return s != null ? TextAlignment.valueOf(s.toUpperCase()) : null;
        } catch (NullPointerException | IllegalArgumentException e) {
            System.out.println("Error occurs while parsing text alignment. (" + s + ")");
            return null;
        }
    }
}
