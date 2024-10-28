import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ParticleTest {
    @Test
    void getGravitationalForce_should_return_force(){
        //setup
        Particle p1 = new Particle(new Vector(1,1,1),new Vector(0,0,0));
        Particle p2 = new Particle(new Vector(-1,-1,-1),new Vector(0,0,0));
        //execute
        var actual = p1.getGavitationalForce(p2);
        //verify
        var vec = new Vector(-2,-2,-2);
        var force = vec.scale(6.67/(12*Math.sqrt(12)));
        assertArrayEquals(force.components,actual.components,0.0000001);
    }
}