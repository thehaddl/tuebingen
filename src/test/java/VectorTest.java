import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VectorTest {

    @Test
    void equals_should_return_false_if_not_equals() {
        var v1 = new Vector(1,2,3);
        var v2 = new Vector(4,5,6);

        var actual = v1.equals(v2);

        assertFalse(actual);
    }

    @Test
    void equals_should_return_true_if_equals() {
        var v1 = new Vector(1,2,3);
        var v2 = new Vector(1,2,3);

        var actual = v1.equals(v2);

        assertTrue(actual);
    }

    @Test
    void toString_should_return_coordinates() {
        var v1 = new Vector(1,2,3);

        var actual = v1.toString();

        assertEquals("Vector[1.0, 2.0, 3.0]", actual);
    }


    @Test
    void add_should_add_two_vectors() {
        // setup
        var v1 = new Vector(1,2,3);
        var v2 = new Vector(10,20,30);

        //execute
        var actual = v1.add(v2);

        // verify
        assertEquals(new Vector(11, 22, 33), actual);
    }

}
