package com.mycompany;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

/**
 * See also: https://examples.javacodegeeks.com/core-java/lang/processbuilder/java-lang-processbuilder-example/
 */
@SuppressWarnings("UseOfSystemOutOrSystemErr")
public class FilesExample {
    private static final String TXT_FILE = "^(.+?)(\\.txt)";

    private static final String DIR_MY_COMPANY = System.getProperty("user.dir") + "/src/main/java/com/mycompany";
    private static final String DIR_TEMP = DIR_MY_COMPANY + "/temp";
    private static final String DIR_OTHER_TEMP = DIR_MY_COMPANY + "/othertemp";
    private static final String DIR_OUTPUT = DIR_MY_COMPANY + "/output";

    private static final String SOME_FILE = "somefile.txt";
    private static final String SOME_OTHER_FILE = "someotherfile.txt";
    private static final String PROCESSBUILDER_OUTPUT = "ProcessBuilder_output.txt";

    private FilesExample() {
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        int times = 5;

        System.out.println("Walking through " + DIR_TEMP + " and visiting its text files " + times + " times: ");
        for (int i = 0; i < times; i++) {
            Files.walk(Paths.get(DIR_TEMP))
                    .filter(p -> Pattern.compile(TXT_FILE).matcher(p.getFileName().toString()).matches()).forEach(System.out::println);
        }

        System.out.println("Opening " + SOME_OTHER_FILE + " - through Files.lines() - and printing its lines: ");
        Files.lines(Paths.get(DIR_OTHER_TEMP, SOME_OTHER_FILE))
                .forEach(System.out::println);

        System.out.println("Starting process lsof and saving output to " + PROCESSBUILDER_OUTPUT + ". This will show the file opened by Files.lines() is still open!");
        ProcessBuilder processBuilder = new ProcessBuilder();
        Process process = processBuilder
                .command("lsof")
                .redirectOutput(Paths.get(DIR_OUTPUT, PROCESSBUILDER_OUTPUT).toFile())
                .start();

        process.waitFor();
        System.out.println("Printing out lines from " + PROCESSBUILDER_OUTPUT + " that contain " + SOME_FILE + " (visited via Files.walk()): ");

        Files.lines(Paths.get(DIR_OUTPUT, PROCESSBUILDER_OUTPUT))
                .filter(line -> line.contains(SOME_FILE))
                .forEach(System.out::println);

        System.out.println("Printing out lines from " + PROCESSBUILDER_OUTPUT + " that contain " + SOME_OTHER_FILE + " (visited via Files.lines()): ");

        Files.lines(Paths.get(DIR_OUTPUT, PROCESSBUILDER_OUTPUT))
                .filter(line -> line.contains(SOME_OTHER_FILE))
                .forEach(System.out::println);
    }
}
