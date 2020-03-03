package lesson03.task01;

public class TestFilterCount {

    private TestFilterCount() {
    }

    public static void main(String[] args) {
        String[] words = {
                "мама", "мама", "мама",
                "мыла", "мыла", "мыла",
                "раму", "раму", "раму",
                "мама", "мыла", "раму",
                "мама", "мыла", "Рому"
        };
        UniqueWordsCountUtil.filterCountUniqueWords(words);
    }
}
