package fr.klemek.mapping;

import fr.klemek.logger.Logger;

import java.awt.*;

public class Launch {

    public static void main(String... args) {
        Logger.init("logging.properties");
        EventQueue.invokeLater(MainWindow::new);
    }
}
