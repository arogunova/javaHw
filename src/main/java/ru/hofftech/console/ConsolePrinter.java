package ru.hofftech.console;

/**
 * Утилитарный класс для вывода информации в консоль.
 * Содержит методы для вывода справки, ошибок и сообщений об успехе.
 */
public class ConsolePrinter {

    /**
     * Выводит полную справку по использованию программы.
     * Показывает все доступные режимы работы и примеры команд.
     */
    public static void printUsage() {
        System.out.println("=== TRUCK LOADER USAGE ===");
        System.out.println();
        System.out.println("=== РЕЖИМ 1: Работа с посылками (CRUD) ===");
        System.out.println("  find-all");
        System.out.println("       Пример: find-all");
        System.out.println();
        System.out.println("  find <name>");
        System.out.println("       Пример: find Посылка_тип_1");
        System.out.println();
        System.out.println("  create -name <name> -form <form>");
        System.out.println("       Пример: create -name \"Куб\" -form \"XXX\\nXXX\\nXXX\"");
        System.out.println();
        System.out.println("  delete <name>");
        System.out.println("       Пример: delete Куб");
        System.out.println();
        System.out.println("  load -parcels-text \"<name1>\\n<name2>\" -type <algorithm> -out <text|json-file> [-out-filename <file>]");
        System.out.println("       Пример (текст): load -parcels-text \"Посылка_тип_1\\nКуб\" -type maxdense -out text");
        System.out.println("       Пример (JSON):  load -parcels-text \"Посылка_тип_1\\nКуб\" -type maxdense -out json-file -out-filename result.json");
        System.out.println();
        System.out.println("=== РЕЖИМ 2: Погрузка из текстового файла ===");
        System.out.println("  java ru.hofftech.Main <file-path> <algorithm> <max-trucks> [--save <json-file>]");
        System.out.println("       Пример: java ru.hofftech.Main test.txt maxdense 3");
        System.out.println("       Пример с сохранением: java ru.hofftech.Main test.txt maxdense 3 --save result.json");
        System.out.println();
        System.out.println("=== РЕЖИМ 3: Загрузка из JSON ===");
        System.out.println("  java ru.hofftech.Main --load <json-file>");
        System.out.println("       Пример: java ru.hofftech.Main --load result.json");
        System.out.println();
        System.out.println("=== ЗАПУСК TELEGRAM БОТА ===");
        System.out.println("  java ru.hofftech.telegram.BotRunner");
        System.out.println("       (требуется файл bot.properties в resources с bot.token и bot.username)");
        System.out.println("=========================");
    }

    /**
     * Выводит сообщение об ошибке в стандартный поток ошибок.
     *
     * @param message текст сообщения об ошибке
     */
    public static void printError(String message) {
        System.err.println("❌ Ошибка: " + message);
    }

    /**
     * Выводит сообщение об успешном выполнении операции.
     *
     * @param message текст сообщения
     */
    public static void printSuccess(String message) {
        System.out.println("✅ " + message);
    }
}