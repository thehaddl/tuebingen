import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParticleSystemTest {
    @Test
    void centerOfGravity_should_return_center_of_gravity () {
        // setup
        List<Particle> p1 = new ArrayList<>();
        p1.add(new Particle(new Vector(1,1,0), new Vector(0,0,0)));
        p1.add(new Particle(new Vector(-1,-1,0), new Vector(0,0,0)));
        ParticleSystem p = ParticleSystem.createFrom(p1);
        var actual = p.centerOfGravity();
        // verify
        assertEquals(new Vector(0,0,0), actual);
    }

    @Test
    void getParticles_should_return_particles () {
        // setup

        List<Particle> p1 = new ArrayList();

        p1.add(new Particle(new Vector(1,1,0), new Vector(1,2,3)));
        p1.add(new Particle(new Vector(-1,-1,0), new Vector(4,5,6.5)));


        ParticleSystem p = ParticleSystem.createFrom(p1);
        //execute
        var actual = p.getParticles();
        // verify

        assertEquals(2,actual.size());
        assertEquals(new Vector(1,1,0), actual.get(0).position);
        assertEquals(new Vector(1,2,3), actual.get(0).velocity);
        assertEquals(new Vector(-1,-1,0), actual.get(1).position);
        assertEquals(new Vector(4,5,6.5), actual.get(1).velocity);

    }

    @Test
    void createRandomParticles_should_return_System_with_random_positions_and_charges(){
        ParticleSystem p = ParticleSystem.createRandomPositionsByRadius(17,100);
        
        assertEquals(17,p.getParticles().size());
        for(Particle a : p.getParticles()){
            for(var b : a.position.components){
                assertTrue(b<100 && b >-100);
            }

        }
    }

    @Test
    void calcAverageDeviation_should_return_average_deviation() {
        // setup

        List<Particle> p1 = new ArrayList();
        p1.add(new Particle(new Vector(1,2,3), new Vector(2,3,4)));
        p1.add(new Particle(new Vector(3,4,5), new Vector(4,5,6.1)));
        ParticleSystem current = ParticleSystem.createFrom(p1);
        List<Particle> p2 = new ArrayList();
        p2.add(new Particle(new Vector(1.1,2,3), new Vector(2.1,3,4)));
        p2.add(new Particle(new Vector(3.1,4,5.1), new Vector(4,5.1,6)));

        ParticleSystem reference = ParticleSystem.createFrom(p2);
        //execute
        var actual = current.calcAverageDeviation(reference);
        // verify
        assertEquals((Math.sqrt(1)+ Math.sqrt(2))/20 ,actual,0.00001);
    }

}
