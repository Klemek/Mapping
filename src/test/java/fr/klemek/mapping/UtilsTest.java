package fr.klemek.mapping;

import java.io.File;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UtilsTest {

    @Test
    public void testDist2() {
        assertEquals(0, Utils.dist2(1, 1, 1, 1));
        assertEquals(1, Utils.dist2(1, 1, 2, 1));
        assertEquals(2, Utils.dist2(1, 1, 2, 2));
    }

    @Test
    public void testSaveOpenFile() {
        File f = new File("test.txt");
        if (f.exists())
            assertTrue(f.delete());

        assertNull(Utils.openFile("test.txt"));

        assertTrue(Utils.saveFile("test.txt", "test\ntest\n"));
        assertTrue(f.exists());

        String content = Utils.openFile("test.txt");
        assertNotNull(content);
        assertEquals("test\ntest\n", content);

        assertTrue(f.delete());
    }

}