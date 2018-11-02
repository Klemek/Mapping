package fr.klemek.mapping;

import fr.klemek.logger.Logger;

import java.awt.*;

import javax.swing.*;

class RefreshThread extends Thread {

    private JPanel panel;
    private int time;

    public RefreshThread(JPanel panel, int time) {
        this.panel = panel;
        this.time = time;
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                EventQueue.invokeLater(this.panel::repaint);
                Thread.sleep(time);
            } catch (Exception e) {
                Logger.log(e);
            }
        }
    }
}
