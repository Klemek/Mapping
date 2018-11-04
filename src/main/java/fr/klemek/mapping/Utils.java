package fr.klemek.mapping;

import fr.klemek.logger.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

final class Utils {

    private Utils() {

    }

    static int dist2(int x1, int y1, int x2, int y2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
    }

    static boolean saveFile(String fileName, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write(content);
            return true;
        } catch (IOException ex) {
            Logger.log(Level.WARNING, ex);
            return false;
        }
    }

    static String openFile(String fileName) {
        try (BufferedReader bw = new BufferedReader(new FileReader(fileName))) {
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = bw.readLine()) != null) {
                output.append(line);
                output.append('\n');
            }
            return output.toString();
        } catch (IOException ex) {
            Logger.log(Level.WARNING, ex);
            return null;
        }
    }

}
