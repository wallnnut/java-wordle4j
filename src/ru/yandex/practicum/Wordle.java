package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {
    static final Logger logger = AppLogger.getLogger(Wordle.class);
    static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            /**
             * Инициализация загрузчика + передача функции нормализации
             */
            WordleDictionaryLoader loader = new WordleDictionaryLoader("words_ru.txt", WordleDictionary::normalize);
            /**
             * Загрузка слов из файла
             */
            ArrayList<String> wordsList = new ArrayList<>(loader.load());
            /**
             * Инициализация справочника
             */
            WordleDictionary dictionary = new WordleDictionary(wordsList);
            /**
             * Инициализация игры передаем справочник
             */
            WordleGame game = new WordleGame(dictionary);

            String answer = "";
            logger.info("Запуск игры");

            System.out.println("Добро пожаловать в Wordle!");
            System.out.println("Угадайте слово из 5 букв. У вас 6 попыток.");
            System.out.println("Введите слово, нажмите Enter для подсказки или 'exit' для выхода.\n");

            while (game.isGameRunning(answer)) {
                runGameLoop(game);
            }
            System.out.printf("Загаданное слово: %s", game.getCorrectAnswer());
        } catch (DictionaryLoadException e) {
            logger.log(Level.SEVERE, "Ошибка загрузки словаря", e);
        }

    }

    private static void runGameLoop(WordleGame game) {
        String answer = "";

        while (game.isGameRunning(answer)) {
            try {
                answer = WordleDictionary.normalize(sc.nextLine());

                if (answer.equalsIgnoreCase("exit")) {
                    logger.info("Завершение сессии пользователем");
                    System.exit(0);
                }

                if (answer.isEmpty()) {
                    System.out.printf("Подсказка: %s%n", game.getHint());
                    continue;
                }

                String comparingResult = game.handleUserAnswer(answer);

                System.out.println(comparingResult);

            } catch (InvalidWordException e) {
                System.out.println(e.getMessage());
                logger.log(Level.INFO, e.getMessage(), e);
            }
        }
    }

}
