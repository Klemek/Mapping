package fr.klemek.mapping;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;

class MainPanel extends JPanel {

    static final float DEFAULT_RATIO = .5f;
    private static final float SIZE = 1.2f;
    private float ratio = DEFAULT_RATIO;

    private transient Map map;
    private transient ToolChangeListener toolChangeListener;

    private transient Tool tool = Tool.NODE_EXTRUDER;

    private int x0;
    private int y0;
    private float xstep;
    private float ystep;
    private int sx = -1;
    private int sy = -1;

    private int[][] xs;
    private int[][] ys;

    private boolean showGrid;
    private boolean showFaces = true;
    private boolean shiftDown;
    private float angle;

    MainPanel(Map map, ToolChangeListener toolChangeListener) {
        this.map = map;
        this.toolChangeListener = toolChangeListener;
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
        if (this.showFaces)
            this.drawFaces(g2);
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
            g2.setColor(isSelectedLine(x1, y1, x2, y2) ? Color.RED : Color.BLACK);
            g2.drawLine(this.xs[x1][y1], this.ys[x1][y1], this.xs[x2][y2], this.ys[x2][y2]);
        }
    }

    private boolean isSelectedLine(int x1, int y1, int x2, int y2) {
        return this.tool == Tool.NODE_EXTRUDER &&
                (Utils.dist2(x1, y1, sx, sy) < (this.shiftDown ? 2 : 1) ||
                        Utils.dist2(x2, y2, sx, sy) < (this.shiftDown ? 2 : 1));
    }

    private void drawFaces(Graphics2D g2) {
        for (int x = 0; x < this.map.size() - 1; x++)
            for (int y = 0; y < this.map.size() - 1; y++)
                drawFace(g2, x, y);
    }

    private void drawFace(Graphics2D g2, int x, int y) {
        int diffY = Math.round(this.map.get(x, y, false) * this.ystep);
        boolean selected = this.tool == Tool.FACE_EXTRUDER && Utils.dist2(x, y, sx, sy) < (this.shiftDown ? 3 : 1);
        if (diffY == 0 && !selected)
            return;
        g2.setColor(selected ? Color.RED : Color.BLUE);
        for (int dx = 0; dx <= 1; dx++)
            for (int dy = 0; dy <= 1; dy++)
                g2.drawLine(this.xs[x + dx][y + dy], this.ys[x + dx][y + dy], this.xs[x + dx][y + dy], this.ys[x + dx][y + dy] - diffY);
        Polygon face = getFace(x, y);
        face.translate(0, -diffY);
        g2.drawPolygon(face);
    }

    void computeMouseMoved(Point position) {
        sx = -1;
        sy = -1;
        if (this.xs == null)
            return;
        int mx = -1;
        int my = -1;
        double min = 20;
        double d;
        for (int x = 0; x < this.map.size(); x++)
            for (int y = 0; y < this.map.size(); y++)
                if (this.tool == Tool.FACE_EXTRUDER && x < this.map.size() - 1 && y < this.map.size() - 1 && this.getFace(x, y).contains(position)) {
                    this.sx = x;
                    this.sy = y;
                    return;
                } else if (this.tool == Tool.NODE_EXTRUDER && (d = position.distance(this.xs[x][y], this.ys[x][y])) < min) {
                    mx = x;
                    my = y;
                    min = d;
                }
        this.sx = mx;
        this.sy = my;
    }

    private Polygon getFace(int x, int y) {
        return new Polygon(new int[]{this.xs[x][y], this.xs[x + 1][y], this.xs[x + 1][y + 1], this.xs[x][y + 1]},
                new int[]{this.ys[x][y], this.ys[x + 1][y], this.ys[x + 1][y + 1], this.ys[x][y + 1]}, 4);
    }

    void computeMouseWheel(int amount, boolean controlDown, boolean shiftDown) {
        if (sx < 0 || sy < 0)
            return;
        if (shiftDown) {
            for (int dx = -1; dx < 2; dx++)
                for (int dy = -1; dy < 2; dy++)
                    editMap(amount, controlDown, sx + dx, sy + dy, this.tool == Tool.NODE_EXTRUDER);
        } else
            editMap(amount, controlDown, sx, sy, this.tool == Tool.NODE_EXTRUDER);
    }

    private void editMap(int amount, boolean controlDown, int x, int y, boolean node) {
        if (x >= 0 && x < this.map.size()
                && y >= 0 && y < this.map.size())
            this.map.set(x, y, this.map.get(x, y, node)
                    + amount * (controlDown ? -1f : -.1f), node);
    }

    void changeTool() {
        ArrayList<Tool> tools = new ArrayList<>(Arrays.asList(Tool.values()));
        this.tool = tools.get((tools.indexOf(this.tool) + 1) % tools.size());
        if (this.tool == Tool.FACE_EXTRUDER)
            this.showFaces = true;
        if (toolChangeListener != null)
            toolChangeListener.toolChange(this.tool);
    }

    void deleteSelected() {
        if (sx < 0 || sy < 0)
            return;
        this.map.set(sx, sy, 0f, this.tool == Tool.NODE_EXTRUDER);
    }

    float getRatio() {
        return ratio;
    }

    void setRatio(float ratio) {
        this.ratio = ratio;
    }

    void changeShowGrid() {
        this.showGrid = !showGrid;
    }

    void changeShowFaces() {
        this.showFaces = !showFaces;
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

    Tool getTool() {
        return tool;
    }
}
