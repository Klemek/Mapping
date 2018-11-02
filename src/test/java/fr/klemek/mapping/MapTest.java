package fr.klemek.mapping;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MapTest {

    @Test
    public void testToString() {
        Map m = new Map(15);
        m.randomize();
        assertEquals(15, m.size());
        Map m2 = new Map(m.toString());
        assertEquals(m.size(), m2.size());
        for (int x = 0; x < m.size(); x++)
            for (int y = 0; y < m.size(); y++)
                assertEquals(m.get(x, y), m2.get(x, y), 0.001f);
    }
}