import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeviatorTests {
    @Test
    void calcDeviation_should_return_array_with_deviation_per_particle_in_list () {
        // setup
        Particle[] p1 = new Particle[]{
                new Particle(new Vector(1,2,3), new Vector(2,3,4)),
                new Particle(new Vector(3,4,5), new Vector(4,5,6))
        };
        Particle[] p2 = new Particle[]{
                new Particle(new Vector(1.1,2,3), new Vector(2.1,3,4)),
                new Particle(new Vector(3.1,4,5.1), new Vector(4,5.1,6))
        };
        var d = new Deviator(2);
        //execute
        var actual = d.calcDeviation(p1,p2);
        // verify
        assertArrayEquals(new double[]{0.1,Math.sqrt(2)/10}, actual,0.000001);
    }
    @Test
    void averageDev_should_return_average_over_deviation_array(){
        // setup
        Particle[] p1 = new Particle[]{
                new Particle(new Vector(1,2,3), new Vector(2,3,4)),
                new Particle(new Vector(3,4,5), new Vector(4,5,6))
        };
        Particle[] p2 = new Particle[]{
                new Particle(new Vector(1.1,2,3), new Vector(2.1,3,4)),
                new Particle(new Vector(3.1,4,5.1), new Vector(4,5.1,6))
        };
        var d = new Deviator(2);
        var a = d.calcDeviation(p1,p2);
        //execute
        var actual = d.averageDev(a);
        // verify
        assertEquals( (0.1 + Math.sqrt(2)/10)/2, actual,0.000001);
    }

}
