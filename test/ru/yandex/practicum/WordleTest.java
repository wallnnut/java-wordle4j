package ru.yandex.practicum;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

class WordleTest {

    @Test
    void compareWordsTest1() {
        WordleDictionary dic = new WordleDictionary(new ArrayList<String>(List.of("лиман")));
        String secret = dic.getRandomWord();
        String guessed = "лимон";
        String result = dic.compareWords(guessed, secret);
        String expected = "+++-+";

        assertEquals(expected, result);
    }

    @Test
    void compareWordsTest2() {
        WordleDictionary dic = new WordleDictionary(new ArrayList<String>(List.of("лиман")));
        String secret = dic.getRandomWord();
        String guessed = "комет";
        String result = dic.compareWords(guessed, secret);
        String expected = "--+--";

        assertEquals(expected, result);
    }

    @Test
    void compareWordsTest3() {
        WordleDictionary dic = new WordleDictionary(new ArrayList<String>(List.of("лиман")));
        String secret = dic.getRandomWord();
        String guessed = "город";
        String result = dic.compareWords(guessed, secret);
        String expected = "-----";

        assertEquals(expected, result);
    }

    @Test
    void compareWordsTest4() {
        WordleDictionary dic = new WordleDictionary(new ArrayList<String>(List.of("лиман")));
        String secret = dic.getRandomWord();
        String guessed = "нмали";
        String result = dic.compareWords(guessed, secret);
        String expected = "^^^^^";

        assertEquals(expected, result);
    }

    @Test
    void compareWordsTest5() {
        WordleDictionary dic = new WordleDictionary(new ArrayList<String>(List.of("тарын")));
        String secret = dic.getRandomWord();
        String guessed = "шалаш";
        String result = dic.compareWords(guessed, secret);
        String expected = "-+-^-";

        assertEquals(expected, result);
    }

    @Test
    void testDictionaryNormalization() {
        assertEquals("арбуз", WordleDictionary.normalize(" Арбуз "));
        assertEquals("елка", WordleDictionary.normalize("Ёлка"));
        assertEquals("", WordleDictionary.normalize(null));
    }

    @Test
    void testDictionaryFilter() {
        Map<Integer, Character> exact = Map.of(0, 'к', 4, 'а');
        Set<Character> present = Set.of('н');
        Set<Character> absent = Set.of('р', 'б');
        List<String> testWords = List.of("арбуз", "книга", "трава", "слово", "экран", "буква");
        WordleDictionary dictionary = new WordleDictionary(testWords);
        List<String> filtered = dictionary.filter(exact, present, absent);
        assertTrue(filtered.contains("книга"));
        assertFalse(filtered.contains("арбуз")); // есть 'р' и 'б' в absent
        assertFalse(filtered.contains("трава")); // не подходит по exact
    }

}
