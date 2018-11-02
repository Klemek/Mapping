package fr.klemek.mapping;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.*;

class MainPanel extends JPanel {

    final static float DEFAULT_RATIO = .5f;
    private final static float SIZE = 1.2f;
    private float ratio = DEFAULT_RATIO;

    private Map map;
    private int w, h;
    private int cx, cy;
    private float mw, mh;
    private int sx = -1, sy = -1;

    private int[][] xs;
    private int[][] ys;

    private boolean showGrid;
    private float angle;

    MainPanel(Map map) {
        this.map = map;

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //ignored
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                sx = -1;
                sy = -1;
                if (MainPanel.this.xs != null) {
                    for (int x = 0; x < MainPanel.this.map.size(); x++)
                        for (int y = 0; y < MainPanel.this.map.size(); y++)
                            if (e.getPoint().distance(MainPanel.this.xs[x][y], MainPanel.this.ys[x][y]) < 6) {
                                sx = x;
                                sy = y;
                                return;
                            }
                }
            }
        });
        this.addMouseWheelListener(e -> {
            if (sx >= 0 && sy >= 0) {
                if (e.isShiftDown()) {
                    for (int dx = -1; dx < 2; dx++)
                        for (int dy = -1; dy < 2; dy++)
                            if (sx + dx >= 0 && sx + dx < MainPanel.this.map.size()
                                    && sy + dy >= 0 && sy + dy < MainPanel.this.map.size())
                                MainPanel.this.map.set(sx + dx, sy + dy, MainPanel.this.map.get(sx + dx, sy + dy)
                                        + (e.getPreciseWheelRotation() < 0 ? 1f : -1f)
                                        * (e.isControlDown() ? 1f : .1f));
                } else {
                    MainPanel.this.map.set(sx, sy, MainPanel.this.map.get(sx, sy)
                            + (e.getPreciseWheelRotation() < 0 ? 1f : -1f)
                            * (e.isControlDown() ? 1f : .1f));
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));

        this.w = this.getWidth();
        this.h = this.getHeight();
        this.cx = Math.round(this.w / 2f);
        this.cy = Math.round(this.h / 2f);
        if (this.w > this.h) {
            this.mh = this.h * SIZE * ratio;
            this.mw = mh / ratio;
        } else {
            this.mw = this.w * SIZE;
            this.mh = mw * ratio;
        }

        g2.clearRect(0, 0, this.w, this.h);
        drawMap(g2);
    }

    private void drawMap(Graphics2D g2) {
        g2.setStroke(new BasicStroke(1f));

        int x0 = Math.round(this.cx - this.mw / 2f);

        this.xs = new int[this.map.size()][this.map.size()];
        this.ys = new int[this.map.size()][this.map.size()];

        float xstep = this.mw / (2 * (this.map.size() - 1));
        float ystep = this.mh / (2 * (this.map.size() - 1));

        g2.setColor(Color.GRAY);

        for (int x = 0; x < this.map.size(); x++) {
            for (int y = 0; y < this.map.size(); y++) {
                Point p = getPoint(x0, xstep, ystep, x, y, this.map.get(x, y));

                this.xs[x][y] = p.x;
                this.ys[x][y] = p.y;

                if (this.showGrid) {
                    Point p0 = getPoint(x0, xstep, ystep, x, y, 0);
                    if (x > 0) {
                        Point p1 = getPoint(x0, xstep, ystep, x - 1, y, 0);
                        g2.drawLine(p0.x, p0.y, p1.x, p1.y);
                    }
                    if (y > 0) {
                        Point p1 = getPoint(x0, xstep, ystep, x, y - 1, 0);
                        g2.drawLine(p0.x, p0.y, p1.x, p1.y);
                    }
                    g2.drawLine(p0.x, p0.y, p.x, p.y);
                }
            }
        }


        g2.setColor(Color.BLACK);

        for (int x = 0; x < this.map.size(); x++) {
            for (int y = 0; y < this.map.size(); y++) {
                if (x > 0)
                    g2.drawLine(this.xs[x - 1][y], this.ys[x - 1][y], this.xs[x][y], this.ys[x][y]);
                if (y > 0)
                    g2.drawLine(this.xs[x][y - 1], this.ys[x][y - 1], this.xs[x][y], this.ys[x][y]);
            }
        }

        g2.setColor(Color.RED);

        if (sx >= 0 && sy >= 0) {
            if (sx > 0)
                g2.drawLine(this.xs[sx - 1][sy], this.ys[sx - 1][sy], this.xs[sx][sy], this.ys[sx][sy]);
            if (sy > 0)
                g2.drawLine(this.xs[sx][sy - 1], this.ys[sx][sy - 1], this.xs[sx][sy], this.ys[sx][sy]);
            if (sx < this.map.size() - 1)
                g2.drawLine(this.xs[sx + 1][sy], this.ys[sx + 1][sy], this.xs[sx][sy], this.ys[sx][sy]);
            if (sy < this.map.size() - 1)
                g2.drawLine(this.xs[sx][sy + 1], this.ys[sx][sy + 1], this.xs[sx][sy], this.ys[sx][sy]);
        }
    }

    private Point getPoint(int x0, float xstep, float ystep, int x, int y, float value) {
        float center = this.map.size() / 2f;
        double r = Math.sqrt(Math.pow(x - center, 2) + Math.pow(y - center, 2));
        double a = Math.atan2(y - center, x - center);
        double x1 = center + r * Math.cos(a + this.angle);
        double y1 = center + r * Math.sin(a + this.angle);
        return new Point(
                Math.round(x0 + xstep * (float) x1 + xstep * (float) y1),
                Math.round(this.cy - ystep * (float) x1 + ystep * (float) y1 - ystep * value)
        );
    }

    float getRatio() {
        return ratio;
    }

    void setRatio(float ratio) {
        this.ratio = ratio;
    }

    boolean isShowGrid() {
        return showGrid;
    }

    void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }

    float getAngle() {
        return angle;
    }

    void setAngle(float angle) {
        this.angle = angle;
    }

    Map getMap() {
        return map;
    }
}
