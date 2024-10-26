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

    @Test
    void subtract_should_subtract_two_vectors() {
        // setup
        var v1 = new Vector(1,2,3);
        var v2 = new Vector(2,1,5);

        //execute
        var actual = v1.subtract(v2);

        // verify
        assertEquals(new Vector(-1, 1, -2), actual);
    }

    @Test
    void scale_should_scale_vector_by_scalar() {
        // setup
        var v1 = new Vector(1,2,3);
        double scalar = 3.0;

        //execute
        var actual = v1.scale(scalar);

        // verify
        assertEquals(new Vector(3, 6, 9), actual);
    }
    @Test
    void getMagnitude_should_return_magnitude_of_vector() {
        // setup
        var v1 = new Vector(1,2,3);

        //execute
        var actual = v1.getMagnitude();

        // verify
        assertEquals(Math.sqrt(14), actual);
    }

    @Test
    void getUnitVec_should_return_unitVector_of_vector() {
        // setup
        var v1 = new Vector(1,2,3);

        //execute
        var actual = v1.scale(1/v1.getMagnitude());

        // verify
        assertEquals(new Vector(1/Math.sqrt(14),2/Math.sqrt(14),3/Math.sqrt(14)), actual);
    }

    @Test
    void calcDistance_should_return_distance_between_particles() {
        // setup
        var v1 = new Vector(1,2,3);
        var v2 = new Vector(2,2,3);

        //execute
        var actual = v1.calcDistance(v2);

        // verify
        assertEquals(1, actual);
    }
    @Test
    void getInteractionForce_between_Particles() {
        // setup
        var v1 = new Vector(1,2,3);
        var v2 = new Vector(4,6,7);
        double forceScalar = (6.67 / (Math.sqrt(41)*Math.sqrt(41)));
        //execute
        var actual = v1.interactionForce(v1.calcDistance(v2));

        // verify
        assertEquals(v1.getUnitVec().scale(forceScalar), actual);
    }

}
