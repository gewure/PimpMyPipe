package indsys.filter;

import indsys.entity.Word;
import indsys.entity.WordList;
import thirdparty.filter.DataTransformationFilter;
import thirdparty.interfaces.Readable;
import thirdparty.interfaces.Writable;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Filters a List of sentence of words for the first word and if it is contained in the Set<String> dictionary
 * it is removed from the sentence
 */
public class DictionaryFilter extends DataTransformationFilter<List<WordList>> {

    public Set<String> dictionary = new HashSet<String>();

    /*
     * constructor
     */
    public DictionaryFilter(Readable<List<WordList>> input, Writable<List<WordList>> output) throws InvalidParameterException {
        super(input, output);

        // add the 'useless' words to the dictionary
        dictionary.add("and");
        dictionary.add("are");
        dictionary.add("is");

    }

    @Override
    protected void process(List<WordList> wholeText) {

        if (wholeText != null) {
            if (wholeText.isEmpty()) {
                return;
            }


            for (int i = 0; i < wholeText.size(); i++) { // loop through sentence
                WordList thisSentence = wholeText.get(i); // get current sentence

                //foreach word in dictionary
                // if contains current dictword remove the first word of sentence
                if (dictionary.contains(thisSentence.getValue().get(0))) {
                    thisSentence.getValue().remove(0);
                }
            }
        }
    }


    private WordList listToWordList(int id, List<Word> list) {
        return new WordList(id, new LinkedList<>(list));

    }
}
