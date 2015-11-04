package indsys.filter;

import indsys.entity.Word;
import thirdparty.filter.DataTransformationFilter;
import thirdparty.interfaces.Readable;
import thirdparty.interfaces.Writable;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Filters a List of sentence of words for the first word and if it is contained in the Set<String> DICTIONARY
 * it is removed from the sentence
 */
public class DictionaryFilter extends DataTransformationFilter<List<List<Word>>> {
    public static final Set<String> DICTIONARY = new HashSet<>();

    public DictionaryFilter(Readable<List<List<Word>>> input, Writable<List<List<Word>>> output)
    throws InvalidParameterException {
        super(input, output);

        // add the 'useless' words to the DICTIONARY
        DICTIONARY.add("and");
        DICTIONARY.add("are");
        DICTIONARY.add("is");

    }

    @Override
    protected void process(List<List<Word>> wordLists) {
        if (wordLists != null && !wordLists.isEmpty()) {

            for (List<Word> wordList : wordLists) { // loop through sentence, get current sentence
                // if contains current dictword remove the first word of sentence

                String word =  wordList.get(0).getValue();
                if (DICTIONARY.contains(word)) {
                    wordList.remove(0);
                }
            }
        }
    }
}
