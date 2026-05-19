package ru.yandex.practicum;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */

public class WordleGame {
    static final Logger logger = AppLogger.getLogger(WordleGame.class);

    private final String correctAnswer;
    private final WordleDictionary dictionary;

    private Random r = new Random();

    private final Set<String> usedWords = new HashSet<>();
    private final Set<String> usedHints = new HashSet<>();

    private final Map<Integer, Character> exactMatches = new HashMap<>();
    private final Set<Character> presentChars = new HashSet<>();
    private final Set<Character> absentChars = new HashSet<>();

    private int stepsCount = 0;

    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
        this.correctAnswer = dictionary.getRandomWord();

        logger.info(String.format("Слов в справочнике: %d, Загаданное слово: %s", dictionary.getWords().size(),
                correctAnswer));
    }

    public void setStepsCount(int stepsCount) {
        this.stepsCount = stepsCount;
    }

    public boolean isGameRuning(String answer) {
        return stepsCount < 6 && !correctAnswer.equals(answer);
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String handleUserAnswer(String answer) {
        logger.info(String.format("Ответ пользователя: %s", answer));

        if (isAnswerValid(answer) && dictionary.hasWord(answer)) {
            String comparingResult = dictionary.compareWords(answer, correctAnswer);

            updateConstraints(answer, comparingResult);

            stepsCount++;

            logger.info(String.format("Результат сравнения: %s, Счетчик шагов %d", comparingResult, stepsCount));

            logger.info(String.format("exactMatches: %s, presentChars: %s, absentChars: %s", exactMatches, presentChars,
                    absentChars));

            return comparingResult;
        } else {
            return "Неверное количество символов в слове или такого слова не существет в справочнике";
        }
    }

    public boolean isAnswerValid(String answer) {
        if (answer != null) {
            return answer.length() == 5;
        } else {
            return false;
        }

    }

    public String getHint() {
        List<String> candidates = dictionary.filter(exactMatches, presentChars, absentChars);
        candidates.removeAll(usedWords);
        candidates.removeAll(usedHints);

        if (candidates.isEmpty()) {
            return "Нет доступных подсказок";
        }

        String hint = candidates.get(r.nextInt(candidates.size()));
        usedHints.add(hint);
        return hint;
    }

    private void updateConstraints(String guess, String feedback) {
        for (int i = 0; i < 5; i++) {
            char c = guess.charAt(i);
            char f = feedback.charAt(i);
            if (f == '+') {
                exactMatches.put(i, c);
            } else if (f == '^') {
                presentChars.add(c);
            } else {
                absentChars.add(c);
            }
        }

        exactMatches.values().forEach(absentChars::remove);
        presentChars.forEach(absentChars::remove);
    }
}
