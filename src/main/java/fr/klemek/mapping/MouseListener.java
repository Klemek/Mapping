package fr.klemek.mapping;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class MouseListener implements MouseMotionListener, MouseWheelListener {

    private MainPanel mp;

    MouseListener(MainPanel mp) {
        this.mp = mp;
        this.mp.addMouseWheelListener(this);
        this.mp.addMouseMotionListener(this);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        //ignored
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mp.computeMouseMoved(e.getPoint());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        this.mp.computeMouseWheel(e.getWheelRotation(), e.isControlDown(), e.isShiftDown());
    }
}
