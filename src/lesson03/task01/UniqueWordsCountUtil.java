package lesson03.task01;

import java.util.HashMap;
import java.util.Map;

public class UniqueWordsCountUtil {
    private UniqueWordsCountUtil() {
    }

    public static void filterCountUniqueWords(String[] words){
      Map<String, Integer> uniqueWords = new HashMap<>();
        for(String word : words){
            uniqueWords.merge(word , 1 , (a , b) -> a + 1);
        }
        uniqueWords.forEach((k, v) -> System.out.println(k + " - " + v));
    }
}
