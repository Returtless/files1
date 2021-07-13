package com.company;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static StringBuilder log = new StringBuilder();

    public static final String FOLDER_PATH = "/Users/returtless/IdeaProjects/files1/Games/";

    public static final String SRC_PATH = FOLDER_PATH + "src/";
    public static final String RES_PATH = FOLDER_PATH + "res/";
    public static final String SAVEGAMES_PATH = FOLDER_PATH + "savegames/";
    public static final String TEMP_PATH = FOLDER_PATH + "temp/";

    public static final String MAIN_PATH = SRC_PATH + "main/";
    public static final String TEST_PATH = SRC_PATH + "test/";

    public static final String DRAWABLES_PATH = RES_PATH + "drawables/";
    public static final String VECTORS_PATH = RES_PATH + "vectors/";
    public static final String ICONS_PATH = RES_PATH + "icons/";

    public static void main(String[] args) {
        //В папке Games создайте несколько директорий: src, res, savegames, temp
        if (createDirectory(new File(SRC_PATH))) {
            //В каталоге src создайте две директории: main, test.
            if (createDirectory(new File(MAIN_PATH))) {
                createFile(new File(MAIN_PATH + "Main.java"));
                createFile(new File(MAIN_PATH + "Utils.java"));
            }
            createDirectory(new File(TEST_PATH));
        }
        //В каталог res создайте три директории: drawables, vectors, icons.
        if (createDirectory(new File(RES_PATH))) {
            createDirectory(new File(DRAWABLES_PATH));
            createDirectory(new File(VECTORS_PATH));
            createDirectory(new File(ICONS_PATH));
        }
        createDirectory(new File(SAVEGAMES_PATH));

        createDirectory(new File(TEMP_PATH));

        //В директории temp создайте файл temp.txt.*/
        File tempFile = new File(TEMP_PATH + "temp.txt");
        createFile(tempFile);
        if (tempFile.canWrite()){
            try (FileWriter fileWriter = new FileWriter(tempFile)){
                fileWriter.write(log.toString());
            } catch (IOException e) {
                System.out.println("Ошибка записи в файл");
            }
        }

    }

    public static boolean createFile(File file) {
        try {
            if (file.createNewFile()) {
                return appendResultToLog(log, file.getAbsolutePath(), true, true);
            } else {
                return appendResultToLog(log, file.getAbsolutePath(), true, false);
            }
        } catch (IOException ex) {
            return appendResultToLog(log, file.getAbsolutePath(), true, false);
        }
    }

    public static boolean createDirectory(File file) {
        if (file.mkdir()) {
           return appendResultToLog(log, file.getAbsolutePath(), false, true);
        } else {
            return appendResultToLog(log, file.getAbsolutePath(), false, false);
        }
    }

    public static boolean appendResultToLog(StringBuilder log, String path, boolean isFile, boolean result) {
        if (result) {
            if (isFile) {
                log.append("Файл ").append(path).append(" был создан\n");
            } else {
                log.append("Папка по пути ").append(path).append(" была создана\n");
            }
            return true;
        } else {
            log.append("Ошибка создания ").append(isFile ? "файла" : "папки").append(" папки по пути ").append(path).append("\n");
            return false;
        }
    }
}
