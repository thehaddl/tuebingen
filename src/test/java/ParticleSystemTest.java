import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ParticleSystemTest {

    private ParticleSystem s1;
    private ParticleSystem s2;
    private ParticleSystem identicalSystem;

    @BeforeEach
    void setUp() {
        List<Particle> p1 = new ArrayList<>();
        p1.add(new Particle(new Vector(0, 0, 0), new Vector(1, 0, 0), 1.0, 1));
        p1.add(new Particle(new Vector(1, 0, 0), new Vector(-1, 0, 0), 1.0, -1));
        p1.add(new Particle(new Vector(0, 1, 0), new Vector(0, 1, 0), 2.0, 1));

        List<Particle> p2 = new ArrayList<>();
        p2.add(new Particle(new Vector(0.1, 0.05, 0), new Vector(1.02, 0, 0), 1.0, 1));
        p2.add(new Particle(new Vector(2, 1, 0), new Vector(-2, 0, 0), 1.0, -1));
        p2.add(new Particle(new Vector(1, 2, 0), new Vector(0, 2, 0), 2.0, 1));

        List<Particle> identicalParticles = new ArrayList<>();
        identicalParticles.add(new Particle(new Vector(0, 0, 0), new Vector(1, 0, 0), 1.0, 1));
        identicalParticles.add(new Particle(new Vector(1, 0, 0), new Vector(-1, 0, 0), 1.0, -1));
        identicalParticles.add(new Particle(new Vector(0, 1, 0), new Vector(0, 1, 0), 2.0, 1));


        s1 =  ParticleSystem.createFrom(p1);
        s2 =  ParticleSystem.createFrom(p2);
        identicalSystem =  ParticleSystem.createFrom(identicalParticles);
    }
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
    void compare_should_return_zero_deviation_for_identical_systems() {
        SimulationComparer result = s2.compare(identicalSystem);

        assertEquals(0.0, result.getMeanPositionError(), 1e-10);
        assertEquals(0.0, result.getRmsePositionError(), 1e-10);
        assertEquals(0.0, result.getMaxPositionError(), 1e-10);

        assertEquals(0.0, result.getMeanVelocityError(), 1e-10);
        assertEquals(0.0, result.getRmseVelocityError(), 1e-10);
        assertEquals(0.0, result.getMaxVelocityError(), 1e-10);

        assertEquals(0.0, result.getKineticEnergyError(), 1e-10);
        assertEquals(0.0, result.getPotentialEnergyError(), 1e-10);
        assertEquals(0.0, result.getTotalEnergyError(), 1e-10);

        assertEquals(0.0, result.getCenterOfMassDrift(), 1e-10);
        assertEquals(0.0, result.getMeanPairDistanceError(), 1e-10);
        assertEquals(0.0, result.getSystemSpreadError(), 1e-10);
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


}
