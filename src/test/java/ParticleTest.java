import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ParticleTest {
    @Test
    void getGravitationalForce_should_return_gForce(){
        //setup
        Particle p1 = new Particle(new Vector(1,1,1),new Vector(0,0,0));
        Particle p2 = new Particle(new Vector(-1,-1,-1),new Vector(0,0,0));
        //execute
        var actual = p1.getGravitationalForce(p2);
        //verify
        var vec = new Vector(-2,-2,-2);
        System.out.println(p1.mass + p2.mass);
        var force = vec.scale(Particle.GRAVITY/((12*Math.sqrt(12))));
        assertArrayEquals(force.components,actual.components,0.0000001);
    }
    @Test
    void getElectricalForce_should_return_eForce(){
        //setup
        Particle p1 = new Particle(new Vector(1,1,1),new Vector(0,0,0),1,2);
        Particle p2 = new Particle(new Vector(-1,-1,-1),new Vector(0,0,0),2,-1);
        //execute
        var actual = p1.getElectricalForce(p2);
        //verify
        var vec = new Vector(-2,-2,-2);
        var force = vec.scale((-2)/((12*Math.sqrt(12)*Particle.ELECTRICFIELDCONST*4*Math.PI)));
        assertArrayEquals(force.components,actual.components,0.00001);
    }
}