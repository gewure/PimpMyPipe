package indsys.filter;

import indsys.entity.Word;
import thirdparty.filter.DataEnrichmentFilter;
import thirdparty.filter.DataTransformationFilter;
import thirdparty.interfaces.Readable;
import thirdparty.interfaces.Writable;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Filters a List of sentence of words for the first word and if it is contained in the Set<String> DICTIONARY
 * it is removed from the sentence
 */
public class WordDictionaryFilter extends DataEnrichmentFilter<List<List<Word>>, List<List<Word>>> {
    private static final String DEFAULT_DICT_FILE_PATH = "dict.txt";
    public static final Set<String> DICTIONARY = new HashSet<>();

    public WordDictionaryFilter(Readable<List<List<Word>>> input, Writable<List<List<Word>>> output)
    throws InvalidParameterException {
        super(input, output);

        // adding the 'useless' words to the DICTIONARY
        loadDictionaryFromFile(DEFAULT_DICT_FILE_PATH);
    }

    public WordDictionaryFilter(Readable<List<List<Word>>> input, Writable<List<List<Word>>> output, String pathToDict)
    throws InvalidParameterException {
        super(input, output);

        // adding the 'useless' words to the DICTIONARY
        loadDictionaryFromFile(pathToDict);
    }

    @Override
    protected boolean fillEntity(List<List<Word>> wordLists, List<List<Word>> entity) {
        if (wordLists != null && !wordLists.isEmpty()) {

            for (List<Word> wordList : wordLists) { // loop through word lists, get current list (line)

                // if the first word is present in the dictionary - remove it from line
                String word =  wordList.get(0).getValue();
                if (DICTIONARY.contains(word)) {
                    wordList.remove(0);
                }

                //check if current line is not empty
                if (!wordList.isEmpty()) {
                    entity.add(wordList);
                }
            }

            //check if the whole list is not empty
            if (!entity.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected List<List<Word>> getNewEntityObject() {
        return new LinkedList<>();
    }

    private static void loadDictionaryFromFile(String pathToDict){
        try (BufferedReader br = Files.newBufferedReader(Paths.get(pathToDict))) {

            for (String line = br.readLine(); line != null; line = br.readLine()) {
                DICTIONARY.add(line);
            }

        } catch (IOException e) {
            System.out.println(
                "ERROR occurs while reading the dictionary file \"" +
                DEFAULT_DICT_FILE_PATH +
                "\"."
            );
        }
    }
}
