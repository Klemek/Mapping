package fr.klemek.mapping;

import java.util.Random;

class Map {

    private float[][] m;
    private float[][] m2;

    Map(String input) {
        String[] parts = input.split("\n\n");
        String[] lines = parts[0].split("\n");
        this.m = new float[lines.length][lines.length];
        for (int x = 0; x < this.m.length; x++) {
            String[] lineSplit = lines[x].split(";");
            for (int y = 0; y < Math.min(this.m.length, lineSplit.length); y++) {
                try {
                    this.m[x][y] = Float.parseFloat(lineSplit[y]);
                } catch (NumberFormatException ignored) {
                    //ignored
                }
            }
        }
        this.m2 = new float[lines.length - 1][lines.length - 1];
        if (parts.length > 1) {
            lines = parts[1].split("\n");
            for (int x = 0; x < Math.min(this.m2.length, lines.length); x++) {
                String[] lineSplit = lines[x].split(";");
                for (int y = 0; y < Math.min(this.m2.length, lineSplit.length); y++) {
                    try {
                        this.m2[x][y] = Float.parseFloat(lineSplit[y]);
                    } catch (NumberFormatException ignored) {
                        //ignored
                    }
                }
            }
        }
    }

    Map(int size) {
        this.m = new float[size][size];
        this.m2 = new float[size - 1][size - 1];
    }

    int size() {
        return this.m.length;
    }

    float get(int x, int y) {
        return this.get(x, y, true);
    }

    float get(int x, int y, boolean primary) {
        if (x < 0 || y < 0 || x >= this.m.length - (primary ? 0 : 1) || y >= this.m.length - (primary ? 0 : 1))
            return 0f;
        return primary ? this.m[x][y] : this.m2[x][y];
    }

    void set(int x, int y, float value) {
        this.set(x, y, value, true);
    }

    void set(int x, int y, float value, boolean primary) {
        if (x < 0 || y < 0 || x >= this.m.length - (primary ? 0 : 1) || y >= this.m.length - (primary ? 0 : 1))
            return;
        if (primary)
            this.m[x][y] = value;
        else
            this.m2[x][y] = value;
    }

    void reset() {
        this.m = new float[this.m.length][this.m.length];
        this.m2 = new float[this.m.length - 1][this.m.length - 1];
    }

    void changeSize(int newSize) {
        if (newSize < 2)
            return;
        float[][] tm = new float[newSize][newSize];
        float[][] tm2 = new float[newSize - 1][newSize - 1];
        for (int x = 0; x < newSize; x++)
            for (int y = 0; y < newSize; y++) {
                if (x < this.m.length && y < this.m.length)
                    tm[x][y] = this.m[x][y];
                if (x < this.m2.length && y < this.m2.length)
                    tm2[x][y] = this.m2[x][y];
            }
        this.m = tm;
        this.m2 = tm2;
    }

    void randomize() {
        Random r = new Random();
        for (int x = 0; x < this.m.length; x++)
            for (int y = 0; y < this.m.length; y++)
                this.m[x][y] += r.nextInt(11) * .1f - 0.5f;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (int x = 0; x < this.size(); x++) {
            for (int y = 0; y < this.size(); y++) {
                output.append(this.m[x][y]);
                output.append(';');
            }
            output.append('\n');
        }
        output.append('\n');
        for (int x = 0; x < this.size() - 1; x++) {
            for (int y = 0; y < this.size() - 1; y++) {
                output.append(this.m2[x][y]);
                output.append(';');
            }
            output.append('\n');
        }
        return output.toString();
    }
}
