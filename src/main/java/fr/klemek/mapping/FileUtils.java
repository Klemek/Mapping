package fr.klemek.mapping;

import fr.klemek.logger.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

final class FileUtils {

    private FileUtils() {

    }

    static boolean save(String fileName, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(content);
            return true;
        } catch (IOException ex) {
            Logger.log(ex);
            return false;
        }
    }

    static String open(String fileName) {
        try (BufferedReader bw = new BufferedReader(new FileReader(fileName))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = bw.readLine()) != null) {
                output.append(line);
                output.append('\n');
            }
            return output.toString();
        } catch (IOException ex) {
            Logger.log(ex);
            return null;
        }
    }

}
