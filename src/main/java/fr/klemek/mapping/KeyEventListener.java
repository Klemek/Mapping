package fr.klemek.mapping;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

public class KeyEventListener implements KeyListener {

    private static final float RATIO_STEP = 1.05f;
    private static final float ANGLE_STEP = (float) (Math.PI / 64f);

    private MainPanel mp;
    private JPanel infoPanel;

    KeyEventListener(MainPanel mp, JPanel infoPanel) {
        this.mp = mp;
        this.infoPanel = infoPanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //ignored
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_BACK_SPACE:
                int n = JOptionPane.showConfirmDialog(
                        this.mp,
                        "Are you sure you want to erase everything ?",
                        "Warning",
                        JOptionPane.YES_NO_OPTION);
                if (n == JOptionPane.YES_OPTION) {
                    this.mp.getMap().reset();
                    this.mp.setRatio(MainPanel.DEFAULT_RATIO);
                    this.mp.setAngle(0f);
                }
                break;
            case KeyEvent.VK_0:
            case KeyEvent.VK_NUMPAD0:
                this.infoPanel.setVisible(!this.infoPanel.isVisible());
                break;
            case KeyEvent.VK_1:
            case KeyEvent.VK_NUMPAD1:
                this.mp.setShowGrid(!this.mp.isShowGrid());
                break;
            case KeyEvent.VK_2:
            case KeyEvent.VK_NUMPAD2:
                this.mp.getMap().randomize();
                break;
            case KeyEvent.VK_PAGE_UP:
                this.mp.getMap().changeSize(this.mp.getMap().size() + 1);
                break;
            case KeyEvent.VK_PAGE_DOWN:
                this.mp.getMap().changeSize(this.mp.getMap().size() - 1);
                break;
            case KeyEvent.VK_UP:
                this.mp.setRatio(this.mp.getRatio() / RATIO_STEP);
                break;
            case KeyEvent.VK_DOWN:
                this.mp.setRatio(this.mp.getRatio() * RATIO_STEP);
                break;
            case KeyEvent.VK_LEFT:
                this.mp.setAngle(this.mp.getAngle() + ANGLE_STEP);
                break;
            case KeyEvent.VK_RIGHT:
                this.mp.setAngle(this.mp.getAngle() - ANGLE_STEP);
                break;
            case KeyEvent.VK_S:
                if (e.isControlDown()) {
                    int n2 = JOptionPane.showConfirmDialog(
                            this.mp,
                            "Do you want to save ?",
                            "Save",
                            JOptionPane.YES_NO_OPTION);
                    if (n2 == JOptionPane.YES_OPTION) {
                        if (FileUtils.save(MainWindow.FILE_NAME, this.mp.getMap().toString())) {
                            JOptionPane.showInternalMessageDialog(this.mp,
                                    "Saved as '" + MainWindow.FILE_NAME + "'",
                                    "Saved",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showInternalMessageDialog(this.mp,
                                    "Error while saving",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //ignored
    }
}
