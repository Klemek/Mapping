package fr.klemek.mapping;

import java.util.Random;

class Map {

    private float[][] m;

    Map(String input) {
        String[] lines = input.split("\n");
        this.m = new float[lines.length][lines.length];
        for (int x = 0; x < lines.length; x++) {
            String[] lineSplit = lines[x].split(";");
            for (int y = 0; y < Math.min(lines.length, lineSplit.length); y++) {
                try {
                    this.m[x][y] = Float.parseFloat(lineSplit[y]);
                } catch (NumberFormatException ignored) {
                }
            }
        }
    }

    Map(int size) {
        this.m = new float[size][size];
    }

    int size() {
        return this.m.length;
    }

    float get(int x, int y) {
        if (x < 0 || y < 0 || x >= this.m.length || y >= this.m.length)
            return 0f;
        return this.m[x][y];
    }

    void set(int x, int y, float value) {
        if (x < 0 || y < 0 || x >= this.m.length || y >= this.m.length)
            return;
        this.m[x][y] = value;
    }

    void reset() {
        this.m = new float[this.m.length][this.m.length];
    }

    void changeSize(int newSize) {
        if (newSize > 1) {
            float[][] m2 = new float[newSize][newSize];
            for (int x = 0; x < newSize; x++)
                for (int y = 0; y < newSize; y++)
                    if (x < this.m.length && y < this.m.length)
                        m2[x][y] = this.m[x][y];
            this.m = m2;
        }
    }

    void randomize() {
        Random r = new Random();
        for (int x = 0; x < this.m.length; x++)
            for (int y = 0; y < this.m.length; y++)
                this.m[x][y] += r.nextFloat() - 0.5f;
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
        return output.toString();
    }
}
