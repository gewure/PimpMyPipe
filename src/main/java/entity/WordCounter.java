package entity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sereGkaluv on 01-Nov-15.
 */
public class WordCounter extends GenericToken<Map<Integer, Integer>> {
    protected WordCounter() {
        _value = new HashMap<>();
        _value.put(0, 0); //LineIndex, WordsAmount
    }

    public void increaseWordCounterForLine(int lineIndex) {
        //Receiving words amount or 0 if we are inspecting this line at the first time.
        Integer wordsAmount = _value.getOrDefault(lineIndex, 0);

        //Updating words amount for this LineIndex.
        _value.put(lineIndex, ++wordsAmount);
    }
}
