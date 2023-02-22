package edu.emory.cs;

import edu.emory.cs.utils.Utils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UtilsTest {
    @Test
    public void getMiddleIndexTest() {
        assertEquals(5, Utils.getMiddleIndex(0, 10));
    }
}
