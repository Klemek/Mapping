package fr.klemek.mapping;

import java.awt.*;

import javax.swing.*;

class MainPanel extends JPanel {

    static final float DEFAULT_RATIO = .5f;
    private static final float SIZE = 1.2f;
    private float ratio = DEFAULT_RATIO;

    private transient Map map;
    private int x0;
    private int y0;
    private float xstep;
    private float ystep;
    private int sx = -1;
    private int sy = -1;

    private int[][] xs;
    private int[][] ys;

    private boolean showGrid;
    private boolean shiftDown;
    private float angle;

    MainPanel(Map map) {
        this.map = map;
        new MouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));

        g2.clearRect(0, 0, this.getWidth(), this.getHeight());
        this.computeNodes();
        if (this.showGrid)
            this.drawGrid(g2);
        this.drawMap(g2);
    }

    private void computeNodes() {
        int w = this.getWidth();
        int h = this.getHeight();

        float mw;
        float mh;
        if (w > h) {
            mh = h * SIZE * ratio;
            mw = mh / ratio;
        } else {
            mw = w * SIZE;
            mh = mw * ratio;
        }

        this.x0 = Math.round(w / 2f - mw / 2f);
        this.y0 = Math.round(h / 2f);

        this.xs = new int[this.map.size()][this.map.size()];
        this.ys = new int[this.map.size()][this.map.size()];

        this.xstep = mw / (2 * (this.map.size() - 1));
        this.ystep = mh / (2 * (this.map.size() - 1));

        for (int x = 0; x < this.map.size(); x++) {
            for (int y = 0; y < this.map.size(); y++) {
                Point p = this.getPoint(x, y, this.map.get(x, y));
                this.xs[x][y] = p.x;
                this.ys[x][y] = p.y;
            }
        }
    }

    private void drawGrid(Graphics2D g2) {
        g2.setColor(Color.GRAY);
        for (int x = 0; x < this.map.size(); x++) {
            for (int y = 0; y < this.map.size(); y++) {
                Point p0 = this.getPoint(x, y, 0);
                if (x > 0) {
                    Point p1 = this.getPoint(x - 1, y, 0);
                    g2.drawLine(p0.x, p0.y, p1.x, p1.y);
                }
                if (y > 0) {
                    Point p1 = this.getPoint(x, y - 1, 0);
                    g2.drawLine(p0.x, p0.y, p1.x, p1.y);
                }
                g2.drawLine(p0.x, p0.y, this.xs[x][y], this.ys[x][y]);
            }
        }
    }

    private void drawMap(Graphics2D g2) {


        for (int x = 0; x < this.map.size(); x++) {
            for (int y = 0; y < this.map.size(); y++) {
                this.drawLine(g2, x - 1, y, x, y);
                this.drawLine(g2, x, y - 1, x, y);
            }
        }
    }

    private Point getPoint(int x, int y, float value) {
        float center = this.map.size() / 2f;
        double r = Math.sqrt(Math.pow(x - center, 2) + Math.pow(y - center, 2));
        double a = Math.atan2(y - center, x - center);
        double x1 = center + r * Math.cos(a + this.angle);
        double y1 = center + r * Math.sin(a + this.angle);
        return new Point(
                Math.round(this.x0 + this.xstep * (float) x1 + this.xstep * (float) y1),
                Math.round(this.y0 - this.ystep * (float) x1 + this.ystep * (float) y1 - this.ystep * value)
        );
    }

    private void drawLine(Graphics2D g2, int x1, int y1, int x2, int y2) {
        if (x1 >= 0 && x1 < this.map.size() &&
                y1 >= 0 && y1 < this.map.size() &&
                x2 >= 0 && x2 < this.map.size() &&
                y2 >= 0 && y2 < this.map.size()) {
            if (Utils.dist2(x1, y1, sx, sy) < (this.shiftDown ? 2 : 1) || Utils.dist2(x2, y2, sx, sy) < (this.shiftDown ? 2 : 1)) {
                g2.setColor(Color.RED);
            } else {
                g2.setColor(Color.BLACK);
            }
            g2.drawLine(this.xs[x1][y1], this.ys[x1][y1], this.xs[x2][y2], this.ys[x2][y2]);
        }
    }

    void computeMouseMoved(Point position) {
        sx = -1;
        sy = -1;
        if (MainPanel.this.xs != null) {
            for (int x = 0; x < MainPanel.this.map.size(); x++)
                for (int y = 0; y < MainPanel.this.map.size(); y++)
                    if (position.distance(MainPanel.this.xs[x][y], MainPanel.this.ys[x][y]) < 6) {
                        sx = x;
                        sy = y;
                        return;
                    }
        }
    }

    void computeMouseWheel(int amount, boolean controlDown, boolean shiftDown) {
        if (sx >= 0 && sy >= 0) {
            if (shiftDown) {
                for (int dx = -1; dx < 2; dx++)
                    for (int dy = -1; dy < 2; dy++)
                        changeNode(amount, controlDown, sx + dx, sy + dy);
            } else
                changeNode(amount, controlDown, sx, sy);
        }
    }

    private void changeNode(int amount, boolean controlDown, int x, int y) {
        if (x >= 0 && x < this.map.size()
                && y >= 0 && y < this.map.size())
            this.map.set(x, y, MainPanel.this.map.get(x, y)
                    + amount * (controlDown ? -1f : -.1f));
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

    void setShiftDown(boolean shiftDown) {
        this.shiftDown = shiftDown;
    }
}
