package ru.yandex.practicum;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.mockito.Mockito.*;

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

    @Test
    void shouldReturnHintFromFiveLetterWords() {

        WordleDictionary dict = mock(WordleDictionary.class);

        when(dict.getRandomWord()).thenReturn("яблок");
        when(dict.getWords()).thenReturn(List.of("яблок", "груша", "слива"));
        when(dict.hasWord(anyString())).thenReturn(true);

        when(dict.filter(anyMap(), anySet(), anySet())).thenReturn(new ArrayList<>(List.of("яблок", "груша", "слива")));

        WordleGame game = new WordleGame(dict);

        String hint = game.getHint();

        assertTrue(List.of("яблок", "груша", "слива").contains(hint));
    }

    @Test
    void shouldReturnNoHintsMessage() {

        WordleDictionary dict = mock(WordleDictionary.class);

        when(dict.getRandomWord()).thenReturn("яблок");
        when(dict.getWords()).thenReturn(List.of("яблок"));
        when(dict.hasWord(anyString())).thenReturn(true);

        when(dict.filter(anyMap(), anySet(), anySet())).thenReturn(new ArrayList<>());

        WordleGame game = new WordleGame(dict);

        String hint = game.getHint();

        assertEquals("Нет доступных подсказок", hint);
    }

    @Test
    void shouldStoreUsedHint() {

        WordleDictionary dict = mock(WordleDictionary.class);

        when(dict.getRandomWord()).thenReturn("яблок");
        when(dict.getWords()).thenReturn(List.of("яблок", "груша"));
        when(dict.hasWord(anyString())).thenReturn(true);

        when(dict.filter(anyMap(), anySet(), anySet())).thenReturn(new ArrayList<>(List.of("яблок", "груша")));

        WordleGame game = new WordleGame(dict);

        String hint = game.getHint();

        assertTrue(game.getUsedHints().contains(hint));
    }

    @Test
    void shouldNotRepeatHintIfPossible() {

        WordleDictionary dict = mock(WordleDictionary.class);

        when(dict.getRandomWord()).thenReturn("яблок");
        when(dict.getWords()).thenReturn(List.of("яблок", "груша", "слива"));
        when(dict.hasWord(anyString())).thenReturn(true);

        when(dict.filter(anyMap(), anySet(), anySet())).thenReturn(new ArrayList<>(List.of("яблок", "груша", "слива")));

        WordleGame game = new WordleGame(dict);

        String first = game.getHint();
        String second = game.getHint();

        assertNotEquals(first, second);
    }

}
