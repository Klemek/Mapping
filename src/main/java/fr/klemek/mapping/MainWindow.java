package fr.klemek.mapping;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.*;

class MainWindow extends JFrame {

    static final String FILE_NAME = "mapping.csv";
    private static final String VERSION = "1.2";
    private static final int DEFAULT_SIZE = 17;
    private static final String INFO_TEXT = "" +
            "<html>" +
            "<h3>INFO</h3>" +
            "[Mouse wheel] - Set height (small)<br>" +
            "[CTRL+Mouse wheel] - Set height (big)<br>" +
            "[SHIFT+Mouse wheel] - Set surrounding heights<br>" +
            "[BACKSPACE] - Erase everything<br>" +
            "[ARROWS] - Move view<br>" +
            "[PAGE UP/DOWN] - Change size<br>" +
            "[CTRL+S] - Save current work<br>" +
            "[SPACE] - Change tool<br>" +
            "[DELETE] - Reset node/face<br>" +
            "[0] - Show/hide this info<br>" +
            "[1] - Show/hide grid<br>" +
            "[2] - Show/hide special faces<br>" +
            "[3] - Add random<br>";
    private transient MainPanel mp;
    private transient RefreshThread refresh;


    MainWindow() {
        Map m = new Map(DEFAULT_SIZE);
        String saved = Utils.openFile(FILE_NAME);
        if (saved != null)
            m = new Map(saved);
        this.mp = new MainPanel(m, tool -> this.setTitle("Mapping v" + VERSION + " (" + tool.name + ")"));
        this.refresh = new RefreshThread(this.mp, 100);
        this.initWindow();
    }

    private void initWindow() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setTitle("Mapping v" + VERSION + " (" + this.mp.getTool().name + ")");
        this.setSize(new Dimension(800, 600));

        this.getContentPane().setLayout(new BorderLayout(0, 0));

        this.setLocationRelativeTo(null);
        this.setVisible(true);

        JLayeredPane layers = new JLayeredPane();
        layers.setLayout(null);
        layers.add(this.mp, (Integer) 0);

        JPanel infoLayer = new JPanel(new BorderLayout(0, 0));
        infoLayer.setBounds(10, 10, 800, 600);
        infoLayer.setOpaque(false);
        JLabel info = new JLabel();
        info.setText(INFO_TEXT);
        infoLayer.add(info, BorderLayout.NORTH);

        layers.add(infoLayer, (Integer) 1);

        this.mp.setSize(new Dimension(800, 600));

        this.getContentPane().add(layers, BorderLayout.CENTER);

        this.revalidate();
        this.repaint();

        this.refresh.start();
        this.addKeyListener(new KeyEventListener(this.mp, infoLayer));

        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                MainWindow.this.mp.setSize(MainWindow.this.getSize());
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                //ignored
            }

            @Override
            public void componentShown(ComponentEvent e) {
                //ignored
            }

            @Override
            public void componentHidden(ComponentEvent e) {
                //ignored
            }
        });
    }

}
