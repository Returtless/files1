package com.company;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
        createFilesAndDirectories();
        saveDataFiles();
    }

    public static void saveDataFiles() {
        File saveGamesDir = new File(SAVEGAMES_PATH);

        if (!saveGamesDir.exists()) {
            System.out.println("Директория для сохранения файлов не существует. Дальнейшая работа программы невозможна");
            return;
        }
        //Создать три экземпляра класса GameProgress.
        GameProgress progress1 = new GameProgress(300, 1, 1, 30);
        GameProgress progress2 = new GameProgress(500, 2, 30, 100);
        GameProgress progress3 = new GameProgress(1000, 3, 60, 500);
        //Сохранить сериализованные объекты GameProgress в папку savegames
        saveFile("save1.dat", progress1);
        saveFile("save2.dat", progress2);
        saveFile("save3.dat", progress3);

        //Отбор только файлов типа .dat, чтобы при повторном вызове кода не захватывался архив
        File[] files = saveGamesDir.listFiles((dir, name) -> name.endsWith(".dat"));
        zipFiles(SAVEGAMES_PATH + "archive.zip", Arrays.asList(Objects.requireNonNull(files)));
    }

    public static void createFilesAndDirectories() {
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
        if (tempFile.canWrite()) {
            try (FileWriter fileWriter = new FileWriter(tempFile)) {
                fileWriter.write(log.toString());
            } catch (IOException e) {
                System.out.println("Ошибка записи в файл");
            }
        }
    }

    public static void saveFile(String filename, GameProgress gameProgress) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                new FileOutputStream(SAVEGAMES_PATH + filename))) {
            objectOutputStream.writeObject(gameProgress);
        } catch (IOException e) {
            System.out.println("Ошибка сохранения в файл");
        }
    }

    public static void zipFiles(String archiveFileName, List<File> files) {
        //Созданные файлы сохранений из папки savegames запаковать в архив zip.
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(archiveFileName))) {
            for (File file : files) {
                if (file.exists()) {
                    FileInputStream fis = new FileInputStream(file.getAbsolutePath());
                    ZipEntry entry = new ZipEntry(file.getName());
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                    fis.close();
                    //Удалить файлы сохранений, лежащие вне архива.
                    file.delete();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static boolean createFile(File file) {
        if (file.exists()) {
            return appendExistResultToLog(log, file.getAbsolutePath(), true);
        }
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
        if (file.exists()) {
            return appendExistResultToLog(log, file.getAbsolutePath(), false);
        }
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

    public static boolean appendExistResultToLog(StringBuilder log, String path, boolean isFile) {
        if (isFile) {
            log.append("Файл ").append(path).append(" уже существует\n");
        } else {
            log.append("Папка по пути ").append(path).append(" уже существует\n");
        }
        return true;
    }
}
