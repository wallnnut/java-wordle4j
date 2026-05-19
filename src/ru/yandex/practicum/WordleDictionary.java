package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/*
 * этот класс содержит в себе список слов List<String> его методы похожи на
 * методы списка, но учитывают особенности игры также этот класс может содержать
 * рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {
    private final List<String> words;
    private final Random r = new Random();

    public WordleDictionary(List<String> words) {
        if (words != null && !words.isEmpty()) {
            this.words = words;
        } else {
            throw new IllegalArgumentException("Список слов не может быть пустым");
        }
    }

    public String getRandomWord() {
        int random = r.nextInt(words.size());
        return words.get(random);
    }

    public boolean hasWord(String word) {
        return words.contains(word);
    }

    public String compareWords(String guess, String answer) {

        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < guess.length(); i++) {

            char g = guess.charAt(i);
            char a = answer.charAt(i);

            if (g == a) {
                builder.append('+');
            } else if (answer.indexOf(g) >= 0) {
                builder.append('^');
            } else {
                builder.append('-');
            }
        }

        return builder.toString();
    }

    public List<String> getWords() {
        return words;
    }

    public static String normalize(String word) {
        if (word == null) {
            return "";
        }
        return word.trim().toLowerCase().replace("ё", "е");
    }

    public List<String> filter(Map<Integer, Character> exactMatches, Set<Character> presentChars,
            Set<Character> absentChars) {
        List<String> candidates = new ArrayList<>();
        for (String word : words) {
            if (matchesConstraints(word, exactMatches, presentChars, absentChars)) {
                candidates.add(word);
            }
        }
        return candidates;
    }

    private boolean matchesConstraints(String word, Map<Integer, Character> exact, Set<Character> present,
            Set<Character> absent) {
        for (Map.Entry<Integer, Character> entry : exact.entrySet()) {
            if (word.charAt(entry.getKey()) != entry.getValue())
                return false;
        }
        for (char c : present) {
            if (!word.contains(String.valueOf(c)))
                return false;
        }
        for (char c : absent) {
            if (word.contains(String.valueOf(c)))
                return false;
        }
        return true;
    }

}
