package fr.klemek.mapping;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MapTest {

    @Test
    public void testToString() {
        Map m = new Map(15);
        m.randomize();
        m.set(0, 0, 5f, false);
        assertEquals(15, m.size());
        Map m2 = new Map(m.toString());
        assertEquals(m.size(), m2.size());
        for (int x = 0; x < m.size(); x++)
            for (int y = 0; y < m.size(); y++)
                assertEquals(m.get(x, y), m2.get(x, y), 0.001f);
        assertEquals(m.get(0, 0, false), m2.get(0, 0, false), 0.001f);
    }

    @Test
    public void testReset() {
        Map m = new Map(15);
        m.randomize();
        m.set(0, 0, 5f, false);
        assertNotEquals(0, m.get(0, 0), 0.001f);
        m.reset();
        assertEquals(0, m.get(0, 0), 0.001f);
        assertEquals(0, m.get(0, 0, false), 0.001f);
    }

    @Test
    public void testChangeSize() {
        Map m = new Map(15);
        m.randomize();
        m.set(0, 0, 5f, false);
        float value = m.get(0, 0);
        assertEquals(15, m.size());
        m.changeSize(18);
        assertEquals(18, m.size());
        assertEquals(value, m.get(0, 0), 0.001f);
        assertEquals(0, m.get(17, 17), 0.001f);
        assertEquals(5f, m.get(0, 0, false), 0.001f);
    }

    @Test
    public void testRandomize() {
        Map m = new Map(15);
        assertEquals(0, m.get(0, 0), 0.001f);
        m.set(0, 0, 5f, false);
        m.randomize();
        assertNotEquals(0, m.get(0, 0), 0.001f);
        assertEquals(5f, m.get(0, 0, false), 0.001f);
    }
}