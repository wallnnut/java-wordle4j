package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Logger;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    static final Logger logger = AppLogger.getLogger(WordleDictionaryLoader.class);
    private Function<String, String> normalize;
    private final String PROJECT_DIR = System.getProperty("user.dir");
    private final String fileName;
    private Path absolutePathToFile;

    public WordleDictionaryLoader(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            this.fileName = fileName.trim();
            this.absolutePathToFile = Paths.get(PROJECT_DIR, this.fileName);
        } else {
            throw new IllegalArgumentException("Название файла не должно быть пустым");
        }
    }

    public WordleDictionaryLoader(String fileName, Function<String, String> normalize) {
        this(fileName);
        this.normalize = normalize;
    }

    public Set<String> load() throws DictionaryLoadException {
        return uploadWords();
    }

    private Set<String> uploadWords() throws DictionaryLoadException {
        Set<String> wordsList = new HashSet<>();

        try (Reader fileReader = new FileReader(absolutePathToFile.toString(), StandardCharsets.UTF_8);) {
            BufferedReader reader = new BufferedReader(fileReader);
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.length() == 5) {
                    wordsList.add(normalize.apply(line));
                }
            }
            return wordsList;
        } catch (IOException e) {
            throw new DictionaryLoadException("Ошибка чтения файла словаря: " + fileName, e);
        }

    }

}
